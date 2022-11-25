package com.cnjava.moviereview.view.activity.video;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ActivityMainBinding;
import com.cnjava.moviereview.databinding.ActivityVideoBinding;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.view.adapter.PlayListAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;

import java.util.ArrayList;
import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoActivity extends AppCompatActivity implements PlayListAdapter.PlayListCallBack {

    private ArrayList<String> ids;
    private int nextVideo = 0;
    private Video video;
    private YouTubePlayer mYouTubePlayer;
    private ActivityVideoBinding binding;
    private boolean isInPipMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ids = getIntent().getStringArrayListExtra("ids");
        video = (Video) getIntent().getSerializableExtra("video");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            binding.ivInPicture.setVisibility(View.GONE);
        }
        Optional<String> name = video.results.stream().filter(obj -> obj.key.equals(ids.get(0))).map(Video.VideoDetail::getName).findFirst();
        name.ifPresent(s -> binding.tvShowsTextVideo.setText(s));
        ImageView ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(binding.youtubePlayerView);
        initPictureInPicture();
        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                // using pre-made custom ui
                binding.progressCircularProfile.setVisibility(View.GONE);
                binding.rvResultPlayList.setVisibility(View.VISIBLE);
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(binding.youtubePlayerView, youTubePlayer);
                binding.youtubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());

                //setPlayNextVideoButtonClickListener(youTubePlayer);

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        ids.get(nextVideo),
                        0f
                );
            }

        };


        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        binding.youtubePlayerView.initialize(listener, options);

        RecyclerView rvPlayList = findViewById(R.id.rvResultPlayList);

        PlayListAdapter videoAdapter = new PlayListAdapter(this, video, this);
        rvPlayList.setAdapter(videoAdapter);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.youtubePlayerView.enterFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.youtubePlayerView.exitFullScreen();
        }
    }

    /**
     * Set a click listener on the "Play next video" button
     */
    /*private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        TextView playNextVideoButton = findViewById(R.id.tv_shows_text_video);

        playNextVideoButton.setOnClickListener(view -> {
                    nextVideo++;
                    if (nextVideo > ids.size() - 1) {
                        nextVideo = 0;
                    }
                    YouTubePlayerUtils.loadOrCueVideo(
                            youTubePlayer,
                            getLifecycle(),
                            ids.get(nextVideo),
                            0f
                    );
                }

        );

    }*/
    private void initPictureInPicture() {
        binding.ivInPicture.setOnClickListener(view -> {
            boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
            if (supportsPIP)
                enterPictureInPictureMode();
        });
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        }

        if (isInPictureInPictureMode) {
            binding.youtubePlayerView.enterFullScreen();
        } else {
            binding.youtubePlayerView.exitFullScreen();
        }
        isInPipMode = isInPictureInPictureMode;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isInPipMode){
            finish();
        }
    }

    @Override
    public void gotoVideoYoutube(Video.VideoDetail videoDetail) {
        if(mYouTubePlayer != null) {
            YouTubePlayerUtils.loadOrCueVideo(
                    mYouTubePlayer,
                    getLifecycle(),
                    videoDetail.key,
                    0f
            );
            binding.tvShowsTextVideo.setText(videoDetail.name);
        }
    }
}

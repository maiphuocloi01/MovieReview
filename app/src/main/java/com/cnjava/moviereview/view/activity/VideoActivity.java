package com.cnjava.moviereview.view.activity;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.R;
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

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoActivity extends AppCompatActivity implements PlayListAdapter.PlayListCallBack {

    private YouTubePlayerView youTubePlayerView;
    private ArrayList<String> ids;
    private int nextVideo = 0;
    private Video video;
    private YouTubePlayer mYouTubePlayer;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ids = getIntent().getStringArrayListExtra("ids");
        video = (Video) getIntent().getSerializableExtra("video");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        tvName = findViewById(R.id.tv_shows_text_video);

        ImageView ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView rvPlayList = findViewById(R.id.rvResultPlayList);

        PlayListAdapter videoAdapter = new PlayListAdapter(this, video, this);
        rvPlayList.setAdapter(videoAdapter);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture();

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                // using pre-made custom ui
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());

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
        youTubePlayerView.initialize(listener, options);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen();
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
        ImageView enterPipMode = findViewById(R.id.ivInPicture);
        enterPipMode.setOnClickListener(view -> {
            boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
            if (supportsPIP)
                enterPictureInPictureMode();
        });
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if (isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
        } else {
            youTubePlayerView.exitFullScreen();
        }
    }

    @Override
    public void gotoVideoYoutube(Video.VideoDetail videoDetail) {
        YouTubePlayerUtils.loadOrCueVideo(
                mYouTubePlayer,
                getLifecycle(),
                videoDetail.key,
                0f
        );
        tvName.setText(videoDetail.name);
    }
}

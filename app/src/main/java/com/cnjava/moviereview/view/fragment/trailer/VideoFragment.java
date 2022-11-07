package com.cnjava.moviereview.view.fragment.trailer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentVideoBinding;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoFragment extends BaseFragment<FragmentVideoBinding, CommonViewModel> {

    public static final String TAG = VideoFragment.class.getName();
    private Object mData;
    private YouTubePlayerView youTubePlayerView;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        String youtubeId = (String) mData;
        Log.d(TAG, "initViews: " + youtubeId);

        youTubePlayerView = binding.videoYoutube2;
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
                Log.d(TAG, "onError: " + youtubeId);
                youTubePlayer.loadVideo(youtubeId, 0);
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = youtubeId;
                Log.d(TAG, "onReady: " + videoId);
                youTubePlayer.loadVideo(youtubeId, 0);
            }

        });

        binding.ivCloseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.release();
                callBack.backToPrev();
            }
        });

        /*binding.ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.enterFullScreen();
            }
        });*/

    }

    @Override
    protected FragmentVideoBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentVideoBinding.inflate(inflater, container, false);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        youTubePlayerView.release();
    }
}

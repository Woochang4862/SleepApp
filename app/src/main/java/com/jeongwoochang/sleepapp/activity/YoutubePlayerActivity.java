package com.jeongwoochang.sleepapp.activity;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.util.data.Item;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubePlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private Item data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        data = ((Item) getIntent().getSerializableExtra("item"));
        youTubePlayerView = findViewById(R.id.youtube_player_view);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        data.getId().getVideoId(),
                        0f
                );

                addFullScreenListenerToPlayer();
                setPlayNextVideoButtonClickListener(youTubePlayer);
            }
        });
    }

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //fullScreenHelper.enterFullScreen();

                addCustomActionsToPlayer();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //fullScreenHelper.exitFullScreen();

                removeCustomActionsFromPlayer();
            }
        });
    }

    private void addCustomActionsToPlayer() {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YoutubePlayerActivity.this, "custom action1 clicked", Toast.LENGTH_SHORT).show();
            }
        });

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YoutubePlayerActivity.this, "custom action1 clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }

    /**
     * Set a click listener on the "Play next video" button
     */
    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        "POdP0qe6IN4", 0f);
            }
        });
    }
}

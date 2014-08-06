package com.sathittham.watwanghin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.content.Intent;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class VDOActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {


    // public static final String API_KEY = "AIzaSyD4v5-ipnGWTw7QZe6LYSh31cFA-ow6grA";

    private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyD4v5-ipnGWTw7QZe6LYSh31cFA-ow6grA";
    private static final int RECOVERY_DIALOG_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vdo);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YoutubeDeveloperKey, this);

    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onInitializationSuccess(Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            YPlayer = player;
            /*
             * Now that this variable YPlayer is global you can access it
             * throughout the activity, and perform all the player actions like
             * play, pause and seeking to a position by code.
             */
            YPlayer.cueVideo("2zNSgSzhBfM");
        }
    }

}
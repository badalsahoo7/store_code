package com.example.vivify_technocrats;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class PlayingVideos extends AppCompatActivity {

    private static final String YOUTUBE_API_KEY = "YOUR_YOUTUBE_API_KEY"; // Replace with your actual YouTube API Key

    Button buttonback2;
    YouTubePlayerView youTubePlayerView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_videos);

        buttonback2 = findViewById(R.id.buttonback2);
        buttonback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayingVideos.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("videos").child("video1");

        // Fetch the video URL from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String videoUrl = dataSnapshot.getValue(String.class);
                if (videoUrl != null) {
                    playVideo(videoUrl);
                } else {
                    Toast.makeText(PlayingVideos.this, "Video URL not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayingVideos.this, "Failed to load video URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playVideo(final String videoUrl) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull final com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                // Extract the video ID from the URL
                String videoId = extractVideoIdFromUrl(videoUrl);
                youTubePlayer.loadVideo(videoId, 0);
            }

            @Override
            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if (state == PlayerConstants.PlayerState.ENDED) {
                    // Video has ended, start DashboardActivity
                    startActivity(new Intent(PlayingVideos.this, DashboardActivity.class));
                    finish();
                }
            }
        });
    }

    private String extractVideoIdFromUrl(String url) {
        String videoId = null;
        if (url != null) {
            String[] split = url.split("v=");
            if (split.length > 1) {
                videoId = split[1];
                int ampersandIndex = videoId.indexOf('&');
                if (ampersandIndex != -1) {
                    videoId = videoId.substring(0, ampersandIndex);
                }
            } else {
                split = url.split("/");
                videoId = split[split.length - 1];
                if (videoId.contains("?")) {
                    videoId = videoId.split("\\?")[0];
                }
            }
        }
        return videoId;
    }
}

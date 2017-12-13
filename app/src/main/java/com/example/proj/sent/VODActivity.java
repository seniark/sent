package com.example.proj.sent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VODActivity extends YouTubeBaseActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mVodRef;
    private String video_url;
    private TextView content;
    Button b;
    YouTubePlayerView ypv;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vod);


        video_url = "";

        mDatabase = FirebaseDatabase.getInstance();
        mVodRef = mDatabase.getReference("vod");



        ypv=(YouTubePlayerView)findViewById(R.id.my_youtube_view);
        b=(Button)findViewById(R.id.play_button);
        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                Log.d("yt", "loading: " + video_url);
                youTubePlayer.loadVideo(video_url);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                video_url = dataSnapshot.getValue(String.class);
                Log.d("yt", "registering onClick...");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ypv.initialize("AIzaSyAPM5u5xJWKy95zSyvq8Kccu9BfgRCNIXs",
                                onInitializedListener);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("db", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mVodRef.addValueEventListener(tickListener);




    }
}

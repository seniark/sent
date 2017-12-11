package com.example.proj.sent;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VODActivity extends BaseNavDrawerActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mVodRef;
    private String video_url;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_vod, contentFrameLayout);

        getSupportActionBar().setTitle("Video of the Day");
        video_url = "";

        content = findViewById(R.id.vodContent);

        mDatabase = FirebaseDatabase.getInstance();
        mVodRef = mDatabase.getReference("vod");

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                video_url = dataSnapshot.getValue(String.class);
                content.setText(video_url);
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

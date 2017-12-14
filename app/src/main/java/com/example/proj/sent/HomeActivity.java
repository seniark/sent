package com.example.proj.sent;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends BaseNavDrawerActivity{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;
    private TextView mTickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);

        getSupportActionBar().setTitle("Home");

        mTickCount = findViewById(R.id.homeTCount);

        mDatabase = FirebaseDatabase.getInstance();
        mTickListRef = mDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("test", "in onDataChange");
                int i=0;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    i++;
                }

                mTickCount.setText(Integer.toString(i));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("dbg", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mTickListRef.addValueEventListener(tickListener);
    }

    protected void getStartedClick(View v)
    {
        Intent i = new Intent(HomeActivity.this, TickListActivity.class);
        startActivity(i);
    }
}

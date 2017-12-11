package com.example.proj.sent;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;




public class TickListActivity extends BaseNavDrawerActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;

    //@GlideModule
    //public class MyAppGlideModule extends AppGlideModule {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.tick_card_view_layout, contentFrameLayout);
        loadCardImage();

        mDatabase = FirebaseDatabase.getInstance();
        mTickListRef = mDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot tickSnapshot : dataSnapshot.getChildren())
                {
                    //deal with tick object
                    Log.d("db", tickSnapshot.getValue(Tick.class).getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("db", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mTickListRef.addValueEventListener(tickListener);

        //testDB();

    }

    protected void testDB()
    {
        ArrayList<Tick> t = new ArrayList<>();
        Tick t1 = new Tick("TestRoute", "5.1");
        Tick t2 = new Tick("TestRoute2", "5.2");
        t.add(t1);
        t.add(t2);
        postTicks(t);
    }

    protected void postTicks(List<Tick> t)
    {
        mTickListRef.setValue(t);
    }

    protected void loadCardImage()
    {
        ImageView iv = findViewById(R.id.tickBanner);
        GlideApp.with(this).load(R.drawable.elcap).into(iv);

    }

}

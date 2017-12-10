package com.example.proj.sent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class TickListActivity extends BaseNavDrawerActivity {

    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_list, contentFrameLayout);
        mDatabase = FirebaseDatabase.getInstance();
        testDB();

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
        DatabaseReference myRef = mDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.setValue(t);
    }
}

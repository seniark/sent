package com.example.proj.sent;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class TickListActivity extends BaseNavDrawerActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;
    private List<Tick> mTickList;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_list, contentFrameLayout);

        mTickList = new ArrayList<>();
        //loadCardImage();

        recycler_view = findViewById(R.id.tickRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recycler_view.setLayoutManager(layoutManager);

        final TickRecyclerAdapter adapter_items = new TickRecyclerAdapter(mTickList, getApplicationContext());
        setClickListener(adapter_items);

        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(adapter_items);

        mDatabase = FirebaseDatabase.getInstance();
        mTickListRef = mDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot tickSnapshot : dataSnapshot.getChildren())
                {
                    //deal with tick object
                    mTickList.add(tickSnapshot.getValue(Tick.class));
                    recycler_view.getAdapter().notifyItemInserted(mTickList.size());
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

        testDB();

    }

    protected void setClickListener(TickRecyclerAdapter adapter_items)
    {
        adapter_items.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                /*
                Movie mMovie = mMovieList.get(position);

                Bundle args = new Bundle();
                args.putString(MovieDetailActivity.ARG_TITLE, mMovie.getName());
                args.putString(MovieDetailActivity.ARG_YEAR, mMovie.getYear());
                args.putString(MovieDetailActivity.ARG_POSTER, mMovie.getPoster_uri());
                args.putString(MovieDetailActivity.ARG_DESCRIPTION, mMovie.getDescription());
                args.putDouble(MovieDetailActivity.ARG_RATING, mMovie.getRating());
                args.putString(MovieDetailActivity.ARG_STARS, mMovie.getStars());

                Intent i = new Intent();
                i.putExtras(args);
                i.setClass(MainActivity.this, MovieDetailActivity.class);
                startActivity(i);
                */
                Log.d("clicks", "registered tick click");

            }
            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }






    protected void testDB()
    {
        ArrayList<Tick> t = new ArrayList<>();
        Tick t1 = new Tick("TestRoute", "5.1");
        Tick t2 = new Tick("TestRoute2", "5.2");
        Tick t3 = new Tick("TestRoute3","5.11a");
        Tick t4 = new Tick("TestRoute4", "5.10b");
        t.add(t1);
        t.add(t2);
        t.add(t3);
        t.add(t4);
        postTicks(t);
    }

    protected void postTicks(List<Tick> t)
    {
        mTickListRef.setValue(t);
    }

    protected void loadCardImage()
    {
        ImageView iv = findViewById(R.id.tickBanner);
        GlideApp.with(this).load(R.drawable.min_mtn_1).into(iv);

    }

}

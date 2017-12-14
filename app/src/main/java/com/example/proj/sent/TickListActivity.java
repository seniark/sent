package com.example.proj.sent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TickListActivity extends BaseNavDrawerActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;
    private List<Tick> mTickList;
    private RecyclerView recycler_view;
    private List<String> firebaseImages = Arrays.asList(
            "https://firebasestorage.googleapis.com/v0/b/sent-55f87.appspot.com/o/min_mtn_1.jpg?alt=media&token=9525eb9b-da1f-4dcf-8be3-3c807992f77b",
            "https://firebasestorage.googleapis.com/v0/b/sent-55f87.appspot.com/o/min_mtn_2.png?alt=media&token=b7d2dea3-7f33-4e5e-b219-d9a4e6966dd5",
            "https://firebasestorage.googleapis.com/v0/b/sent-55f87.appspot.com/o/min_mtn_4.jpg?alt=media&token=62d8e17b-abb4-4846-aea3-0dd721453ca0",
            "https://firebasestorage.googleapis.com/v0/b/sent-55f87.appspot.com/o/min_mtn_5.jpg?alt=media&token=6d826d65-1dc0-4ced-bd47-a87a5f91b14e");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_list, contentFrameLayout);

        getSupportActionBar().setTitle("Tick List");

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

                //RecyclerView.Adapter tra = recycler_view.getAdapter();
                //while(tra.getItemCount() != 0)
                //{
                //    tra.notifyItemRemoved(0);
                //}
                Log.d("dbg", "onDataChange");

                //int oldsize = mTickList.size();

                if(mTickList.size() > 0)
                {
                    recycler_view.getAdapter().notifyItemRangeRemoved(0,mTickList.size() - 1);
                }

                mTickList.clear();
                for(DataSnapshot tickSnapshot : dataSnapshot.getChildren())
                {
                    //deal with tick object
                    Tick curTick = tickSnapshot.getValue(Tick.class);
                    mTickList.add(curTick);
                    Log.d("dbg", tickSnapshot.getValue(Tick.class).getName());
                }

                recycler_view.getAdapter().notifyItemRangeInserted(0,mTickList.size() - 1);
                recycler_view.getAdapter().notifyDataSetChanged();

                //This is a brand new tick, go to detail view for it
                /*
                if(mTickList.size() > oldsize)
                {
                    Bundle args = new Bundle();
                    args.putString(TickDetailActivity.ARG_TICKNUM, Integer.toString(mTickList.size() -1));

                    Intent i = new Intent();
                    i.putExtras(args);
                    i.setClass(TickListActivity.this, TickDetailActivity.class);
                    startActivity(i);

                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("dbg", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mTickListRef.addValueEventListener(tickListener);

        //testDB();

    }

    protected void setClickListener(TickRecyclerAdapter adapter_items)
    {
        adapter_items.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Log.d("dbg", "adapter item onClick, TickListActivity: sending ticknum");

                Bundle args = new Bundle();
                Tick passToDetail = mTickList.get(position);
                args.putString(TickDetailActivity.ARG_TICKID,passToDetail.getDbkey());

                Intent i = new Intent();
                i.putExtras(args);
                i.setClass(TickListActivity.this, TickDetailActivity.class);
                startActivity(i);
            }
            @Override
            public void onItemLongClick(View v, int position) {}
        });
    }

    protected void postTicks(List<Tick> t)
    {
        mTickListRef.setValue(t);
    }

    protected void postTickAtEnd(Tick t)
    {
        Log.d("dbg", "postTickAtEnd in TickListActivity: " + t.getName());
        mTickList.add(t);
        DatabaseReference sp = mTickListRef.push();
        t.setDbkey(sp.getKey());
        sp.setValue(t);
    }

    protected void onFabClick(View v)
    {
        Random rand = new Random();
        int n = rand.nextInt(firebaseImages.size());
        Tick t = new Tick("New Tick",
                "5.X",
                firebaseImages.get(n),
                "Add Notes...",
                "Set Location",
                "Set Date");

        postTickAtEnd(t);
        Log.d("dbg", "FAB clicked");





    }

}

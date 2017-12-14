package com.example.proj.sent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TAK-DEV on 12/6/2017.
 */

public class TickRecyclerAdapter extends RecyclerView.Adapter<TickRecyclerAdapter.ViewHolder> {
    List<Tick> list_tick;
    OnItemClickListener vc=null;
    Context mCtx_MR;
    RecyclerView mRV;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView route_name;
        public TextView route_grade;
        public ImageView route_img;

        public ViewHolder(View view) {
            super(view);
            route_name = view.findViewById(R.id.routeNameTextView);
            route_grade = view.findViewById(R.id.routeGradeTextView);
            route_img= view.findViewById(R.id.tickBanner);
        }
    }

    public TickRecyclerAdapter(List<Tick> list_tick, Context mCtx) {
        this.list_tick = list_tick;
        this.mCtx_MR = mCtx;
    }

    public void setOnItemClickListener(OnItemClickListener _vo){
        vc=_vo;
        Log.d("dbg", "setOnItemClickListener Called");
        if(_vo.equals(null)) Log.d("Clicks", "But it was null");
    }

    @Override
    public TickRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tick_card_view_layout, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vc!=null) {
                    vc.onItemClick(v, view_holder.getAdapterPosition());
                    Log.d("Clicks", "Sending click to view holder...");
                }
                else
                {
                    Log.d("Clicks", "Vc was null onClick");
                }
        }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(vc!=null) {
                    vc.onItemLongClick(v, view_holder.getAdapterPosition());
                    Log.d("Clicks", "Sending long click to view holder...");
                }
                return true;
            }
        });
        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.route_name.setText(list_tick.get(position).getName());
        holder.route_grade.setText(list_tick.get(position).getGrade());

        GlideApp.with(mCtx_MR)
                .load(list_tick.get(position).getImage_uri())
                .into(holder.route_img);
    }

    @Override
    public int getItemCount() {
        return list_tick.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRV = recyclerView;
    }
}

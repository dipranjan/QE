package com.qualityengineer.dearc.qe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> contentlist;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> contentlist) {
        this.context = context;
        this.contentlist = contentlist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.name.setText(contentlist.get(position));

    }


    @Override
    public int getItemCount() {
        return contentlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            name = itemView.findViewById(R.id.name);

        }
    }
}
package com.mfusion.mycoordinatorapplicationtest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP PC on 28-10-2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);

        }

    }

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    public RecyclerAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        final String name = mDataset.get(i);


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

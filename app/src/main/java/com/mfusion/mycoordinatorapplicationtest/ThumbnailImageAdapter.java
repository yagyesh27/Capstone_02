package com.mfusion.mycoordinatorapplicationtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by HP PC on 28-10-2016.
 */
public class ThumbnailImageAdapter extends BaseAdapter {

    private Context mContext;

    public ThumbnailImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public Integer[] mThumbIds = {

    };
}

package com.mfusion.mycoordinatorapplicationtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP PC on 21-11-2016.
 */

public class GridImgViewAdap extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    private ArrayList<ArticleSourceImage> artSource = new ArrayList<ArticleSourceImage>();
    Integer[] mThumbnails;

    public GridImgViewAdap(Context context, ArrayList<ArticleSourceImage> artSource) {
        this.artSource = artSource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public ArticleSourceImage getItem(int i) {
        return artSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public GridImgViewAdap(Context c) {
        mContext = c;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View grid;
        ArticleSourceImage source = getItem(i);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            //grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_cell_home,null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imageViewGrid);
            imageView.setAdjustViewBounds(true);

            Picasso.with(mContext).load(source.artImgUrl).into(imageView);


        } else {
            grid = (View) convertView;
        }

        return grid;
    }

        /*public void setArticleSourceImages(Cursor cursor) {
            artSource.addAll(data);
            notifyDataSetChanged();
        }*/

    public void setArticleSourceImages(ArrayList<ArticleSourceImage> data) {
        artSource.addAll(data);
        notifyDataSetChanged();
    }

}

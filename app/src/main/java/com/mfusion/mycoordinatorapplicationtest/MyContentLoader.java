package com.mfusion.mycoordinatorapplicationtest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.mfusion.mycoordinatorapplicationtest.MainActivity;
import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP PC on 18-11-2016.
 */
public class MyContentLoader extends android.support.v4.content.AsyncTaskLoader{
    Context context;
    List list =  new ArrayList();
    public MyContentLoader(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public List<ArticleSourceImage> loadInBackground() {


        Log.d("Inside Loader", "Inside loader ------------------------:::::::###########");


        String URL = "content://com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";

        Uri students = Uri.parse(URL);
        Cursor c = context.getContentResolver().query(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                list.add(new ArticleSourceImage(c.getString(c.getColumnIndex(ArticleSourceImage.COL_SOURCE)),c.getString(c.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL))));
                Log.d("Loader",
                        c.getString(c.getColumnIndex(ArticleSourceImage.COL_SOURCE)) +
                                ", " + c.getString(c.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL)));
            } while (c.moveToNext());
        }


        return list;
    }

    @Override
    public void deliverResult(Object data) {
        super.deliverResult(data);
    }
}

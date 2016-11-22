package com.mfusion.mycoordinatorapplicationtest;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImage;
import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider;

/**
 * Implementation of App Widget functionality.
 */
public class NewsAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        String titles[]={"","","","","",""};
        for (int i = 0; i < N; i++) {

            RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);
            Cursor cursor = context.getContentResolver().query(ArticleSourceImageProvider.URI_ARTICLESOURCES,null,null,null,null);

            int ind = 0;


            while (cursor.moveToNext() ) {

                cursor.getString(cursor.getColumnIndex( ArticleSourceImage.COL_SOURCE));
                titles[ind] = cursor.getString(cursor.getColumnIndex( ArticleSourceImage.COL_ART_TITLE));

                ind++;

            }


            ind = 0;


            try {
                mView.setTextViewText(R.id.textViewW1, titles[0]);
                mView.setContentDescription(R.id.textViewW1, titles[0]);
                mView.setTextViewText(R.id.textViewW2, titles[1]);
                mView.setContentDescription(R.id.textViewW2, titles[1]);
                mView.setTextViewText(R.id.textViewW3, titles[2]);
                mView.setContentDescription(R.id.textViewW3, titles[2]);
                mView.setTextViewText(R.id.textViewW4, titles[3]);
                mView.setContentDescription(R.id.textViewW4, titles[3]);
                mView.setTextViewText(R.id.textViewW5, titles[4]);
                mView.setContentDescription(R.id.textViewW5, titles[4]);
                mView.setTextViewText(R.id.textViewW6, titles[5]);
                mView.setContentDescription(R.id.textViewW6, titles[5]);
            }catch (Exception e){
                Log.e("widget set data", e.toString());
            }



            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


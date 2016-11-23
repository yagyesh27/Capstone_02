package com.mfusion.mycoordinatorapplicationtest.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP PC on 15-11-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler singleton;

    public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "providerExample";

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context.getApplicationContext();
    }

    public synchronized ArticleSourceImage getArticleSourceImage(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(ArticleSourceImage.TABLE_NAME,
                ArticleSourceImage.FIELDS, ArticleSourceImage.COL_SOURCE + " IS ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        ArticleSourceImage item = null;
        if (cursor.moveToFirst()) {
            item = new ArticleSourceImage(cursor);
        }
        cursor.close();

        return item;
    }

    public synchronized int removeArticleSourceImage(final ArticleSourceImage articleSourceImage) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(ArticleSourceImage.TABLE_NAME,
                ArticleSourceImage.COL_SOURCE + " IS ?",
                new String[]{articleSourceImage.source});

        return result;
    }

    public synchronized boolean putArticleSourceImage(final ArticleSourceImage articleSourceImage) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (articleSourceImage.source.length() > 0) {
            result += db.update(ArticleSourceImage.TABLE_NAME, articleSourceImage.getContent(),
                    ArticleSourceImage.COL_SOURCE + " IS ?",
                    new String[] { String.valueOf(articleSourceImage.source) });
        }

        if (result > 0) {
            success = true;
        } else {

            final long id = db.insert(ArticleSourceImage.TABLE_NAME, null,
                    articleSourceImage.getContent());

            if (articleSourceImage.source.length() > 0) {

                success = true;
            }
        }

        return success;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(ArticleSourceImage.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  ArticleSourceImage.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

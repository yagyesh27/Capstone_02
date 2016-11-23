package com.mfusion.mycoordinatorapplicationtest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.sql.SQLException;
import java.util.HashMap;

public class ArticleSourceImageProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";
    public static final String SCHEME = "content://";
    public static final String PROVIDER_NAME = "com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";
    final static int SOURCE = 1;

    public static final String ARTICLESOURCES = SCHEME + AUTHORITY + "/articlesources";
    public static final Uri URI_ARTICLESOURCES = Uri.parse(ARTICLESOURCES);

    public static final String ARTICLESOURCES_BASE = ARTICLESOURCES + "/";

    private static HashMap<String, String> ARTICLESOURCE_PROJECTION_MAP;
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "ArticleSources", SOURCE);

    }

    private SQLiteDatabase db;
    private static DatabaseHandler mOpenHelper;

    public ArticleSourceImageProvider(){

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){

            case SOURCE:
                return "com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImage";


            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }


    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowID = db.insert(	ArticleSourceImage.TABLE_NAME, "", contentValues);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(URI_ARTICLESOURCES, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new android.database.SQLException("Failed to add a record into " + uri);
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new DatabaseHandler(getContext());

        db = mOpenHelper.getWritableDatabase();

        return true;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;



        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ArticleSourceImage.TABLE_NAME);

        switch (uriMatcher.match(uri)) {


            case SOURCE:
                qb.appendWhere( ArticleSourceImage.COL_SOURCE+ "=" + uri.getPathSegments().get(1));
                break;




        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case SOURCE:
                count = db.update(ArticleSourceImage.TABLE_NAME, values, selection, selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

}
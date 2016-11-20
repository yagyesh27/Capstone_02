package com.mfusion.mycoordinatorapplicationtest.data;

import android.content.ContentValues;
import android.database.Cursor;


/**
 * Created by HP PC on 15-11-2016.
 */
public class ArticleSourceImage {




    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "ArticleSources";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed

    // These fields can be anything you want.
    public static final String COL_SOURCE = "source";
    public static final String COL_ART_IMG_URL = "artImgUrl";



    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_SOURCE, COL_ART_IMG_URL };

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("

                    + COL_SOURCE + " TEXT NOT NULL DEFAULT '',"
                    + COL_ART_IMG_URL + " TEXT NOT NULL DEFAULT ''"
                    + ")";

    // Fields corresponding to database columns


    public String source = "";
    public String artImgUrl = "";


    /**
     * No need to do anything, fields are already set to default values above
     */
    public ArticleSourceImage(String source , String artImgUrl) {

        this.source = source;
        this.artImgUrl = artImgUrl;

    }

    /**
     * Convert information from the database into a Person object.
     */
    public ArticleSourceImage(final Cursor cursor) {
        // Indices expected to match order in FIELDS!

        this.source = cursor.getString(0);
        this.artImgUrl = cursor.getString(1);

    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_SOURCE, source);
        values.put(COL_ART_IMG_URL, artImgUrl);


        return values;
    }

}

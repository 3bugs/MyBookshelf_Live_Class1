package com.example.mybookshelf.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mybookshelf.R;
import com.example.mybookshelf.Utils;

import java.io.IOException;

/**
 * Created by Promlert on 9/20/2015.
 */
public class MyHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookshelf.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "books";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_SUB_TITLE = "subtitle";
    public static final String COL_ISBN = "isbn";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_COVER_IMAGE_FILENAME = "cover_image_filename";

    private static final String CREATE_BOOKS_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_TITLE + " TEXT, "
                    + COL_SUB_TITLE + " TEXT, "
                    + COL_ISBN + " TEXT, "
                    + COL_DESCRIPTION + " TEXT, "
                    + COL_COVER_IMAGE_FILENAME + " TEXT)";

    private static final String TAG = "MyHelper";
    private Context mContext;

    public MyHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKS_TABLE);
        insertSampleBookData(db);
    }

    private void insertSampleBookData(SQLiteDatabase db) {
        String[] titleArray = mContext.getResources().getStringArray(R.array.title_array);
        String[] subTitleArray = mContext.getResources().getStringArray(R.array.subtitle_array);
        String[] isbnArray = mContext.getResources().getStringArray(R.array.isbn_array);
        String[] descriptionArray = mContext.getResources().getStringArray(R.array.description_array);
        String[] coverImageFilenameArray = mContext.getResources().getStringArray(R.array.cover_image_filename_array);

        for (int i = 0; i < titleArray.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(COL_TITLE, titleArray[i]);
            cv.put(COL_SUB_TITLE, subTitleArray[i]);
            cv.put(COL_ISBN, isbnArray[i]);
            cv.put(COL_DESCRIPTION, descriptionArray[i]);
            cv.put(COL_COVER_IMAGE_FILENAME, coverImageFilenameArray[i]);

            long result = db.insert(TABLE_NAME, null, cv);
            if (result == -1) {
                Log.e(TAG, "Error inserting initial data into the database.");
            } else {
                Log.i(TAG, "Insert data successfully. Row ID: " + result);
            }

            try {
                Utils.copyFileFromAssetsToImagesDir(mContext, coverImageFilenameArray[i]);
            } catch (IOException e) {
                Log.e(TAG, "Error copying image file from assets to images dir.");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL(CREATE_BOOKS_TABLE);
    }
}

package com.example.mybookshelf.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mybookshelf.R;
import com.example.mybookshelf.Utils;
import com.example.mybookshelf.model.Book;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Promlert on 9/14/2015.
 */
public class BooksDAO {

    private static final String TAG = "BooksDAO";

    private static final String DATABASE_NAME = "bookshelf.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "books";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_SUB_TITLE = "subtitle";
    public static final String COL_ISBN = "isbn";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_COVER_IMAGE_FILENAME = "cover_image_filename";

    private static final String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT, "
            + COL_SUB_TITLE + " TEXT, "
            + COL_ISBN + " TEXT, "
            + COL_DESCRIPTION + " TEXT, "
            + COL_COVER_IMAGE_FILENAME + " TEXT"
            + ")";

    private static DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public BooksDAO(Context context) {
        mContext = context;

        if (mHelper == null) {
            mHelper = new DatabaseHelper(context);
        }
        mDatabase = mHelper.getWritableDatabase();
    }

    public ArrayList<Book> selectAll() {
        ArrayList<Book> books = new ArrayList<Book>();

        String sqlSelect = String.format("SELECT * FROM %s ORDER BY %s", TABLE_NAME, COL_ID);
        Cursor cursor = mDatabase.rawQuery(sqlSelect, null);

        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COL_SUB_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COL_ISBN)),
                    cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(COL_COVER_IMAGE_FILENAME))
            );
            books.add(book);
        }
        return books;
    }

    public boolean insert(String title, String subTitle, String isbn, String description,
                          String coverImageFilename) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_SUB_TITLE, subTitle);
        cv.put(COL_ISBN, isbn);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_COVER_IMAGE_FILENAME, coverImageFilename);

        long result = mDatabase.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Log.e(TAG, "Error inserting new book data into the database.");
            return false;
        } else {
            Log.i(TAG, "Insert data successfully. Row ID: " + result);
            return true;
        }
    }

    public int update(long id, String title, String subTitle, String isbn, String description,
                       String coverImageFilename) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_SUB_TITLE, subTitle);
        cv.put(COL_ISBN, isbn);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_COVER_IMAGE_FILENAME, coverImageFilename);

        return mDatabase.update(TABLE_NAME, cv, COL_ID + "=" + id, null);
    }

    public int delete(long bookId) {
        return mDatabase.delete(TABLE_NAME, COL_ID + "=" + bookId, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
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

        }
    }
}

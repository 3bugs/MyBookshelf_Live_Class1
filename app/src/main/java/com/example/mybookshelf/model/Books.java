package com.example.mybookshelf.model;

import android.content.Context;
import android.util.Log;

import com.example.mybookshelf.R;
import com.example.mybookshelf.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Promlert on 9/19/2015.
 */
public class Books {

    private static final String TAG = "Books";

    private final ArrayList<Book> DATA = new ArrayList<>();
    private Context mContext;
    private static Books mInstance;

    public static Books getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Books(context);
        }
        return mInstance;
    }

    private Books(Context context) {
        this.mContext = context;
        addSampleBookData();
    }

    public ArrayList<Book> getBooks() {
        return DATA;
    }

    public void addBook(Book newBook) {
        DATA.add(newBook);
    }

    private void addSampleBookData() {
        String[] titleArray = mContext.getResources().getStringArray(R.array.title_array);
        String[] subTitleArray = mContext.getResources().getStringArray(R.array.subtitle_array);
        String[] isbnArray = mContext.getResources().getStringArray(R.array.isbn_array);
        String[] descriptionArray = mContext.getResources().getStringArray(R.array.description_array);
        String[] coverImageFilenameArray = mContext.getResources().getStringArray(R.array.cover_image_filename_array);

        try {
            for (int i = 0; i < titleArray.length; i++) {
                Book book = new Book(i, titleArray[i], subTitleArray[i], isbnArray[i], descriptionArray[i], coverImageFilenameArray[i]);
                this.DATA.add(book);

                Utils.copyFileFromAssetsToImagesDir(mContext, coverImageFilenameArray[i]);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error copying image file from assets to images dir.");
            e.printStackTrace();
        }
    }
}

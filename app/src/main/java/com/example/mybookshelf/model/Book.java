package com.example.mybookshelf.model;

/**
 * Created by Promlert on 9/19/2015.
 */
public class Book {
    private int mId;
    private String mTitle;
    private String mSubTitle;
    private String mIsbn;
    private String mDescription;
    private String mCoverImageFilename;

    public Book(int id, String title, String subTitle,
                String isbn, String description, String coverImageFilename) {
        this.mId = id;
        this.mTitle = title;
        this.mSubTitle = subTitle;
        this.mIsbn = isbn;
        this.mDescription = description;
        this.mCoverImageFilename = coverImageFilename;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public String getIsbn() {
        return mIsbn;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCoverImageFilename() {
        return mCoverImageFilename;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}

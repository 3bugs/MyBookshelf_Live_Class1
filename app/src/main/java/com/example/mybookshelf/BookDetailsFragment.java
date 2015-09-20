package com.example.mybookshelf;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.mybookshelf.model.Book;
import com.example.mybookshelf.model.Books;
import com.example.mybookshelf.net.BooksHTTP;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {

    private int mBookIndex;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    // factory method
    public static BookDetailsFragment newInstance(int bookIndex) {
        BookDetailsFragment frag = new BookDetailsFragment();

        Bundle args = new Bundle();
        args.putInt("book_index", bookIndex);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBookIndex = getArguments().getInt("book_index");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Books books = Books.getInstance(getActivity());
        Book b = books.getBooks().get(mBookIndex);

        TextView titleTextView = (TextView) getView().findViewById(R.id.book_title);
        TextView subTitleTextView = (TextView) getView().findViewById(R.id.book_sub_title);
        TextView isbnTextView = (TextView) getView().findViewById(R.id.book_isbn);
        TextView descriptionTextView = (TextView) getView().findViewById(R.id.book_description);
        ImageView coverImageView = (ImageView) getView().findViewById(R.id.book_cover_image);

        titleTextView.setText(b.getTitle());
        subTitleTextView.setText(b.getSubTitle());
        isbnTextView.setText(b.getIsbn());
        descriptionTextView.setText(b.getDescription());

        //Bitmap coverImageBitmap = Utils.getImageBitmap(getActivity(), b.getCoverImageFilename());
        //coverImageView.setImageBitmap(coverImageBitmap);

        String url = BooksHTTP.BASE_URL + "images/" + b.getCoverImageFilename();
        Glide.with(this).load(url).downloadOnly(new Target<File>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {

            }

            @Override
            public void onLoadCleared(Drawable placeholder) {

            }

            @Override
            public void getSize(SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(Request request) {

            }

            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        });

    }

    public void setBookIndex(int bookIndex) {
        this.mBookIndex = bookIndex;
    }

}

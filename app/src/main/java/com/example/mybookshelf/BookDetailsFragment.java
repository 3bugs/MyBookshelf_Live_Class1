package com.example.mybookshelf;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookshelf.model.Book;
import com.example.mybookshelf.model.Books;


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

        Bitmap coverImageBitmap = Utils.getImageBitmap(getActivity(), b.getCoverImageFilename());
        coverImageView.setImageBitmap(coverImageBitmap);
    }

    public void setBookIndex(int bookIndex) {
        this.mBookIndex = bookIndex;
    }

}

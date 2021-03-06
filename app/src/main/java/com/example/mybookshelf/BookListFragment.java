package com.example.mybookshelf;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.mybookshelf.db.MyHelper;
import com.example.mybookshelf.model.AddBookActivity;
import com.example.mybookshelf.model.Book;
import com.example.mybookshelf.model.Books;

/**
 * Created by Promlert on 9/19/2015.
 */
public class BookListFragment extends ListFragment {

    private static final String TAG = "BookListFragment";

    interface BookListListener {
        void itemClicked(int id);
    }

    private BookListListener mListener;

    private ArrayAdapter<Book> mAdapter;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try {
            mListener = (BookListListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()
                    + " must implement BookListListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mListener != null) {
            mListener.itemClicked(position);
        } else {
            Log.d(TAG, "mListener is NULL!!!!!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

/*
        MyHelper helper = new MyHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + MyHelper.TABLE_NAME, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] { MyHelper.COL_TITLE, MyHelper.COL_ISBN },
                new int[] { android.R.id.text1, android.R.id.text2 },
                0
        );
        setListAdapter(adapter);
*/

        Books books = Books.getInstance(getActivity());

        mAdapter = new ArrayAdapter<Book>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                books.getBooks()
        );
        setListAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
/*
            Books books = Books.getInstance(getActivity());
            books.addBook(
                    new Book(
                            0,
                            "ชื่อหนังสือ",
                            "ชื่อ subtitle",
                            "1234567890",
                            "xxxxxxxxxx",
                            ""
                    )
            );
            books.getBooks();
            mAdapter.notifyDataSetChanged();
*/
            Intent intent = new Intent(getActivity(), AddBookActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

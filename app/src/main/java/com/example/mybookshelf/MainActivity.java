package com.example.mybookshelf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mybookshelf.model.Book;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BookListFragment.BookListListener {

    private static final String TAG = "MainActivity";
    //public static final ArrayList<Book> BOOKS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(int id) {
        View fragmentContainer = findViewById(R.id.fragment_container);

        if (fragmentContainer != null) { // layout-large
            BookDetailsFragment frag = BookDetailsFragment.newInstance(id);
            //frag.setBookIndex(id);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragment_container, frag);
            transaction.commit();
        } else { // layout
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.KEY_BOOK_INDEX, id);
            startActivity(intent);
        }
    }
}

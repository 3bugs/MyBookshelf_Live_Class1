package com.example.mybookshelf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mybookshelf.db.MyHelper;
import com.example.mybookshelf.model.Book;
import com.example.mybookshelf.net.BooksHTTP;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BookListFragment.BookListListener {

    private static final String TAG = "MainActivity";
    //public static final ArrayList<Book> BOOKS = new ArrayList<>();

    private MyHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BooksHTTP b = new BooksHTTP(this);
        //b.insertSampleBookData();

/*
        helper = new MyHelper(this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + MyHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MyHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(MyHelper.COL_TITLE));
            String subTitle = cursor.getString(cursor.getColumnIndex(MyHelper.COL_SUB_TITLE));

            String msg = String.format("ID: %d, Title: %s, Subtitle: %s", id, title, subTitle);
            Log.i(TAG, msg);
        }
*/
        testOkHttp();
    }

    private void testOkHttp() {
        final OkHttpClient client = new OkHttpClient();

/*
        RequestBody formBody = new FormEncodingBuilder()
                .add("name", "Promlert")
                .add("age", "40")
                .build();
*/

        final Request request = new Request.Builder()
                .url("http://192.168.56.1/mybookshelf/test.php")
                /*.post(formBody)*/
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String msg = response.body().string();
                Log.i(TAG, msg);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

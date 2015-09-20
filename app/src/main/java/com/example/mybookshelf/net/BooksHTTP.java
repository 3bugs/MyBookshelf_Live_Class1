package com.example.mybookshelf.net;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.mybookshelf.R;
import com.example.mybookshelf.Utils;
import com.example.mybookshelf.model.Book;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Promlert on 9/16/2015.
 */
public class BooksHTTP {

    private static final String TAG = "BooksHTTP";
    public static final String BASE_URL = "http://192.168.56.1/mybookshelf/";
    private static final String SELECT_ALL_URL = BASE_URL + "select_all.php";
    private static final String INSERT_URL = BASE_URL + "insert.php";
    private static final String UPDATE_URL = BASE_URL + "update.php";
    private static final String DELETE_URL = BASE_URL + "delete.php";

    private static final OkHttpClient client = new OkHttpClient();
    private Context mContext;

    public interface OnQueryResponseListener {
        void onSuccess(ArrayList<Book> books);
    }

    public interface OnNonQueryResponseListener {
        void onSuccess(String responseString);
        void onFailure();
    }

    public BooksHTTP(Context context) {
        mContext = context;
    }

    public void selectAll(final OnQueryResponseListener listener) {
        try {
            doRequest(SELECT_ALL_URL, null, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "HTTP request failed!!!");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    final String responseBody = response.body().string();
                    Log.i(TAG, "********************");
                    Log.i(TAG, "Response body: " + responseBody);
                    Log.i(TAG, "********************");

                    final ArrayList<Book> books = parseJson(responseBody);

                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(books);
                                }
                            }
                    );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String title, String subTitle, String isbn, String description,
                       File imageFile, final OnNonQueryResponseListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("subtitle", subTitle);
        params.put("isbn", isbn);
        params.put("description", description);

        try {
            doRequestMultipart(INSERT_URL, params, imageFile, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "HTTP request failed!!!");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    //TODO:

                    final String responseBody = response.body().string();

                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(responseBody);
                                }
                            }
                    );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSampleBookData() {
        String[] titleArray = mContext.getResources().getStringArray(R.array.title_array);
        String[] subTitleArray = mContext.getResources().getStringArray(R.array.subtitle_array);
        String[] isbnArray = mContext.getResources().getStringArray(R.array.isbn_array);
        String[] descriptionArray = mContext.getResources().getStringArray(R.array.description_array);
        String[] coverImageFilenameArray = mContext.getResources().getStringArray(R.array.cover_image_filename_array);

        for (int i = 0; i < titleArray.length; i++) {
            insert(
                    titleArray[i],
                    subTitleArray[i],
                    isbnArray[i],
                    descriptionArray[i],
                    new File(mContext.getFilesDir().getAbsolutePath() + "/images/", coverImageFilenameArray[i]),
                    new OnNonQueryResponseListener() {
                        @Override
                        public void onSuccess(String responseString) {
                            Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {

                        }
                    }
            );
/*
            if (result == -1) {
                Log.e(TAG, "Error inserting initial data into the database.");
            } else {
                Log.i(TAG, "Insert data successfully. Row ID: " + result);
            }
*/
        }

    }

    private ArrayList<Book> parseJson(String jsonString) {
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            int success = json.getInt("success");

            if (success == 1) {
                JSONArray jsonBooks = json.getJSONArray("books");

                for (int i = 0; i < jsonBooks.length(); i++) {
                    JSONObject jsonBook = jsonBooks.getJSONObject(i);

                    Book book = new Book(
                            Integer.valueOf(jsonBook.getString("_id")),
                            jsonBook.getString("title"),
                            jsonBook.getString("subtitle"),
                            jsonBook.getString("isbn"),
                            jsonBook.getString("description"),
                            jsonBook.getString("cover_image_filename")
                    );
                    books.add(book);
                }
            } else if (success == 0) {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void doRequest(String url, Map<String, String> params, Callback callback) throws Exception {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.add(key, value);
            }
        }
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

    public void doRequestMultipart(String url, HashMap<String, String> params, File file, Callback callback) throws Exception {
        //file = new File(mContext.getFilesDir(), "images/code.jpg");

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, value)
                );
            }
        }
        if (file != null) {
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"image.jpg\""),
                    RequestBody.create(MEDIA_TYPE_JPG, file)
            );
        }
        RequestBody formBody = builder.build();

/*
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Android Studio"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title2\""),
                        RequestBody.create(null, "Android Studio2"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title3\""),
                        RequestBody.create(null, "Android Studio3"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"code.jpg\""),
                        RequestBody.create(MEDIA_TYPE_JPG, file))
                .build();
*/

        Request request = new Request.Builder()
                /*.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)*/
                /*.url("http://httpbin.org/post")*/
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}

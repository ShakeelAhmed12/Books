package com.example.shakeel.books;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String newURL;

    private static final int BOOKS_LOADER_ID = 1;

    private EditText editTextSearchQuery;
    private TextView emptyTextView;
    private ImageButton searchBttn;

    private BooksAdapter mAdapter;

    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView booksListView = findViewById(R.id.list);
        emptyTextView = findViewById(R.id.empty_text);

        booksListView.setEmptyView(emptyTextView);

        mAdapter = new BooksAdapter(this, new ArrayList<Books>());

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        booksListView.setAdapter(mAdapter);

        editTextSearchQuery = findViewById(R.id.search_bar);
        searchBttn = findViewById(R.id.search_icon);

        searchBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(editTextSearchQuery.getText().toString());
            }
        });

    }

    private void search(String Query){
        newURL = URL + Query;
        Log.e("MainActivity", newURL);

        if(isConnected){
            getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOKS_LOADER_ID, null, this);
        }else{
            emptyTextView.setText(R.string.no_internet);
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        return new BooksLoader(this, newURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> books) {
        emptyTextView.setText(R.string.no_books);

        mAdapter.clear();
        if(books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}

package com.example.shakeel.books;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Shakeel on 20/03/2018.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String mUrl;

    public BooksLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        List<Books> books = QueryUtil.fetchBooks(mUrl);
        return books;
    }
}

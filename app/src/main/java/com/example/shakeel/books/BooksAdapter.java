package com.example.shakeel.books;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Created by Shakeel on 19/03/2018.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(@NonNull Activity context, ArrayList<Books> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = convertView;

        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Books currentBook = getItem(position);

        TextView bookTitle = itemView.findViewById(R.id.book_title);
        TextView bookAuthor = itemView.findViewById(R.id.book_author);

        bookAuthor.setText(currentBook.getAuthor());
        bookTitle.setText(currentBook.getTitle());

        return itemView;
    }
}

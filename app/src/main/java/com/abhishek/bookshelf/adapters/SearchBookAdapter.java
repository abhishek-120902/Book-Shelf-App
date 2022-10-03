package com.abhishek.bookshelf.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.bookshelf.R;
import com.abhishek.bookshelf.activities.BookInfoActivity;
import com.abhishek.bookshelf.models.BookInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.SearchBookViewHolder> {

    // creating variables for arraylist and context.
    private final ArrayList<BookInfo> bookInfoArrayList;
    private final Context mcontext;

    public SearchBookAdapter(ArrayList<BookInfo> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public SearchBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_books_rv_item, parent, false);
        return new SearchBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookViewHolder holder, int position) {
        BookInfo bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.dateTV.setText(bookInfo.getPublishedDate());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());

        // To set image from URL in our image view.
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        holder.itemView.setOnClickListener(v -> {
            // Calling a new activity and passing all the data of that item in next intent.
            Intent i = new Intent(mcontext, BookInfoActivity.class);
            i.putExtra("book_id", bookInfo.getBookId());
            i.putExtra("title", bookInfo.getTitle());
            i.putExtra("authors", bookInfo.getAuthors());
            i.putExtra("publisher", bookInfo.getPublisher());
            i.putExtra("publishedDate", bookInfo.getPublishedDate());
            i.putExtra("description", bookInfo.getDescription());
            i.putExtra("pageCount", bookInfo.getPageCount());
            i.putExtra("imgUrl", bookInfo.getThumbnail());

            // After passing that data we are starting our new  intent.
            mcontext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();
    }

    public class SearchBookViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public SearchBookViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
        }
    }
}

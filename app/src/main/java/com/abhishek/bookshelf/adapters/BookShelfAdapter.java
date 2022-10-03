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
import com.abhishek.bookshelf.database.BookEntity;
import com.abhishek.bookshelf.database.RoomDB;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookShelfAdapter extends RecyclerView.Adapter<BookShelfAdapter.BookShelfViewHolder> {

    // creating variables for arraylist and context.
    private final Context mcontext;
    private final List<BookEntity> bookEntityList;
    private RoomDB database;

    public BookShelfAdapter(Context mcontext, List<BookEntity> bookEntityList) {
        this.mcontext = mcontext;
        this.bookEntityList = bookEntityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookShelfAdapter.BookShelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_shelf_rv_item, parent, false);
        return new BookShelfAdapter.BookShelfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookShelfAdapter.BookShelfViewHolder holder, int position) {

        //Initialize book entity
        BookEntity entity = bookEntityList.get(position);

        //Initialize database
        database = RoomDB.getInstance(mcontext);

        //Set data in their positions
        holder.nameTV.setText(entity.getBookName());
        holder.publisherTV.setText(entity.getPublisherName());
        Picasso.get().load(entity.getBookCover()).into(holder.bookIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, BookInfoActivity.class);
                i.putExtra("book_id", entity.getBook_id());
                i.putExtra("title", entity.getBookName());
//                i.putExtra("authors", bookEntityList.get(position).getAuthors());
                i.putExtra("publisher", entity.getPublisherName());
                i.putExtra("publishedDate", entity.getPublishedDate());
                i.putExtra("description", entity.getDescription());
//                i.putExtra("pageCount", bookEntityList.get(position).getPageCount());
                i.putExtra("imgUrl", entity.getBookCover());

                // after passing that data we are
                // starting our new  intent.
                mcontext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.bookEntityList.size();
    }

    public class BookShelfViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, publisherTV;
        ImageView bookIV;

        public BookShelfViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.rvBookTitle);
            publisherTV = itemView.findViewById(R.id.rvPublisherName);
            bookIV = itemView.findViewById(R.id.rvBookCover);
        }
    }
}

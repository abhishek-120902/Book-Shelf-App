package com.abhishek.bookshelf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.abhishek.bookshelf.R;
import com.abhishek.bookshelf.database.BookEntity;
import com.abhishek.bookshelf.database.DBAsyncTask;
import com.abhishek.bookshelf.database.RoomDB;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class BookInfoActivity extends AppCompatActivity {

    BookEntity bookEntity;
    //Variables
    private Toolbar toolbar;
    private ImageView bookCover;
    private TextView tvBookName, tvPublisherName, tvPublishedDate, tvNoOfPages, tvDescription;
    private String bookId, thumbnail, bookName, publisherName, publishedDate, description;
    private int noOfPages;
    private AppCompatButton share, addToShelf, removeFromShelf;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        //Initialising variables
        initialise();
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            showInfo();
        }

        //Check if book is in the shelf or not
        DBAsyncTask task = new DBAsyncTask(this, bookEntity, 1);
        try {
            Boolean checkShelf = task.execute().get();
            if (checkShelf) {
                removeFromShelf.setVisibility(View.VISIBLE);
                addToShelf.setVisibility(View.GONE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Add Click Listeners on Buttons
        addToShelf.setOnClickListener(v -> addBookToShelf());
        removeFromShelf.setOnClickListener(v -> removeBookFromShelf());
        share.setOnClickListener(v -> shareBook());

    }

    private void initialise() {
        toolbar = findViewById(R.id.toolbarBookInfo);
        bookCover = findViewById(R.id.bookCover);
        tvBookName = findViewById(R.id.bookName);
        tvPublisherName = findViewById(R.id.publisherName);
        tvPublishedDate = findViewById(R.id.publishedDate);
        tvNoOfPages = findViewById(R.id.bookNoOfPages);
        tvDescription = findViewById(R.id.bookDesc);
        bookId = getIntent().getStringExtra("book_id");
        thumbnail = getIntent().getStringExtra("imgUrl");
        bookName = getIntent().getStringExtra("title");
        publisherName = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        noOfPages = getIntent().getIntExtra("pageCount", 0);
        description = getIntent().getStringExtra("description");
        share = findViewById(R.id.shareBtn);
        addToShelf = findViewById(R.id.addToShelfBtn);
        removeFromShelf = findViewById(R.id.removeFromShelfBtn);
        database = RoomDB.getInstance(this);
        bookEntity = new BookEntity();
        bookEntity.setBook_id(bookId);
        bookEntity.setBookName(bookName);
        bookEntity.setPublisherName(publisherName);
        bookEntity.setPublishedDate(publishedDate);
        bookEntity.setBookCover(thumbnail);
        bookEntity.setDescription(description);
    }

    private void showInfo() {
        Picasso.get().load(thumbnail).into(bookCover);
        tvBookName.setText(bookName);
        tvPublisherName.setText(publisherName);
        tvPublishedDate.setText(publishedDate);
        tvNoOfPages.setText(Integer.toString(noOfPages));
        tvDescription.setText(description);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Swipe to refresh book shelf", Toast.LENGTH_SHORT).show();
    }

    private void removeBookFromShelf() {

        DBAsyncTask async = new DBAsyncTask(BookInfoActivity.this, bookEntity, 3);
        try {
            Boolean result = async.execute().get();
            if (result) {
                Toast.makeText(this, "Book has been removed from the Shelf", Toast.LENGTH_SHORT).show();
                removeFromShelf.setVisibility(View.GONE);
                addToShelf.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Some error occured. Please try again...", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addBookToShelf() {

        DBAsyncTask async = new DBAsyncTask(BookInfoActivity.this, bookEntity, 2);
        try {
            if (async.execute().get()) {
                Toast.makeText(this, "Book has been added to the Shelf", Toast.LENGTH_SHORT).show();
                addToShelf.setVisibility(View.GONE);
                removeFromShelf.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Some error occurred. Please try again...", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void shareBook() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this wonderful book on Google Books. Book Name : \"" + bookName + "\"");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

}
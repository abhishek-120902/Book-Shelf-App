package com.abhishek.bookshelf.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abhishek.bookshelf.R;
import com.abhishek.bookshelf.adapters.BookShelfAdapter;
import com.abhishek.bookshelf.database.BookEntity;
import com.abhishek.bookshelf.database.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BookShelfActivity extends AppCompatActivity {

    //Variables
    private RelativeLayout rlBookShelf;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private RecyclerView rvBookShelf;
    private BookShelfAdapter adapter;
    private GridLayoutManager layoutManager;
    private AppCompatButton yes, no;
    private SwipeRefreshLayout refreshLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<BookEntity> bookList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);

        //Initialising variables
        initialise();
        setSupportActionBar(toolbar);

        loadShelf();

        rvBookShelf.setLayoutManager(layoutManager);
        rvBookShelf.setAdapter(adapter);

        fab.setOnClickListener(v -> goToSearchBook());
        refreshLayout.setOnRefreshListener(() -> {
            BookShelfActivity.this.recreate();
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        });

    }

    private void goToSearchBook() {
        startActivity(new Intent(BookShelfActivity.this, SearchBookActivity.class));
    }

    private void initialise() {
        rlBookShelf = findViewById(R.id.rlBookShelf);
        toolbar = findViewById(R.id.toolbarBookShelf);
        mAuth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.addBookFAB);
        rvBookShelf = findViewById(R.id.rvBookShelf);
        layoutManager = new GridLayoutManager(this, 2);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void loadShelf() {
        RoomDB database = RoomDB.getInstance(this);
        bookList = database.bookDao().getAll();
        adapter = new BookShelfAdapter(BookShelfActivity.this, bookList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.log_out_alert_dialog, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        yes = view.findViewById(R.id.yesLogout);
        no = view.findViewById(R.id.noLogout);

        yes.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(BookShelfActivity.this, "You have logged out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BookShelfActivity.this, LoginActivity.class));
            BookShelfActivity.this.finish();
            preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            editor = preferences.edit().putBoolean("remember", false);
            editor.apply();
        });

        no.setOnClickListener(v -> alertDialog.cancel());

    }
}
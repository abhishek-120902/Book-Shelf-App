package com.abhishek.bookshelf.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.bookshelf.R;
import com.abhishek.bookshelf.adapters.SearchBookAdapter;
import com.abhishek.bookshelf.models.BookInfo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchBookActivity extends AppCompatActivity {

    //Variables
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView rvSearchBook;
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        //Initialising variables
        initialise();
        setSupportActionBar(toolbar);

        searchView.clearFocus();    //To remove cursor from search view in some lower API's level Android devices
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getBookInfo(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void initialise() {
        toolbar = findViewById(R.id.toolbarSearchBook);
        searchView = findViewById(R.id.searchView);
        rvSearchBook = findViewById(R.id.rvSearchView);
        shimmerFrameLayout = findViewById(R.id.shimmerSearchBook);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Swipe again to refresh book shelf", Toast.LENGTH_SHORT).show();
    }

    private void getBookInfo(String query) {
        // creating a new array list.
        bookInfoArrayList = new ArrayList<>();
        rvSearchBook.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        // To initialize the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(SearchBookActivity.this);

        // To clear cache this will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        // below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(SearchBookActivity.this);


        // To make json object request inside that we are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            shimmerFrameLayout.stopShimmer();
            rvSearchBook.setVisibility(View.VISIBLE);
            // Extraction of json data.
            try {
                JSONArray itemsArray = response.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                    String id = itemsObj.optString("id");
                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                    String title = volumeObj.optString("title");
                    JSONArray authorsArray = volumeObj.getJSONArray("authors");
                    String publisher = volumeObj.optString("publisher");
                    String publishedDate = volumeObj.optString("publishedDate");
                    String description = volumeObj.optString("description");
                    int pageCount = volumeObj.optInt("pageCount");
                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    String thumbnail = imageLinks.optString("thumbnail");
                    ArrayList<String> authorsArrayList = new ArrayList<>();
                    if (authorsArray.length() != 0) {
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authorsArrayList.add(authorsArray.optString(i));
                        }
                    }
                    // save this data in modal class.
                    BookInfo bookInfo = new BookInfo(id, title, publisher, thumbnail, publishedDate, description, pageCount, authorsArrayList);

                    // To pass our modal class in array list.
                    bookInfoArrayList.add(bookInfo);

                    // To pass our array list in adapter class.
                    SearchBookAdapter adapter = new SearchBookAdapter(bookInfoArrayList, SearchBookActivity.this);

                    // To add linear layout manager for recycler view.
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchBookActivity.this, RecyclerView.VERTICAL, false);
                    RecyclerView mRecyclerView = findViewById(R.id.rvSearchView);

                    // Setting layout manager and adapter to recycler view.
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(adapter);
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // displaying a toast message when we get any error from API
                Toast.makeText(SearchBookActivity.this, "No Data Found" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // also displaying error message in toast.
            Toast.makeText(SearchBookActivity.this, "Error found is " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        // Adding this json object request in our request queue.
        queue.add(booksObjrequest);

    }

}
package com.abhishek.bookshelf.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    //Insert Query
    @Insert(onConflict = REPLACE)
    void insert(BookEntity bookEntity);

    //Delete Query
    @Delete
    void delete(BookEntity bookEntity);

    //Get all books query
    @Query("SELECT * FROM books")
    List<BookEntity> getAll();

    //Get book by id
    @Query("SELECT * FROM books WHERE book_id = :id")
    BookEntity getBookById(String id);

}

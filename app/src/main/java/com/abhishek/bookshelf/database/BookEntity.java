package com.abhishek.bookshelf.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Define table name
@Entity(tableName = "books")
public class BookEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private String book_id;
    @ColumnInfo(name = "book_name")
    private String bookName;
    @ColumnInfo(name = "publisher_name")
    private String publisherName;
    @ColumnInfo(name = "published_date")
    private String publishedDate;
    @ColumnInfo(name = "book_cover")
    private String bookCover;
    @ColumnInfo(name = "description")
    private String description;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.abhishek.bookshelf.models;

import java.util.ArrayList;

public class BookInfo {

    // creating string, int and array list
    // variables for our book details
    private String bookId;
    private String title;
    private String publisher;
    private String thumbnail;
    private String publishedDate;
    private String description;
    private int pageCount;
    private ArrayList<String> authors;

    public BookInfo(String bookId, String title, String publisher, String bookCover, String publishedDate, String description, int pageCount, ArrayList<String> authors) {
        this.bookId = bookId;
        this.title = title;
        this.publisher = publisher;
        this.thumbnail = bookCover;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.authors = authors;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String bookCover) {
        this.thumbnail = bookCover;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

}

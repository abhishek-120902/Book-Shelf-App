package com.abhishek.bookshelf.database;

import android.content.Context;
import android.os.AsyncTask;

public class DBAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final BookEntity bookEntity;
    private final int mode;
    private Context context;
    private final RoomDB db = RoomDB.getInstance(context);

    public DBAsyncTask(Context context, BookEntity bookEntity, int mode) {
        this.context = context;
        this.bookEntity = bookEntity;
        this.mode = mode;
    }

    /*
            Mode 1 : Check DB if book is already there in shelf or not
            Mode 2 : Add the book in Shelf
            Mode 3 : Remove the book from Shelf
            */

    @Override
    protected Boolean doInBackground(Void... voids) {

        switch (mode) {
            case 1: {
//                        Check DB if book is already there in shelf or not
                BookEntity entity = db.bookDao().getBookById(bookEntity.getBook_id());
//                        db.close();
                return entity != null;

            }
            case 2: {
//                        Add the book in Shelf
                db.bookDao().insert(bookEntity);
//                        db.close();
                return true;
            }
            case 3: {
//                        Remove the book from Shelf
                db.bookDao().delete(bookEntity);
//                        db.close();
                return true;
            }
        }

        return false;
    }
}


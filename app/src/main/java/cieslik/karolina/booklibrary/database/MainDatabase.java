package cieslik.karolina.booklibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cieslik.karolina.booklibrary.ui.Book;

public class MainDatabase
{
    public static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase database;
    private Context context;
    private DatabaseHelper databaseHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(BookLiterals.DB_CREATE_BOOKS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(BookLiterals.DROP_BOOKS_TABLE);
            onCreate(db);
        }
    }

    public MainDatabase(Context context)
    {
        this.context = context;
    }

    public MainDatabase open()
    {
        databaseHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try
        {
            database = databaseHelper.getWritableDatabase();
        } catch (SQLException e)
        {
            database = databaseHelper.getReadableDatabase();
        }
        return this;
    }

    public void close()
    {
        databaseHelper.close();
    }

    public long insertBook(Book aBook)
    {
        ContentValues newBook = new ContentValues();
        newBook.put(BookLiterals.BOOK_ISBN, aBook.getIsbn());
        newBook.put(BookLiterals.BOOK_TITLE, aBook.getTitle());
        newBook.put(BookLiterals.BOOK_AUTHOR, aBook.getAuthor());
        newBook.put(BookLiterals.BOOK_PUBLISHER, aBook.getPublisher());
        newBook.put(BookLiterals.BOOK_PUBLISHING_YEAR, aBook.getPublishingYear());
        newBook.put(BookLiterals.BOOK_NOTES, aBook.getNotes());
        newBook.put(BookLiterals.BOOK_COVER, aBook.getCover());
        newBook.put(BookLiterals.BOOK_RATE, aBook.getRate());
        return database.insert(BookLiterals.BOOK_TABLE_NAME, null, newBook);
    }

    public long insertBook(String isbn, String title, String author, String publisher, String publishingYear, String notes, String
            cover, int rate)
    {
        ContentValues newBook = new ContentValues();
        newBook.put(BookLiterals.BOOK_ISBN, isbn);
        newBook.put(BookLiterals.BOOK_TITLE, title);
        newBook.put(BookLiterals.BOOK_AUTHOR, author);
        newBook.put(BookLiterals.BOOK_PUBLISHER, publisher);
        newBook.put(BookLiterals.BOOK_PUBLISHING_YEAR, publishingYear);
        newBook.put(BookLiterals.BOOK_NOTES, notes);
        newBook.put(BookLiterals.BOOK_COVER, cover);
        newBook.put(BookLiterals.BOOK_RATE, rate);
        return database.insert(BookLiterals.BOOK_TABLE_NAME, null, newBook);
    }

    public boolean updateBook(Book aBook)
    {
        String isbn = aBook.getIsbn();
        String title = aBook.getTitle();
        String author = aBook.getAuthor();
        String publisher = aBook.getPublisher();
        String publishingYear = aBook.getPublishingYear();
        String notes = aBook.getNotes();
        String cover = aBook.getCover();
        int rate = aBook.getRate();

        return updateBook(isbn, title, author, publisher, publishingYear, notes, cover, rate);
    }


    public boolean updateBook(String isbn, String title, String author, String publisher, String publishingYear, String notes,
                              String cover, int rate)
    {
        String where = BookLiterals.BOOK_ISBN + "=" + isbn;

        ContentValues updateBook = new ContentValues();
        updateBook.put(BookLiterals.BOOK_ISBN, isbn);
        updateBook.put(BookLiterals.BOOK_TITLE, title);
        updateBook.put(BookLiterals.BOOK_AUTHOR, author);
        updateBook.put(BookLiterals.BOOK_PUBLISHER, publisher);
        updateBook.put(BookLiterals.BOOK_PUBLISHING_YEAR, publishingYear);
        updateBook.put(BookLiterals.BOOK_NOTES, notes);
        updateBook.put(BookLiterals.BOOK_COVER, cover);
        updateBook.put(BookLiterals.BOOK_RATE, rate);
        return database.update(BookLiterals.BOOK_TABLE_NAME, updateBook, where, null) > 0;
    }

    public boolean deleteBook(String isbn)
    {
        String where = BookLiterals.BOOK_ISBN + "=" + isbn;
        return database.delete(BookLiterals.BOOK_TABLE_NAME, where, null) > 0;
    }

    public ArrayList<Book> getAllBooks()
    {
        String[] columns = {BookLiterals.BOOK_ID,
                BookLiterals.BOOK_ISBN,
                BookLiterals.BOOK_TITLE,
                BookLiterals.BOOK_AUTHOR,
                BookLiterals.BOOK_PUBLISHER,
                BookLiterals.BOOK_PUBLISHING_YEAR,
                BookLiterals.BOOK_NOTES,
                BookLiterals.BOOK_COVER,
                BookLiterals.BOOK_RATE};

        Cursor cursor = database.query(BookLiterals.BOOK_TABLE_NAME, columns, null, null, null, null, null);

        Book book;
        ArrayList<Book> books = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(BookLiterals.BOOK_ID_COLUMN);
                String isbn = cursor.getString(BookLiterals.BOOK_ISBN_COLUMN);
                String title = cursor.getString(BookLiterals.BOOK_TITLE_COLUMN);
                String author = cursor.getString(BookLiterals.BOOK_AUTHOR_COLUMN);
                String publisher = cursor.getString(BookLiterals.BOOK_PUBLISHER_COLUMN);
                String publishingYear = cursor.getString(BookLiterals.BOOK_PUBLISHING_YEAR_COLUMN);
                String notes = cursor.getString(BookLiterals.BOOK_NOTES_COLUMN);
                String cover = cursor.getString(BookLiterals.BOOK_COVER_COLUMN);
                int rate = cursor.getInt(BookLiterals.BOOK_RATE_COLUMN);
                book = new Book(id, isbn, title, author, publisher, publishingYear, notes, cover, rate);
                books.add(book);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return books;
    }

    public Book getBook(String aIsbn)
    {
        String[] columns = {BookLiterals.BOOK_ID,
                BookLiterals.BOOK_ISBN,
                BookLiterals.BOOK_TITLE,
                BookLiterals.BOOK_AUTHOR,
                BookLiterals.BOOK_PUBLISHER,
                BookLiterals.BOOK_PUBLISHING_YEAR,
                BookLiterals.BOOK_NOTES,
                BookLiterals.BOOK_COVER,
                BookLiterals.BOOK_RATE};

        String where = BookLiterals.BOOK_ISBN + "=" + aIsbn;
        Cursor cursor = database.query(BookLiterals.BOOK_TABLE_NAME, columns, where, null, null, null, null);
        Book book = null;
        if (cursor != null && cursor.moveToFirst())
        {
            String id = cursor.getString(BookLiterals.BOOK_ID_COLUMN);
            String isbn = cursor.getString(BookLiterals.BOOK_ISBN_COLUMN);
            String title = cursor.getString(BookLiterals.BOOK_TITLE_COLUMN);
            String author = cursor.getString(BookLiterals.BOOK_AUTHOR_COLUMN);
            String publisher = cursor.getString(BookLiterals.BOOK_PUBLISHER_COLUMN);
            String publishingYear = cursor.getString(BookLiterals.BOOK_PUBLISHING_YEAR_COLUMN);
            String notes = cursor.getString(BookLiterals.BOOK_NOTES_COLUMN);
            String cover = cursor.getString(BookLiterals.BOOK_COVER_COLUMN);
            int rate = cursor.getInt(BookLiterals.BOOK_RATE_COLUMN);
            book = new Book(id, isbn, title, author, publisher, publishingYear, notes, cover, rate);
            cursor.close();
        }
        return book;
    }

    public Book selectBook(String isbn)
    {
        Book book = null;
        String selectQuery = "SELECT * FROM "
                + BookLiterals.BOOK_TABLE_NAME + " WHERE "
                + BookLiterals.BOOK_ISBN + " = " + "'" + isbn + "'";
        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst())
        {
            String id = c.getString(BookLiterals.BOOK_ISBN_COLUMN);
            book = getBook(id);
        }
        c.close();

        return book;
    }
}

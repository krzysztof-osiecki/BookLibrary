package cieslik.karolina.booklibrary.database;


public class BookLiterals
{
    public static final String BOOK_TABLE_NAME = "Books";
    public static final String BOOK_ID = "id";
    public static final String BOOK_ISBN = "isbn";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_PUBLISHER = "publisher";
    public static final String BOOK_PUBLISHING_YEAR = "publishing_year";
    public static final String BOOK_NOTES = "notes";
    public static final String BOOK_COVER = "cover";
    public static final String BOOK_RATE = "rate";

    public static final int BOOK_ID_COLUMN = 0;
    public static final int BOOK_ISBN_COLUMN = 1;
    public static final int BOOK_TITLE_COLUMN = 2;
    public static final int BOOK_AUTHOR_COLUMN = 3;
    public static final int BOOK_PUBLISHER_COLUMN = 4;
    public static final int BOOK_PUBLISHING_YEAR_COLUMN = 5;
    public static final int BOOK_NOTES_COLUMN = 6;
    public static final int BOOK_COVER_COLUMN = 7;
    public static final int BOOK_RATE_COLUMN = 8;

    public static final String DB_CREATE_BOOKS_TABLE = "CREATE TABLE "
            + BookLiterals.BOOK_TABLE_NAME + "( "
            + BookLiterals.BOOK_ID + " " + "TEXT" + ", "
            + BookLiterals.BOOK_ISBN + " " + "TEXT" + ", "
            + BookLiterals.BOOK_TITLE + " " + "TEXT" + ","
            + BookLiterals.BOOK_AUTHOR + " " + "TEXT" + ","
            + BookLiterals.BOOK_PUBLISHER + " " + "TEXT" + ","
            + BookLiterals.BOOK_PUBLISHING_YEAR + " " + "TEXT" + ","
            + BookLiterals.BOOK_NOTES + " " + "TEXT" + ","
            + BookLiterals.BOOK_COVER + " " + "TEXT" + ","
            + BookLiterals.BOOK_RATE + " " + "INTEGER" + ");";

    public static final String DROP_BOOKS_TABLE = "DROP TABLE IF EXISTS "
            + BookLiterals.BOOK_TABLE_NAME;
}

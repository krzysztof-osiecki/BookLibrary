package cieslik.karolina.booklibrary.ui;

public class Book
{
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishingYear;
    private String notes;
    private String cover;
    private int rate;

    public Book(String isbn)
    {
        this.isbn = isbn;
    }

    public Book(String id, String isbn, String title, String author, String publisher, String publishingYear, String notes, String
            cover, int rate)
    {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.notes = notes;
        this.cover = cover;
        this.rate = rate;
    }

    public Book(String isbn, String title, String author, String publisher, String publishingYear)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String getPublishingYear()
    {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear)
    {
        this.publishingYear = publishingYear;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public int getRate()
    {
        return rate;
    }

    public void setRate(int rate)
    {
        this.rate = rate;
    }
}

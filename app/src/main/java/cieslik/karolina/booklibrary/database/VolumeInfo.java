
package cieslik.karolina.booklibrary.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VolumeInfo
{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("authors")
    @Expose
    private List<String> authors = new ArrayList<String>();
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("publishedDate")
    @Expose
    private String publishedDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("imageLinks")
    @Expose
    private ImageLinks imageLinks;

    /**
     * @return The title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return The authors
     */
    public List<String> getAuthors()
    {
        return authors;
    }

    /**
     * @param authors The authors
     */
    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }

    /**
     * @return The publisher
     */
    public String getPublisher()
    {
        return publisher;
    }

    /**
     * @param publisher The publisher
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    /**
     * @return The publishedDate
     */
    public String getPublishedDate()
    {
        return publishedDate;
    }

    /**
     * @param publishedDate The publishedDate
     */
    public void setPublishedDate(String publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    /**
     * @return The description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return The pageCount
     */
    public Integer getPageCount()
    {
        return pageCount;
    }

    /**
     * @param pageCount The pageCount
     */
    public void setPageCount(Integer pageCount)
    {
        this.pageCount = pageCount;
    }

    /**
     * @return The imageLinks
     */
    public ImageLinks getImageLinks()
    {
        return imageLinks;
    }

    /**
     * @param imageLinks The imageLinks
     */
    public void setImageLinks(ImageLinks imageLinks)
    {
        this.imageLinks = imageLinks;
    }

}

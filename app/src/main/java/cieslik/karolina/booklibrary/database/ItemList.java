package cieslik.karolina.booklibrary.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karolina on 10.11.2016.
 */

public class ItemList implements Serializable
{
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();

    /**
     * @return The items
     */
    public List<Item> getItems()
    {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<Item> items)
    {
        this.items = items;
    }
}

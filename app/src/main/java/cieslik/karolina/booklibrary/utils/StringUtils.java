package cieslik.karolina.booklibrary.utils;

/**
 * Created by Karolina on 05.11.2016.
 */

public class StringUtils
{

    public static boolean isNotNullOrEmpty(String text)
    {
        return text != null && !text.trim().isEmpty();
    }
}

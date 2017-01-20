package cieslik.karolina.booklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Karolina on 05.11.2016.
 */

public class DeviceUtils
{
    public static int dpToPx(Activity activity, int dp)
    {
        Resources r = activity.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void hideInputKeyboard(View view, Activity activity)
    {
        if (view != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

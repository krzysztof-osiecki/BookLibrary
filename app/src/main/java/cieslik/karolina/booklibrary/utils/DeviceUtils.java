package cieslik.karolina.booklibrary.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Karolina on 05.11.2016.
 */

public class DeviceUtils
{
    public static int dpToPx(Activity activity, int dp) {
        Resources r = activity.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

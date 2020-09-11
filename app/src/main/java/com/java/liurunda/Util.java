package com.java.liurunda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

public class Util {
    public static void showSnackbar(Activity activity, final String message) {
        Snackbar snack = Snackbar.make(activity.findViewById(R.id.layout), message, Snackbar.LENGTH_SHORT);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams();
        params.setAnchorId(R.id.bottom_nav);
        params.anchorGravity = Gravity.TOP;
        params.gravity = Gravity.TOP;
        snack.getView().setLayoutParams(params);
        snack.show();
    }

    //public static final int CATEGORY_ALL = 0;
    public static final int CATEGORY_EVENTS = 0;
    public static final int CATEGORY_POINTS = 1;
    public static final int CATEGORY_NEWS = 2;
    public static final int CATEGORY_PAPERS = 3;
//(1 << CATEGORY_ALL) |
    public static final int CATEGORY_FULL =  (1 << CATEGORY_EVENTS) | (1 << CATEGORY_POINTS) | (1 << CATEGORY_NEWS) | (1 << CATEGORY_PAPERS);

    public static final int[] CATEGORY_FLAGS = {CATEGORY_EVENTS, CATEGORY_POINTS, CATEGORY_NEWS, CATEGORY_PAPERS};

    public static int loadCategorySettings(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("categories", Context.MODE_PRIVATE);
        return preferences.getInt("category", CATEGORY_FULL);
    }

    public static boolean writeCategorySettings(Activity activity, int categories) {
        if (categories == 0) {
            return false; // refuse settings if no category is selected
        } else {
            SharedPreferences preferences = activity.getSharedPreferences("categories", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("category", categories);
            editor.commit();
            return true;
        }
    }
}

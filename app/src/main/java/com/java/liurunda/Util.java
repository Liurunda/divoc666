package com.java.liurunda;

import android.app.Activity;
import android.content.Context;
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
}

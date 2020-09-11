package com.java.liurunda;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationUtil {
    public static final int fadeAnimationDuration = 400;

    public static void fadeIn(View view) {
        if (view.getVisibility() == View.VISIBLE) return;
        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(fadeAnimationDuration);
        view.startAnimation(animation);
        view.setEnabled(true);
    }

    public static void fadeOut(View view) {
        if (view.getVisibility() != View.VISIBLE) return;
        view.setEnabled(false);
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(fadeAnimationDuration);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }
}

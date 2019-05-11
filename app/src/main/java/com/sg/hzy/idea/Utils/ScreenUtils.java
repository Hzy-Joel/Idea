package com.sg.hzy.idea.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by 胡泽宇 on 2018/11/8.
 */

public class ScreenUtils {
    public static int WIDTH;
    public static int HEIGHT;
    public static DisplayMetrics getScreenSize(Context context) {

        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        return dm;
        //获取方式
        // int width = outMetrics.widthPixels;
        //int height = outMetrics.heightPixels;
    }
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    public static void setScreen(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WIDTH = wm.getDefaultDisplay().getWidth();
        HEIGHT = wm.getDefaultDisplay().getHeight();
    }
  }

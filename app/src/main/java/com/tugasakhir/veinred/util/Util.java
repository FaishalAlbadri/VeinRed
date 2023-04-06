package com.tugasakhir.veinred.util;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Util {

    public static void refreshColor(SwipeRefreshLayout refresh){
        refresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


}

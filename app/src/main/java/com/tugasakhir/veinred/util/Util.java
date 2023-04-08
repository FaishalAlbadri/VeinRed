package com.tugasakhir.veinred.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Util {

    public static void refreshColor(SwipeRefreshLayout refresh){
        refresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if(html == null){
            // return an empty spannable if the html is null
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(int resource, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(String.format(context.getResources().getString(resource)), Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(String.format(context.getResources().getString(resource)));
        }
    }
}

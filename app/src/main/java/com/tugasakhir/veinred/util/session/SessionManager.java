package com.tugasakhir.veinred.util.session;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.tugasakhir.veinred.ui.HomeActivity;
import com.tugasakhir.veinred.ui.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    public static final String key_id_user = "key_id_user";
    public static final String key_user_email = "key_user_email";
    public static final String key_user_password = "key_user_password";
    private static final String is_login = "islogin";
    private static final String pref_id_user = "pref_id_user";
    private static final String pref_user_email = "pref_user_email";
    private static final String pref_user_password = "pref_user_password";

    private Context context;
    private int mode;
    private SharedPreferences id_userPref, user_emailPref, user_passwordPref;
    private SharedPreferences.Editor id_userEditor, user_emailEditor, user_passwordEditor;

    public SessionManager(Context context) {
        mode = 0;
        this.context = context;
        id_userPref = context.getSharedPreferences(pref_id_user, mode);
        user_emailPref = context.getSharedPreferences(pref_user_email, mode);
        user_passwordPref = context.getSharedPreferences(pref_user_password, mode);

        id_userEditor = id_userPref.edit();
        user_emailEditor = user_emailPref.edit();
        user_passwordEditor = user_passwordPref.edit();
    }

    public void createUser(String id_user, String user_email, String user_password) {
        id_userEditor.putBoolean(is_login, true);
        id_userEditor.putString(key_id_user, id_user);
        user_emailEditor.putString(key_user_email, user_email);
        user_passwordEditor.putString(key_user_password, user_password);

        id_userEditor.commit();
        user_emailEditor.commit();
        user_passwordEditor.commit();
    }

    public void checkLogin() {
        if (!this.is_login()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
        } else {
            Intent i = new Intent(context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    public HashMap<String, String> getUser() {
        HashMap<String, String> user = new HashMap<>();
        user.put(key_id_user, id_userPref.getString(key_id_user, null));
        user.put(key_user_email, user_emailPref.getString(key_user_email, null));
        user.put(key_user_password, user_passwordPref.getString(key_user_password, null));
        return user;
    }

    private boolean is_login() {
        return id_userPref.getBoolean(is_login, false);
    }

    public void logout() {
        this.clear();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    public void clear() {
        id_userEditor.clear();
        user_emailEditor.clear();
        user_passwordEditor.clear();

        id_userEditor.commit();
        user_emailEditor.commit();
        user_passwordEditor.commit();
    }
}


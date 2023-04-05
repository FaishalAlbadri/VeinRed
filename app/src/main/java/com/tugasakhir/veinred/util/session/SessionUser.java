package com.tugasakhir.veinred.util.session;

import android.content.Context;

import java.util.HashMap;

public class SessionUser {

    private static SessionManager sessionManager;
    private static HashMap<String, String> hashMap;
    private static SessionUser mInstance = null;

    public static SessionUser getInstance(Context context) {
        mInstance = new SessionUser();
        sessionManager = new SessionManager(context);
        hashMap = sessionManager.getUser();

        return mInstance;
    }

    public void setDataUser() {
        User.getInstance().setId_user(hashMap.get(SessionManager.key_id_user));
        User.getInstance().setUser_email(hashMap.get(SessionManager.key_user_email));
        User.getInstance().setUser_password(hashMap.get(SessionManager.key_user_password));

    }

    public void logout() {
        sessionManager.logout();
    }
}

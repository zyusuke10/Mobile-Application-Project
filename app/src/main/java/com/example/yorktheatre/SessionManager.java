package com.example.yorktheatre;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SESSION_ACTIVE = "session_active";
    private int user_id;


    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void startSession(int user_id) {
        editor.putInt(KEY_USER_ID, user_id);
        editor.putBoolean(KEY_SESSION_ACTIVE, true);
        editor.apply();
    }

    public void endSession() {
        editor.remove(KEY_USER_ID);
        editor.putBoolean(KEY_SESSION_ACTIVE, false);
        editor.apply();
    }

    public boolean isSessionActive() {
        return sharedPreferences.getBoolean(KEY_SESSION_ACTIVE, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }
}

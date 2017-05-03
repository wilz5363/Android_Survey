package com.example.chanw.surveypsm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by chanw on 3/26/2017.
 */

public class SessionManager {

    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SurveyPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Context context){
        this._context = context;
        mPref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public void createLoginSession(String email){
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(intent);
        }
    }

    public boolean isLoggedIn(){
        return mPref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public String getUserEmail() {
        return mPref.getString(KEY_EMAIL, "");
    }
}

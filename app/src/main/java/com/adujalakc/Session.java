package com.adujalakc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marsuc on 11/06/2017.
 */

public class Session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public Session(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        /*
        *you can make modifications to the data in the preferences
        * and atomically commit those changes back to the SharedPreferences object.
         */
        editor = preferences.edit();

    }


    public void setLoggedIn(boolean loggedin, String username, String id){
        editor.putBoolean("loggedInmode",loggedin);
        editor.putString("username",username);
        editor.putString("id",id);
        editor.commit();
    }

    //return status log in user untuk dicek di LoginActivity
    public boolean loggedin(){
        return preferences.getBoolean("loggedInmode", false);
    }

    public String getId(){
        return preferences.getString("id","NULL");
    }

    public String getUsername(){
        return preferences.getString("username","DEFAULT");
    }

}

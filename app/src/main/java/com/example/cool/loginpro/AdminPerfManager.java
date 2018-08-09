package com.example.cool.loginpro;

import android.content.Context;
import android.content.SharedPreferences;

class AdminPerfManager {
    Context context;
    AdminPerfManager(Context context) {
        this.context=context;
    }

    public void saveLoginDetails(String email,String password)
    {
        SharedPreferences preferences=context.getSharedPreferences("AdminLoginDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("AdminEmail",email);
        editor.putString("AdminPassword",password);
        editor.apply();
    }
    public String getEmail()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("AdminLoginDetails",Context.MODE_PRIVATE);
        return sharedPreferences.getString("AdminEmail","");
    }
    public boolean isUserLogOut()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("AdminLoginDetails",Context.MODE_PRIVATE);
        boolean isEmailEmpty =sharedPreferences.getString("AdminEmail","").isEmpty();
        boolean isPasswordEmpty =sharedPreferences.getString("AdminPassword","").isEmpty();
        return isEmailEmpty||isPasswordEmpty;
    }
    public void clear()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("AdminLoginDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove("AdminEmail");
        editor.remove("AdminPassword");
        editor.apply();
    }
}
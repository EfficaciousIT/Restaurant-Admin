package com.efficacious.e_smartsrestaurant.Base;

import android.content.Context;
import android.content.SharedPreferences;

import com.efficacious.e_smartsrestaurant.Model.LoginDetail;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.List;

public class SharedPrefManger {
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManger(Context context) {
        this.context = context;
    }

    public void saveUser(List<LoginDetail> user){
        sharedPreferences = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        editor = sharedPreferences.edit();
        editor.putInt(WebConstant.RESTAURANT_ID,user.get(0).getResId());
        editor.putInt(WebConstant.USER_TYPE_ID,user.get(0).getUserTypeId());
        editor.putInt(WebConstant.EMPLOYEE_ID,user.get(0).getEmployeeId());
        editor.putString(WebConstant.FIRST_NAME,user.get(0).getFirstName());
        editor.putString(WebConstant.LAST_NAME,user.get(0).getLastName());
        editor.putString(WebConstant.USER_TYPE,user.get(0).getUserType());
        editor.putString(WebConstant.USER_NAME,user.get(0).getUserName());
        editor.putString(WebConstant.PASSWORD,user.get(0).getPassword());
        editor.putBoolean(WebConstant.LOGGED,true);
        editor.apply();
    }

    public boolean isLoggedIn(){
        sharedPreferences = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        return sharedPreferences.getBoolean(WebConstant.LOGGED,false);
    }

    public void logout(){
        sharedPreferences = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}

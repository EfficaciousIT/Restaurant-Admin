package com.efficacious.e_smartsrestaurant.Notification;

import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static Retrofit retrofit = null;
    public static Retrofit getRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(WebConstant.FCM_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

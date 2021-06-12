package com.example.cornonalivetrackerapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClass {

    public static Retrofit retrofit=null;

    public static ApiInterface getRetrofitInstance(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ApiInterface.class);
    }

}

package com.example.cornonalivetrackerapp.network;

import com.example.cornonalivetrackerapp.model.DataClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    public String BASE_URL ="https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<DataClass>> getCountryData();
}

package com.example.ftrani.apiclimademo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ftrani on 19/10/17.
 */

public interface WeatherService {

    @GET("weather")
    Call<City> getCity(@Query("q") String city, @Query("appid") String key);

    @GET("weather")
    Call<City> getCity(@Query("id") int idCity, @Query("appid") String key);

    @GET("weather")
    Call<City> getCity(@Query("id") int idCity, @Query("appid") String key, @Query("units") String value);

    @GET("weather")
    Call<City> getCity(@Query("id") int idCity, @Query("appid") String key, @Query("units") String value, @Query("lang") String lang);

}

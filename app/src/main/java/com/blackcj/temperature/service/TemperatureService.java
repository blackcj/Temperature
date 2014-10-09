package com.blackcj.temperature.service;

import com.blackcj.temperature.model.Temperature;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Define the API endpoint for Retrofit
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
public interface TemperatureService {
    @GET("/spark/getTemp.php")
    void getTemp(Callback<Temperature> callback);
}

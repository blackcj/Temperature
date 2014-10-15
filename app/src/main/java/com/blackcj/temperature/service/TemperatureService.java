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
    /**
     * Asynchronous task to retrieve the current temperature.
     *
     * @param callback
     */
    @GET("/spark/getTemp.php")
    void getTemp(Callback<Temperature> callback);

    /**
     * Synchronous task used for Unit Test
     *
     * @return
     */
    @GET("/spark/getTemp")
    Temperature getTemp();
}

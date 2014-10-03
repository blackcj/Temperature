package com.blackcj.temperature.service;

import com.blackcj.temperature.model.Temperature;

import org.json.JSONObject;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Chris on 10/2/2014.
 */
public interface TemperatureService {
    @GET("/spark/getTemp.php")
    void getTemp(Callback<Temperature> callback);
}

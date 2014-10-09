package com.blackcj.temperature.service;

import com.blackcj.temperature.model.Temperature;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Define the API endpoint for Retrofit
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
public interface ReportService {
    @GET("/spark/getReport.php")
    void getReportData(Callback<List<Temperature>> callback);
}

package com.blackcj.temperature.service;

import com.blackcj.temperature.model.Temperature;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Chris on 10/2/2014.
 */
public interface ReportService {
    @GET("/spark/getReport.php")
    void getReportData(Callback<List<Temperature>> callback);
}

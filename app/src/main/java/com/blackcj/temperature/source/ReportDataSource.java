package com.blackcj.temperature.source;

import android.util.Log;

import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.service.ReportService;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chris on 10/2/2014.
 */
public class ReportDataSource implements Callback<List<Temperature>> {

    private ReportListener mListener;

    public ReportDataSource(ReportListener listener) {
        mListener = listener;
    }

    public void getReports() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://blackcj.com")
                .build();

        ReportService service = restAdapter.create(ReportService.class);
        service.getReportData(this);
    }

    @Override
    public void failure(final RetrofitError error) {
        Log.d("TemperatureDataSource", "Error");
        mListener.onError();
    }
    @Override
    public void success(List<Temperature> reportData, Response response) {
        mListener.onReportData(reportData);
    }

    public interface ReportListener {
        public void onReportData(List<Temperature> reportData);
        public void onError();
    }
}

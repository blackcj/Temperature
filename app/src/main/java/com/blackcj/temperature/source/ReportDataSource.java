package com.blackcj.temperature.source;

import android.util.Log;

import com.blackcj.temperature.Constants;
import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.service.ReportService;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Data source used to make requests for report data.
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
public class ReportDataSource implements Callback<List<Temperature>> {

    private ReportListener mListener;

    public ReportDataSource(ReportListener listener) {
        mListener = listener;
    }

    public void getReports() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .build();

        ReportService service = restAdapter.create(ReportService.class);
        service.getReportData(this);
    }

    public void addListener(ReportListener listener) {
        mListener = listener;
    }

    public void removeListeners(){
        mListener = null;
    }

    @Override
    public void failure(final RetrofitError error) {
        Log.d("TemperatureDataSource", "Error");
        mListener.onError();
    }
    @Override
    public void success(List<Temperature> reportData, Response response) {
        if(mListener != null) {
            mListener.onReportData(reportData);
        }
    }

    public interface ReportListener {
        public void onReportData(List<Temperature> reportData);
        public void onError();
    }
}

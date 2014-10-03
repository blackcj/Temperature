package com.blackcj.temperature.source;

import android.util.Log;

import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.service.TemperatureService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chris on 10/2/2014.
 */
public class TemperatureDataSource {

    private TemperatureListener mListener;

    public TemperatureDataSource(TemperatureListener listener) {
        mListener = listener;
    }

    public void getTemp() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://blackcj.com")
                .build();

        TemperatureService service = restAdapter.create(TemperatureService.class);
        service.getTemp(new Callback<Temperature>() {
            @Override
            public void failure(final RetrofitError error) {
                Log.d("TemperatureDataSource", "Error");
                mListener.onError();
            }
            @Override
            public void success(Temperature temp, Response response) {
                Log.d("TemperatureDataSource", "Temperature:" + temp.toString());
                mListener.onTemperature(temp);
            }
        });
    }
    public interface TemperatureListener {
        public void onTemperature(Temperature temperature);
        public void onError();
    }
}

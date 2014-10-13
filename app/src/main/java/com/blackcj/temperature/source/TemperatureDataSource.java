package com.blackcj.temperature.source;

import android.util.Log;

import com.blackcj.temperature.Constants;
import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.service.TemperatureService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Data source used to make requests for temperature data.
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
public class TemperatureDataSource implements Callback<Temperature> {

    private TemperatureListener mListener;

    public TemperatureDataSource(TemperatureListener listener) {
        mListener = listener;
    }

    public void getTemp() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .build();

        TemperatureService service = restAdapter.create(TemperatureService.class);
        service.getTemp(this);
    }

    public void addListener(TemperatureListener listener) {
        mListener = listener;
    }

    public void removeListeners(){
        mListener = null;
    }

    @Override
    public void failure(final RetrofitError error) {
        Log.d("TemperatureDataSource", "Error");
        if(mListener != null) {
            mListener.onError();
        }
    }
    @Override
    public void success(Temperature temp, Response response) {
        Log.d("TemperatureDataSource", "Temperature:" + temp.toString());
        if(mListener != null) {
            mListener.onTemperature(temp);
        }
    }

    public interface TemperatureListener {
        public void onTemperature(Temperature temperature);
        public void onError();
    }
}

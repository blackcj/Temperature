package com.blackcj.temperature;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.service.TemperatureService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chris on 10/14/2014.
 */
public class TemperatureTest extends AndroidTestCase {

    private static final String TAG = "TemperatureTest";
    private Temperature temp;
    private RestAdapter restAdapter;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp");
        super.setUp();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setClient(new LocalJsonClient(getContext()))
                .build();

        TemperatureService service = restAdapter.create(TemperatureService.class);
        temp = service.getTemp();
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown");
        super.tearDown();
    }

    public void testTemperature() throws Exception {
        Log.d(TAG, "Running testTemperature");
        assertEquals(73.5, temp.temperature);
        assertEquals(44.1, temp.humidity);
        assertEquals(0, temp.ac_status);
    }
}

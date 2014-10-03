package com.blackcj.temperature.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcj.temperature.R;
import com.blackcj.temperature.activity.MainActivity;
import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.source.TemperatureDataSource;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Chris on 10/2/2014.
 */
public class CurrentTempFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        TemperatureDataSource.TemperatureListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TemperatureDataSource mDataSource;

    @InjectView(R.id.current_temperature_text)
    TextView currentTempText;

    @InjectView(R.id.current_humidity_text)
    TextView currentHumidityText;

    @InjectView(R.id.swipeRefreshLayout_listView)
    SwipeRefreshLayout mSwipeRefreshLayout;

    protected int section_number;

    public static CurrentTempFragment newInstance(int sectionNumber) {
        CurrentTempFragment f = new CurrentTempFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        section_number = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_current_temp, container, false);

        ButterKnife.inject(this, view);

        mDataSource = new TemperatureDataSource(this);
        mDataSource.getTemp();

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Now return the SwipeRefreshLayout as this fragment's content view
        return view;
    }

    @Override
    public void onRefresh() {
        mDataSource.getTemp();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }

    @Override
    public void onTemperature(Temperature temperature) {
        currentTempText.setText(temperature.toString());
        currentHumidityText.setText(temperature.humidity + "%");
    }

    @Override
    public void onError() {
        Toast.makeText(this.getActivity(), "Error occurred.", Toast.LENGTH_LONG).show();
    }
}

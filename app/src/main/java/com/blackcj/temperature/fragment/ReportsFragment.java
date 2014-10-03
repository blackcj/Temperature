package com.blackcj.temperature.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blackcj.temperature.R;
import com.blackcj.temperature.activity.MainActivity;
import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.source.ReportDataSource;
import com.blackcj.temperature.source.TemperatureDataSource;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Chris on 10/2/2014.
 */
public class ReportsFragment extends BaseFragment implements ReportDataSource.ReportListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ReportDataSource mDataSource;
    private XYSeries series;

    @InjectView(R.id.chart)
    LinearLayout mChart;

    public static ReportsFragment newInstance(int sectionNumber) {
        ReportsFragment f = new ReportsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        ButterKnife.inject(this, view);

        mDataSource = new ReportDataSource(this);
        mDataSource.getReports();

        // Now return the SwipeRefreshLayout as this fragment's content view
        return view;
    }


    @Override
    public void onReportData(List<Temperature> reportData) {
        int hour = 0;
        series = new XYSeries("Temperature hourly");
        for (Temperature hf : reportData) {
            series.add(hour++, hf.temperature);
        }
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(5);
        renderer.setColor(getResources().getColor(R.color.blue14));
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(10);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true, false);
        mRenderer.setLabelsTextSize(40f);
        mRenderer.setYAxisMax(80);
        mRenderer.setYAxisMin(70);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setXLabels(0);
        hour = 0;
        for (Temperature hf : reportData) {
            if(hour % 12 == 0) {
                Double d = series.getX(hour);
                mRenderer.addXTextLabel(d, hf.formatted_date);
            }
            hour++;
        }



        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);
        mChart.addView(chartView);
    }

    @Override
    public void onError() {

    }
}

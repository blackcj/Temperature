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
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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

    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset;
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private XYSeries mCurrentSeries;
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    private ReportDataSource mDataSource;
    private List<Temperature> mReportData;

    /** The chart view that displays the data. */
    private GraphicalView mChartView;

    @InjectView(R.id.chart)
    LinearLayout mChartLayout;

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

        setUpRenderer();
        mCurrentSeries = new XYSeries("Temperature hourly");
        mDataset = new XYMultipleSeriesDataset();
        mDataset.addSeries(mCurrentSeries);
        mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
        mChartView.addZoomListener(new ZoomListener() {
            public void zoomApplied(ZoomEvent e) {
                updateLabels();
            }

            public void zoomReset() {
            }
        }, true, true);
        mChartView.addPanListener(new PanListener() {
            @Override
            public void panApplied() {
                updateLabels();
            }
        });
        mChartLayout.addView(mChartView);

        // Now return the SwipeRefreshLayout as this fragment's content view
        return view;
    }

    private void updateLabels() {
        double start = mRenderer.getXAxisMin();
        double stop = mRenderer.getXAxisMax();
        double quarterStep = (stop - start) / 6;
        double halfStep = (stop - start) / 2;
        mRenderer.clearXTextLabels();
        mRenderer.addXTextLabel(start + quarterStep, mReportData.get((int) (start + quarterStep)).formatted_date);
        mRenderer.addXTextLabel(start + halfStep, mReportData.get((int) (start + halfStep)).formatted_date);
        mRenderer.addXTextLabel(stop - quarterStep, mReportData.get((int) (stop - quarterStep)).formatted_date);
    }

    private void setUpRenderer() {
        // Now we create the renderer
        mCurrentRenderer = new XYSeriesRenderer();
        mCurrentRenderer.setLineWidth(5);
        mCurrentRenderer.setColor(getResources().getColor(R.color.blue14));
        // Include low and max value
        mCurrentRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        mCurrentRenderer.setPointStrokeWidth(10);
        mCurrentRenderer.setShowLegendItem(false);

        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true, false);
        mRenderer.setLabelsTextSize(45f);
        mRenderer.setAxesColor(getResources().getColor(R.color.darkGrey));

        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setXLabels(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mChartView != null) {
            mChartView.repaint();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mDataSource != null) {
            mDataSource.addListener(this);
            mDataSource.getReports();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDataSource.removeListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
        outState.putSerializable("dataset", mDataset);
        outState.putSerializable("renderer", mRenderer);
        outState.putSerializable("current_series", mCurrentSeries);
        outState.putSerializable("current_renderer", mCurrentRenderer);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mDataset = (XYMultipleSeriesDataset) savedInstanceState.getSerializable("dataset");
            mRenderer = (XYMultipleSeriesRenderer) savedInstanceState.getSerializable("renderer");
            mCurrentSeries = (XYSeries) savedInstanceState.getSerializable("current_series");
            mCurrentRenderer = (XYSeriesRenderer) savedInstanceState.getSerializable("current_renderer");
        }
    }

    @Override
    public void onReportData(List<Temperature> reportData) {
        Collections.reverse(reportData);
        int hour = 0;
        mReportData = reportData;
        mCurrentSeries.clear();
        double maxTemp = 70;
        double minTemp = 70;
        for (Temperature hf : reportData) {
            if(hf.temperature > maxTemp) maxTemp = hf.temperature;
            if(hf.temperature < minTemp) minTemp = hf.temperature;
            mCurrentSeries.add(hour++, hf.temperature);
        }
        mRenderer.clearXTextLabels();
        mRenderer.setYAxisMax(Math.round(maxTemp) + 1);
        mRenderer.setYAxisMin(Math.round(minTemp) - 1);
        mRenderer.setXAxisMin(reportData.size()- 30);
        mRenderer.setXAxisMax(reportData.size());
        mRenderer.setPanLimits(new double [] {0, reportData.size(), 0, 0});
        mRenderer.setZoomLimits(new double [] {0, reportData.size(), 0, 0});
        // An array containing the margin size values, in this order: top, left, bottom, right
        mRenderer.setMargins(new int [] {5,0,5,0});

        mRenderer.setYLabelsPadding(-75);

        updateLabels();
        mDataset.clear();
        mDataset.addSeries(mCurrentSeries);
        mChartView.repaint();
    }

    @Override
    public void onError() {

    }
}

package com.blackcj.temperature.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blackcj.temperature.R;
import com.blackcj.temperature.model.Temperature;
import com.blackcj.temperature.source.ReportDataSource;

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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Report graph used to display a chart of historical temperature data.
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
@SuppressWarnings("WeakerAccess") // Butterknife requires public reference of injected views
public class ReportsFragment extends BaseFragment implements ReportDataSource.ReportListener {

    protected DisplayMetrics metrics;
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
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        ButterKnife.inject(this, view);

        mDataSource = new ReportDataSource(this);
        mDataSource.getReports();

        setUpRenderer();
        mCurrentSeries = new XYSeries(getString(R.string.series_title));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mDataSource.getReports();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mCurrentRenderer.setLineWidth(getDPI(2, metrics));
        mCurrentRenderer.setColor(getResources().getColor(R.color.blue14));
        // Include low and max value
        mCurrentRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        mCurrentRenderer.setPointStrokeWidth(getDPI(5, metrics));
        mCurrentRenderer.setShowLegendItem(false);

        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true, false);
        mRenderer.setLabelsTextSize(getDPI(15, metrics));
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

    /**
     * Baseline an int to the correct pixel density.
     *
     * @param size int to convert
     * @param metrics display metrics of current view
     * @return
     */
    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
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

        mRenderer.setYLabelsPadding(-getDPI(35, metrics));

        updateLabels();
        mDataset.clear();
        mDataset.addSeries(mCurrentSeries);
        mChartView.repaint();
    }

    @Override
    public void onError() {
        Toast.makeText(this.getActivity(), getString(R.string.api_error), Toast.LENGTH_LONG).show();
    }
}

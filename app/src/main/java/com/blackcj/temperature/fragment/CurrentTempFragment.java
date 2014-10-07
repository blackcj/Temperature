package com.blackcj.temperature.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcj.core.view.ObservableScrollView;
import com.blackcj.core.view.ScrollViewListener;
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
        TemperatureDataSource.TemperatureListener, ScrollViewListener {
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

    ListFragmentSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.observable_scroll_view)
    ObservableScrollView mObservableScrollView;

    @InjectView(R.id.temp_layout)
    RelativeLayout mTempLayout;

    @InjectView(R.id.humidity_layout)
    RelativeLayout mHumidityLayout;

    @InjectView(R.id.light_layout)
    RelativeLayout mLightLayout;

    @InjectView(R.id.sun_layout)
    RelativeLayout mSunLayout;

    @InjectView(R.id.sun_icon)
    RelativeLayout mSunIcon;

    @InjectView(R.id.scrolling_content)
    RelativeLayout mScrollingContent;

    protected int section_number;
    protected DisplayMetrics metrics;
    protected RelativeLayout[] layouts;

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
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
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
        layouts = new RelativeLayout[]{mTempLayout, mHumidityLayout, mLightLayout, mSunLayout};
        setHasOptionsMenu(true);


        mDataSource = new TemperatureDataSource(this);
        mDataSource.getTemp();

        // Temporarily removed to test functionality without the swipe to refresh

        // Now create a SwipeRefreshLayout to wrap the fragment's content view
        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());

        // Add the list fragment's content view to the SwipeRefreshLayout, making sure that it fills
        // the SwipeRefreshLayout
        mSwipeRefreshLayout.addView(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Make sure that the SwipeRefreshLayout will fill the fragment
        mSwipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

        mObservableScrollView.setScrollViewListener(this);
        mObservableScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // Now return the SwipeRefreshLayout as this fragment's content view
        return mSwipeRefreshLayout;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    private int scrollCap = 0;
    private boolean atBottom = false;

    /**
     * This can be re-written to work as an AnimationPattern of the scroll view.
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int yScroll = y;
        double percentScrolled = (double)scrollView.getScrollY() / (double)scrollView.getMaxScrollAmount();

        if(y < 0) {
            yScroll = 0;
            percentScrolled = 0;
        }

        int diff = (mScrollingContent.getBottom()-(scrollView.getHeight()+scrollView.getScrollY()+mScrollingContent.getTop()));// Calculate the scrolldiff
        if( diff <= 0 && atBottom) {
            return;
        } else if(diff <=0) {
            // if diff is zero, then the bottom has been reached
            Log.d("CurrentTempFragment", "MyScrollView: Bottom has been reached" );
            atBottom = true;
        }else {
            atBottom = false;
        }


        // Layouts are 170dp tall
        int layoutHeight = (int)(170.0 * (1.0 - percentScrolled));
        int shadowPadding = 20;
        layoutHeight = Math.max(layoutHeight, 70);

        if(layoutHeight != 70) {
            // All layouts have locked. Allow the last layout to finish scrolling up
            scrollCap = yScroll;
        }

        // TODO: Instead of using relative layouts, create a class that knows it's min / max size
        for(int i = 0; i < layouts.length; i++) {
            // Convert from int to dp
            int toHeight = getDPI(layoutHeight, metrics);
            int toTopMargin = scrollCap + getDPI(layoutHeight*i - shadowPadding*(i+1), metrics);
            if(i < layouts.length - 1) {
                updateLayout(layouts[i], toHeight, toTopMargin);
            }else {
                // Special case for the last layout, it's taller and doesn't re-size
                updateLayout(layouts[i], toTopMargin);
            }
        }

        // Animate the sun up based on scroll index
        int padding = Math.max(300,(int)(getDPI(400, metrics) * (0.85 - percentScrolled)));
        mSunIcon.setPadding(0,padding,0,0);
    }

    public void updateLayout(RelativeLayout layout, int layoutHeight, int layoutMargin) {
        RelativeLayout.LayoutParams head_params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
        head_params.height = layoutHeight;
        head_params.setMargins(0, layoutMargin, 0, 0); //substitute parameters for left, top, right, bottom
        layout.setLayoutParams(head_params);
    }

    public void updateLayout(RelativeLayout layout, int layoutMargin) {
        RelativeLayout.LayoutParams head_params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
        head_params.setMargins(0, layoutMargin, 0, 0); //substitute parameters for left, top, right, bottom
        layout.setLayoutParams(head_params);
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    public class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        /**
         * As mentioned above, we need to override this method to properly signal when a
         * 'swipe-to-refresh' is possible.
         *
         * @return true if the {@link android.widget.ListView} is visible and can scroll up.
         */
        @Override
        public boolean canChildScrollUp() {
            final ObservableScrollView scrollView = mObservableScrollView;
            if (scrollView.getVisibility() == View.VISIBLE) {
                return canListViewScrollUp(scrollView);
            } else {
                return false;
            }
        }

    }

    /**
     * Utility method to check whether a {@link ObservableScrollView} can scroll up from it's current position.
     * Handles platform version differences, providing backwards compatible functionality where
     * needed.
     */
    private static boolean canListViewScrollUp(ObservableScrollView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            // For ICS and above we can call canScrollVertically() to determine this
            return true;//ViewCompat.canScrollVertically(listView, -1);
        } else {
            // Min SDK 14
            return true;
        }
    }
}

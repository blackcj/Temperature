package com.blackcj.temperature.fragment;

import android.support.v4.app.Fragment;

import butterknife.ButterKnife;

/**
 * Created by Chris on 10/2/2014.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Release the views injects by butterknife
        ButterKnife.reset(this);
    }
}

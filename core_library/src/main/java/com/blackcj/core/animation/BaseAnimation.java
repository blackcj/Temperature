package com.blackcj.core.animation;

import android.widget.RelativeLayout;

import com.blackcj.core.view.ObservableScrollView;
import com.blackcj.core.view.ResizableRelativeLayout;
import com.blackcj.core.view.ScrollViewListener;

/**
 * Created by Chris on 10/12/2014.
 */
public abstract class BaseAnimation implements ScrollViewListener {

    protected ResizableRelativeLayout[] mLayouts;
    protected RelativeLayout mScrollingContent;

    public BaseAnimation(RelativeLayout scrollingContent) {
        mScrollingContent = scrollingContent;
    }

    public void setLayouts(ResizableRelativeLayout[] layouts) {
        mLayouts = layouts;
    }

    public void updateLayout(ResizableRelativeLayout layout, double percent, int topMargin) {
        layout.resize(percent, topMargin);
    }
}

package com.blackcj.core.animation;

import android.util.Log;
import android.widget.RelativeLayout;

import com.blackcj.core.view.ObservableScrollView;

/**
 * Created by Chris on 10/12/2014.
 */
public class StickyAnimation extends BaseAnimation {

    private boolean atBottom = false;
    private int mScrollHeight = 0;

    public StickyAnimation(RelativeLayout scrollingContent) {
        super(scrollingContent);
    }

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
        if(mScrollHeight == 0) {
            mScrollHeight = mScrollingContent.getHeight();
        }
        double percentScrolled = (double)y/(mScrollHeight - scrollView.getHeight());
        if( percentScrolled == 1 && atBottom) {
            return;
        } else if(percentScrolled == 1) {
            // if diff is zero, then the bottom has been reached
            Log.d("CollapseAllButLastAnimation", "MyScrollView: Bottom has been reached");
            atBottom = true;
        }else {
            atBottom = false;
        }

        yScroll = Math.min(yScroll, mScrollHeight - scrollView.getHeight());
        int layoutHeight = 0;
        for(int i = 0; i < mLayouts.length; i++) {
            //Log.d("CollapseAllButLastAnimation", "MyScrollView: " + mLayouts[i].shadowPadding);
            int shadowPadding = 0;
            if(i > 0){
                shadowPadding = (int)mLayouts[i].shadowPadding;
            }
            if(yScroll - shadowPadding >= layoutHeight) {
                mLayouts[i].resize(0, yScroll - shadowPadding);
            }else {
                mLayouts[i].resize(0, layoutHeight);
            }
            layoutHeight += mLayouts[i].getLayoutHeight();

        }
    }
}


package com.blackcj.core.view;

/**
 * Created by Chris on 10/5/2014.
 */
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView implements ValueAnimator.AnimatorUpdateListener {

    // Cap out scroll velocity
    private static int MAX_SCROLL_SPEED = 7000;

    private ScrollViewListener scrollViewListener = null;

    private ValueAnimator animator;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
        this.setSmoothScrollingEnabled(true);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * Override fling behavior to ensure fluid movement.
     *
     * @param velocityY
     */
    @Override
    public void fling (int velocityY)
    {
        int topVelocityY = (int) ((Math.min(Math.abs(velocityY), MAX_SCROLL_SPEED) ) * Math.signum(velocityY));

        animator = ValueAnimator.ofFloat(topVelocityY / 100, 0f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(250);

        animator.addUpdateListener(this);
        animator.start();

        // Default fling behavior caused issues with setting values from onScrollChanged
        //super.fling(topVelocityY);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = ((Float) (animation.getAnimatedValue())).floatValue();
        scrollBy(0, (int)value);
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 0.0f;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return 0.0f;
    }

}

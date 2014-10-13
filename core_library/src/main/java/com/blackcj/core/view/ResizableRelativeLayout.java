package com.blackcj.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.blackcj.core.R;

/**
 * Created by Chris on 10/12/2014.
 */
public class ResizableRelativeLayout extends RelativeLayout {

    public float maxHeight;
    public float minHeight;
    public float shadowPadding = 0.0f;
    public double percent = 0.0;

    public ResizableRelativeLayout(Context context) {
        super(context);

    }

    public ResizableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ResizableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a=getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ResizableRelativeLayout);

        maxHeight = a.getDimension(R.styleable.ResizableRelativeLayout_maxLayoutHeight, 0F);
        minHeight = a.getDimension(R.styleable.ResizableRelativeLayout_minLayoutHeight, 0F);
        shadowPadding = a.getDimension(R.styleable.ResizableRelativeLayout_shadowPadding, 0F);

        //Don't forget to recycle
        a.recycle();
    }

    public int getLayoutHeight(){
        return (int)(minHeight + ((1 - percent) * (maxHeight - minHeight)) - shadowPadding);
    }

    public boolean isFinished() {
        return percent >= 1.0;
    }

    public void resize(double percentHeight, int margin) {
        percent = percentHeight;
        RelativeLayout.LayoutParams head_params = (RelativeLayout.LayoutParams)this.getLayoutParams();
        head_params.height = (int)(minHeight + ((1 - Math.min(percent, 1)) * (maxHeight - minHeight)));
        head_params.topMargin = margin;
        this.setLayoutParams(head_params);
    }
}

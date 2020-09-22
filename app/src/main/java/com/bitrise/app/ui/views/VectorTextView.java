package com.bitrise.app.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;

import com.bitrise.app.R;
import com.bitrise.app.utils.AndroidVersionUtil;

public class VectorTextView extends AppCompatTextView {

    public VectorTextView(Context context) {
        super(context);
    }

    public VectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public VectorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.VectorTextView);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (AndroidVersionUtil.hasLollipop()) {
                drawableLeft = attributeArray.getDrawable(R.styleable.VectorTextView_drawableLeft);
                drawableRight = attributeArray.getDrawable(R.styleable.VectorTextView_drawableRight);
                drawableBottom = attributeArray.getDrawable(R.styleable.VectorTextView_drawableBottom);
                drawableTop = attributeArray.getDrawable(R.styleable.VectorTextView_drawableTop);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.VectorTextView_drawableLeft, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.VectorTextView_drawableRight, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.VectorTextView_drawableBottom, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.VectorTextView_drawableTop, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }
    }
}

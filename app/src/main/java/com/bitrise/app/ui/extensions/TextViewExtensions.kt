package com.bitrise.app.ui.extensions

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.bitrise.app.ui.items.models.DrawableSimpleTextView

fun TextView.applyDrawable(drawableSimpleTextView: DrawableSimpleTextView) {
    val widthRes = drawableSimpleTextView.widthRes ?: drawableSimpleTextView.squareSizeRes
    val heightRes = drawableSimpleTextView.heightRes ?: drawableSimpleTextView.squareSizeRes

    val drawableLeft: Drawable? = drawableSimpleTextView.drawableLeft?.let {
        AppCompatResources.getDrawable(context, it)
            ?.tint(context, drawableSimpleTextView.tintColorRes)
            ?.resize(context, widthRes, heightRes)
    }
    val drawableRight: Drawable? = drawableSimpleTextView.drawableRight?.let {
        AppCompatResources.getDrawable(context, it)
            ?.tint(context, drawableSimpleTextView.tintColorRes)
            ?.resize(context, widthRes, heightRes)
    }
    val drawableBottom: Drawable? = drawableSimpleTextView.drawableBottom?.let {
        AppCompatResources.getDrawable(context, it)
            ?.tint(context, drawableSimpleTextView.tintColorRes)
            ?.resize(context, widthRes, heightRes)
    }
    val drawableTop: Drawable? = drawableSimpleTextView.drawableTop?.let {
        AppCompatResources.getDrawable(context, it)
            ?.tint(context, drawableSimpleTextView.tintColorRes)
            ?.resize(context, widthRes, heightRes)
    }

    setCompoundDrawablesWithIntrinsicBounds(
        drawableLeft,
        drawableTop,
        drawableRight,
        drawableBottom
    )

    drawableSimpleTextView.compoundDrawablePaddingRes?.let {
        compoundDrawablePadding = context.resources.getDimensionPixelSize(it)
    }
}
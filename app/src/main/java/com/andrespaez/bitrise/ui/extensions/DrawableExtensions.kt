package com.andrespaez.bitrise.ui.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.andrespaez.bitrise.R

fun Drawable.tintWithHexColor(hexColor: String?): Drawable =
    hexColor?.let { tintWithColor(Color.parseColor(it)) } ?: this

fun Drawable.tint(context: Context, @ColorRes tintColorRes: Int?): Drawable =
    tintColorRes?.let { tintWithColor(ContextCompat.getColor(context, it)) } ?: this

fun Drawable.tintWithColor(@ColorInt tintColor: Int): Drawable = apply {
    DrawableCompat.wrap(this)
        .apply { DrawableCompat.setTintList(this, ColorStateList.valueOf(tintColor)) }
}

fun Drawable.resize(
    context: Context,
    @DimenRes widthRes: Int?,
    @DimenRes heightRes: Int?
): Drawable =
    if (widthRes != null && heightRes != null) {
        val width = context.resources.getDimensionPixelSize(widthRes)
        val height = context.resources.getDimensionPixelSize(heightRes)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, width, height)
        draw(canvas)
        BitmapDrawable(context.resources, bitmap)
    } else this

fun Drawable.tintBorder(
    context: Context,
    @ColorRes tintColorRes: Int,
    @DimenRes strokeWidthRes: Int = R.dimen.spacing_xmicro
) {
    if (this is GradientDrawable) {
        val strokeWidth = context.resources.getDimensionPixelSize(strokeWidthRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val strokeColor = ContextCompat.getColorStateList(context, tintColorRes)
            setStroke(strokeWidth, strokeColor)
        } else {
            val strokeColor = ContextCompat.getColor(context, tintColorRes)
            setStroke(strokeWidth, strokeColor)
        }
    }
}

package com.bitrise.app.ui.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.text.inSpans

inline fun SpannableStringBuilder.fontSize(
    context: Context,
    @DimenRes fontSizeRes: Int,
    builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(AbsoluteSizeSpan(context.resources.getDimensionPixelSize(fontSizeRes)), builderAction)

fun SpannableStringBuilder.appendDrawable(drawable: Drawable) = apply {
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    setSpan(ImageSpan(drawable), length - 1, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
}

inline fun SpannableStringBuilder.appendClickableText(
    text: String,
    isUnderlineText: Boolean = true,
    crossinline onClickAction: () -> Unit
) = inSpans(object : ClickableSpan() {

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = isUnderlineText
    }

    override fun onClick(view: View) = run { onClickAction() }

}) { append(text) }

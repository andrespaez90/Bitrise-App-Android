package com.bitrise.app.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bitrise.app.ui.utils.ansiEscapeToSpannable

object LogBinding {

    @JvmStatic
    @BindingAdapter("log_text")
    fun setIconByDisableIcon(textView: TextView, log: String?) {
        log?.let{
            textView.text = ansiEscapeToSpannable(textView.context, log)
        }
    }
}
package com.bitrise.app.ui.binding

import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.bold
import androidx.databinding.BindingAdapter
import com.bitrise.app.R
import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.models.UserActivity
import com.bitrise.app.ui.extensions.fontSize

object UserActivityBinding {

    @JvmStatic
    @BindingAdapter("activity_description")
    fun setUserActivityDescription(textView: TextView, userActivity: UserActivity?) {
        userActivity?.let {
            textView.text = SpannableStringBuilder()
                .bold { appendLine(userActivity.title) }
                .fontSize(textView.context, R.dimen.font_caption1) {
                    append(userActivity.description)
                }
        }
    }
}
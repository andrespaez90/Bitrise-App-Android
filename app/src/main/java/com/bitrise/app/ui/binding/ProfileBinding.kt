package com.bitrise.app.ui.binding

import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.bold
import androidx.databinding.BindingAdapter
import com.bitrise.app.R
import com.bitrise.app.network.models.Profile
import com.bitrise.app.ui.extensions.fontSize

object ProfileBinding {

    @JvmStatic
    @BindingAdapter("user_description")
    fun setUserDescription(textView: TextView, profile: Profile?) {
        profile?.let {
            textView.text = SpannableStringBuilder()
                .bold { appendLine(profile.name) }
                .fontSize(textView.context, R.dimen.font_caption1) {
                    append(profile.email)
                }
        }
    }
}
package com.andrespaez.bitrise.ui.binding

import android.text.SpannableStringBuilder
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.ui.extensions.fontSize
import com.andrespaez.bitrise.ui.extensions.getDrawableCompat

object AppBinding {

    @JvmStatic
    @BindingAdapter("app_icon_by_disable_state")
    fun setIconByDisableIcon(imageView: ImageView, isDisable: Boolean?) {
        isDisable?.let {
            imageView.setImageDrawable(
                imageView.context.getDrawableCompat(
                    if (it) R.drawable.ic_disable_circle
                    else R.drawable.ic_check_circle
                )
            )
        }
    }

    @JvmStatic
    @BindingAdapter("app_description")
    fun setAppDescription(textView: TextView, appModel: AppModel?) {
        appModel?.let {
            textView.text = SpannableStringBuilder()
                .fontSize(textView.context, R.dimen.font_subtitle) {
                    bold { appendLine(appModel.title) }
                        .color(R.color.colorAccent) {
                            fontSize(textView.context, R.dimen.font_caption1) {
                                append(appModel.owner.name)
                            }
                        }
                }
        }
    }

}
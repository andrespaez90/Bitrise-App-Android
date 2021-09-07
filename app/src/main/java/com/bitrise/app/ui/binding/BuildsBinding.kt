package com.bitrise.app.ui.binding

import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.bitrise.app.R
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.extensions.fontSize
import com.bitrise.app.ui.extensions.getDrawableCompat

object BuildsBinding {

    @JvmStatic
    @BindingAdapter("build_state_icon")
    fun setIconByDisableIcon(imageView: ImageView, buildsModel: BuildsModel?) {
        buildsModel?.let {
            imageView.setImageDrawable(
                imageView.context.getDrawableCompat(
                    when {
                        buildsModel.isRunning -> R.drawable.ic_running
                        buildsModel.isFailed -> R.drawable.ic_close
                        buildsModel.isSuccess -> R.drawable.ic_done
                        buildsModel.pullRequestId == 0 -> R.drawable.ic_done
                        else -> R.drawable.ic_merge
                    }
                )
            )
        }
    }

    @JvmStatic
    @BindingAdapter("build_state_color")
    fun setBackgroundVyBuildState(layout: ViewGroup, buildsModel: BuildsModel?) {
        buildsModel?.let {
            val color = getColorByStatus(buildsModel)
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, color))
        }
    }

    private fun getColorByStatus(buildsModel: BuildsModel): Int {
        return when {
            buildsModel.status == 0 && buildsModel.isOnHold -> R.color.blue_on_hold
            buildsModel.status == 0 -> R.color.colorAccent
            buildsModel.status == 1 -> R.color.green_success
            buildsModel.isFailed -> R.color.red_abort
            else -> R.color.yellow_aborted
        }
    }

    @JvmStatic
    @BindingAdapter("builds_state_text")
    fun setAppDescription(textView: TextView, buildsModel: BuildsModel?) {
        buildsModel?.let {
            val context = textView.context
            textView.text = SpannableStringBuilder()
                .fontSize(textView.context, R.dimen.font_subtitle) {
                    bold {
                        color(
                            ContextCompat.getColor(
                                context,
                                when {
                                    buildsModel.isOnHold -> R.color.blue_on_hold
                                    buildsModel.status == 0 -> R.color.colorAccent
                                    buildsModel.status == 1 -> R.color.green_success
                                    buildsModel.isFailed -> R.color.red_abort
                                    else -> R.color.yellow_aborted
                                }
                            )
                        ) {
                            append(
                                when {
                                    buildsModel.isOnHold -> context.getString(R.string.copy_state_on_hold)
                                    buildsModel.status == 0 -> context.getString(R.string.copy_state_running)
                                    buildsModel.status == 1 -> context.getString(R.string.copy_state_success)
                                    buildsModel.isFailed -> context.getString(R.string.copy_state_failed)
                                    else -> context.getString(R.string.copy_state_aborted)
                                }
                            )
                        }
                    }
                }

        }
    }

}
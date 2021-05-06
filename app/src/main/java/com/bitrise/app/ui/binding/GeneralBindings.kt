package com.bitrise.app.ui.binding

import android.content.ContextWrapper
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bitrise.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object GeneralBindings {

    @JvmStatic
    @BindingAdapter("load_image")
    fun loadImage(imageView: ImageView, imageUrl: String?) {
        val context: ContextWrapper = imageView.context as ContextWrapper
        var builder = Glide.with(context.baseContext).load(imageUrl)
        val options =
            RequestOptions().placeholder(R.drawable.ic_logo_face).dontAnimate().diskCacheStrategy(
                DiskCacheStrategy.NONE
            )
        builder = builder.apply(options)
        builder.into(imageView)
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun View.setIsVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }
}
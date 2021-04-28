package com.bitrise.app.ui.binding

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.bitrise.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object GeneralBindings {

    @JvmStatic
    @BindingAdapter("load_image")
    fun loadImage(imageView: ImageView, imageUrl: String?) {
        var builder = getGlideRequestManager(imageView.context)?.load(imageUrl)
        val options =
            RequestOptions().placeholder(R.drawable.ic_logo_face).dontAnimate().diskCacheStrategy(
                DiskCacheStrategy.NONE
            )
        builder = builder?.apply(options)
        builder?.into(imageView)
    }

    private fun getGlideRequestManager(context: Context): RequestManager? = when {
        context is FragmentActivity && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !context.isDestroyed) ->
            Glide.with(context)
        context is Activity && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !context.isDestroyed) ->
            Glide.with(context)
        context is ContextWrapper ->
            Glide.with(context.baseContext)
        else -> null
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun View.setIsVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }
}
package com.andrespaez.bitrise.ui.binding

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.andrespaez.bitrise.R
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
            RequestOptions().placeholder(R.drawable.ic_no_image).dontAnimate().diskCacheStrategy(
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
}
package com.bitrise.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bitrise.app.R

class Loading : RelativeLayout {

    var progressBar: ProgressBar? = null
        private set

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        progressBar = ProgressBar(context)
        addView(progressBar, layoutParams)
        setBackgroundColor(ContextCompat.getColor(context, R.color.white_transparent))
        isClickable = true
    }

    fun setState(state: Boolean) {
        if (state) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }

    fun showProgressBar() {
        this.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        this.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }
}

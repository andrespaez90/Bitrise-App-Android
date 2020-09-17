package com.andrespaez.bitrise.ui.items

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemView
import com.andrespaez.bitrise.ui.extensions.applyDrawable
import com.andrespaez.bitrise.ui.items.models.VectorTextParams
import com.andrespaez.bitrise.ui.views.VectorTextView

class SimpleVectorCompatTextView(context: Context) :
    VectorTextView(context),
    GenericItemView<VectorTextParams> {

    private lateinit var itemDescription: VectorTextParams

    init {
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun bind(item: VectorTextParams) {
        itemDescription = item
        initItem(item)
        applyDrawable(item.drawable)
        compoundDrawablePadding =
            resources.getDimensionPixelSize(itemDescription.padding.withDrawable)
        text = if (item.resourceId != 0) context.getString(item.resourceId) else item.text
        gravity = item.gravity
        tag = item.tag
    }

    private fun initItem(item: VectorTextParams) {
        setFontStyle(item)
        addPadding()
        addMargin()
        setBackgroundResource(item.backgroundColor)
    }

    private fun setFontStyle(item: VectorTextParams) {
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(itemDescription.fontSize).toFloat()
        )
        setTextColor(ContextCompat.getColor(context, item.textColor))
    }

    private fun addPadding() {
        val top: Int = resources.getDimensionPixelSize(itemDescription.padding.spacingTop)
        val bottom: Int = resources.getDimensionPixelSize(itemDescription.padding.spacingBottom)
        val left: Int = resources.getDimensionPixelSize(itemDescription.padding.spacingLeft)
        val right: Int = resources.getDimensionPixelSize(itemDescription.padding.spacingRight)
        setPadding(left, top, right, bottom)
    }

    private fun addMargin() {
        val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.topMargin =
            resources.getDimensionPixelSize(itemDescription.margin.spacingTop)
        marginLayoutParams.bottomMargin =
            resources.getDimensionPixelSize(itemDescription.margin.spacingBottom)
        marginLayoutParams.leftMargin =
            resources.getDimensionPixelSize(itemDescription.margin.spacingLeft)
        marginLayoutParams.rightMargin =
            resources.getDimensionPixelSize(itemDescription.margin.spacingRight)
        layoutParams = marginLayoutParams
    }

    override fun getView(): View = rootView

    override val data: VectorTextParams
        get() = itemDescription
}

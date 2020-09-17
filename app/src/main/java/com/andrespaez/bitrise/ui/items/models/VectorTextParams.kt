package com.andrespaez.bitrise.ui.items.models

import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.andrespaez.bitrise.R

data class VectorTextParams(var text: CharSequence) {

    var fontSize = R.dimen.font_body

    var textColor = R.color.font_black

    var backgroundColor = android.R.color.white

    var padding = SpacingSimpleTextView()

    var margin = SpacingSimpleTextView(R.dimen.spacing_empty)

    var gravity = Gravity.CENTER_VERTICAL

    var drawable = DrawableSimpleTextView()

    var tag = ""

    var resourceId: Int = 0
        private set

    constructor(resourceId: Int) : this("") {
        this.resourceId = resourceId
    }
}

class SpacingSimpleTextView() {

    var spacingTop = R.dimen.spacing_medium

    var spacingBottom = R.dimen.spacing_medium

    var spacingLeft = R.dimen.spacing_large

    var spacingRight = R.dimen.spacing_large

    var withDrawable = R.dimen.spacing_medium

    constructor(@DimenRes dimen: Int) : this() {

        spacingTop = dimen

        spacingBottom = dimen

        spacingLeft = dimen

        spacingRight = dimen
    }

    constructor(
        @DimenRes dimenLeft: Int,
        @DimenRes dimenTop: Int,
        @DimenRes dimenRight: Int,
        @DimenRes dimenBottom: Int
    ) : this() {

        spacingTop = dimenTop

        spacingBottom = dimenBottom

        spacingLeft = dimenLeft

        spacingRight = dimenRight
    }
}

data class DrawableSimpleTextView(
    var drawableLeft: Int? = null,
    var drawableRight: Int? = null,
    var drawableBottom: Int? = null,
    var drawableTop: Int? = null,
    @DimenRes var compoundDrawablePaddingRes: Int? = null,
    @ColorRes var tintColorRes: Int? = null,
    @DimenRes var widthRes: Int? = null,
    @DimenRes var heightRes: Int? = null,
    @DimenRes var squareSizeRes: Int? = null
)

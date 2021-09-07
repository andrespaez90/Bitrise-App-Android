package com.bitrise.app.ui.adapters.list.models

import android.view.View

interface GenericItemView<T> {

    val data: T

    fun bind(data: T)

    fun setSelected(isSelected: Boolean)

    fun getView(): View? = null

    fun binds(data: Any?) {
        bind(data as T)
    }

}


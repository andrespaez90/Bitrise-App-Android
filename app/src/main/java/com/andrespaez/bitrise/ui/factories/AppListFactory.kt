package com.andrespaez.bitrise.ui.factories

import android.view.ViewGroup
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemView
import com.andrespaez.bitrise.ui.items.AppDescriptionViewItem
import com.andrespaez.bitrise.ui.items.BuildDescriptionViewItem
import com.andrespaez.bitrise.ui.items.SimpleVectorCompatTextView

const val ITEM_APP_SELECTOR = 1001
const val ITEM_BUILD_SELECTOR = 1002

open class AppListFactory(
    private var listener: ((view: Any) -> Unit)? = null
) : GenericAdapterFactory() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemView<*> {
        return when (viewType) {
            ITEM_APP_SELECTOR -> AppDescriptionViewItem(parent.context).apply {
                setOnClickListener { listener?.invoke(data) }
            }
            ITEM_BUILD_SELECTOR -> BuildDescriptionViewItem(parent.context).apply {
                setOnClickListener { listener?.invoke(it) }
            }
            else -> SimpleVectorCompatTextView(parent.context).apply {
                setOnClickListener { listener?.invoke(it) }
            }
        }
    }

    fun setListener(newListener: (data: Any) -> Unit) {
        this.listener = newListener
    }
}
package com.bitrise.app.ui.factories

import android.view.ViewGroup
import com.bitrise.app.ui.adapters.list.models.GenericItemView
import com.bitrise.app.ui.items.*
import com.bitrise.app.ui.items.charts.CreditsChartByStateViewItem
import com.bitrise.app.ui.items.charts.LineChartViewItem
import com.bitrise.app.ui.items.charts.PieChartViewItem

const val ITEM_APP_SELECTOR = 1001
const val ITEM_BUILD_SELECTOR = 1002
const val ITEM_USER_ACTIVITY = 1003
const val ITEM_ORGANIZATION = 1004
const val ITEM_LINE_CHART = 1005
const val ITEM_STATUS_PIE_BAR_CHART = 1006
const val ITEM_CHAR_CREDITS_BY_STATUS = 1007

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
            ITEM_USER_ACTIVITY -> UserActivityDescriptionViewItem(parent.context)
            ITEM_ORGANIZATION -> OrganizationViewItem(parent.context)
            ITEM_LINE_CHART -> LineChartViewItem(parent.context)
            ITEM_STATUS_PIE_BAR_CHART -> PieChartViewItem(parent.context)
            ITEM_CHAR_CREDITS_BY_STATUS -> CreditsChartByStateViewItem(parent.context)
            else -> SimpleVectorCompatTextView(parent.context).apply {
                setOnClickListener { listener?.invoke(it) }
            }
        }
    }

    fun setListener(newListener: (data: Any) -> Unit) {
        this.listener = newListener
    }
}
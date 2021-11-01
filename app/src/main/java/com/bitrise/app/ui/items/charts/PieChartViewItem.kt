package com.bitrise.app.ui.items.charts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bitrise.app.R
import com.bitrise.app.databinding.ItemPieChartItemBinding
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.adapters.list.models.GenericItemView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartViewItem(context: Context) : GenericItemView<List<BuildsModel>> {

    override lateinit var data: List<BuildsModel>

    private val binding = ItemPieChartItemBinding.inflate(LayoutInflater.from(context))

    init {
        binding.root.layoutParams =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_micro),
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_micro)
                )
            }
        initChart()
    }

    private fun initChart() {
        binding.barChart.description.isEnabled = false
        binding.barChart.animateX(1500)
        binding.barChart.description.isEnabled = false
        binding.barChart.setUsePercentValues(true)
        binding.barChart.setDrawEntryLabels(false)

        val legend: Legend = binding.barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
    }

    override fun bind(data: List<BuildsModel>) {
        this.data = data
        binding.textViewTitle.text = binding.root.context.getString(R.string.title_status_distribution)
        updateChart()
    }

    private fun updateChart() {
        val context = binding.root.context
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(data.count { it.isRunning }.toFloat(), context.getString(R.string.copy_state_running)))
        entries.add(PieEntry(data.count { it.isFailed }.toFloat(), context.getString(R.string.copy_state_failed)))
        entries.add(PieEntry(data.count { it.isSuccess }.toFloat(), context.getString(R.string.copy_state_success)))
        entries.add(PieEntry(data.count { it.isOnHold }.toFloat(), context.getString(R.string.copy_state_on_hold)))
        entries.add(PieEntry(data.count { it.isAborted }.toFloat(), context.getString(R.string.copy_abort)))

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.colors = listOf(
            ContextCompat.getColor(binding.root.context, R.color.colorAccent),
            ContextCompat.getColor(binding.root.context, R.color.red_abort),
            ContextCompat.getColor(binding.root.context, R.color.green_success),
            ContextCompat.getColor(binding.root.context, R.color.blue_on_hold),
            ContextCompat.getColor(binding.root.context, R.color.yellow_aborted),
        )

        binding.barChart.data = PieData(dataSet)
    }


    override fun setSelected(isSelected: Boolean) {
        binding.root.isSelected = isSelected
    }

    override fun getView(): View = binding.root

    fun setOnClickListener(action: (List<BuildsModel>) -> Unit) {
        binding.root.setOnClickListener { action(data) }
    }

}
package com.bitrise.app.ui.items.charts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bitrise.app.R
import com.bitrise.app.databinding.ItemCreditsByStateChartItemBinding
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.adapters.list.models.GenericItemView
import com.bitrise.app.ui.binding.BuildsBinding.getColorByStatus
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class CreditsChartByStateViewItem(private val context: Context) :
    GenericItemView<Map<String, List<BuildsModel>>> {

    override lateinit var data: Map<String, List<BuildsModel>>

    private val binding = ItemCreditsByStateChartItemBinding.inflate(LayoutInflater.from(context))

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
        binding.horizontalChart.run {
            animateY(1500)
            description.isEnabled = false
            setPinchZoom(false)
            setFitBars(true)
            setDrawValueAboveBar(true)
            configureChartAxies(this)
            configureChartLegend(legend)
        }
    }

    private fun configureChartLegend(l: Legend) {
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 8f
        l.xEntrySpace = 4f
    }

    private fun configureChartAxies(chart: HorizontalBarChart) {
        with(chart){
            xAxis.run {
                position = XAxisPosition.BOTTOM
                setDrawAxisLine(true)
                setDrawGridLines(false)
                setDrawLabels(false)
                granularity = 10f
            }

            axisRight.run {
                setDrawAxisLine(true)
                setDrawGridLines(false)
                setDrawLabels(false)
                axisMinimum = 0f
            }

            axisLeft.run {
                setDrawAxisLine(true)
                setDrawGridLines(false)
                axisMinimum = 0f
            }
        }
    }

    override fun bind(data: Map<String, List<BuildsModel>>) {
        this.data = data
        updateChart()
    }

    private fun updateChart() {
        val dataSet = mutableListOf<IBarDataSet>()
        val spaceForBar = 11f
        data.onEachIndexed { index, entry ->
            dataSet.add(
                BarDataSet(
                    listOf(
                        BarEntry(
                            (index.toFloat() + 1) * spaceForBar,
                            entry.value.sumBy { build -> build.creditCost?.toInt() ?: 0 }.toFloat()
                        )
                    ),
                    entry.key
                ).apply {
                    color = ContextCompat.getColor(context, getColorByStatus(entry.value.first()))
                }
            )
        }

        val data = BarData(dataSet)
        data.setValueTextSize(10f)
        data.barWidth = 10f
        binding.horizontalChart.data = data
    }


    override fun setSelected(isSelected: Boolean) {
        binding.root.isSelected = isSelected
    }

    override fun getView(): View = binding.root

    fun setOnClickListener(action: (Map<String, List<BuildsModel>>) -> Unit) {
        binding.root.setOnClickListener { action(data) }
    }

}
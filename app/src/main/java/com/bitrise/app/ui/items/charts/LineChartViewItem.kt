package com.bitrise.app.ui.items.charts

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bitrise.app.R
import com.bitrise.app.databinding.ItemLineChartItemBinding
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.adapters.list.models.GenericItemView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

typealias BuildMetrics = Map.Entry<String, List<BuildsModel>>

class LineChartViewItem(context: Context) : GenericItemView<BuildMetrics> {

    override lateinit var data: BuildMetrics

    private val binding = ItemLineChartItemBinding.inflate(LayoutInflater.from(context))


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
        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.animateX(1500)

        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.xAxis.setDrawAxisLine(false)
        binding.lineChart.xAxis.setDrawLabels(false)
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        binding.lineChart.axisRight.setDrawAxisLine(false)
        binding.lineChart.axisRight.setDrawLabels(false)

        binding.lineChart.axisLeft.setDrawAxisLine(false)
        binding.lineChart.axisLeft.setDrawLabels(true)
    }

    override fun bind(data: BuildMetrics) {
        this.data = data
        binding.textViewTitle.text =
            SpannableStringBuilder(binding.root.context.getString(R.string.copy_workflow))
                .append(": ${data.key}")

        updateChart()
    }

    private fun updateChart() {
        drawLine(data.value.filter { it.isSuccess })
    }

    private fun drawLine(builds: List<BuildsModel>) {
        val line = LineDataSet(
            builds.sortedBy { it.startedOnWorkerAt }
                .mapIndexed { index, dataEntry ->
                    Entry(
                        index.toFloat(),
                        dataEntry.buildTime.toFloat()
                    )
                },
            "",
        ).applyStyle()

        val data = LineData(line)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)
        binding.lineChart.data = data
    }

    override fun setSelected(isSelected: Boolean) {
        binding.root.isSelected = isSelected
    }

    override fun getView(): View = binding.root

    fun setOnClickListener(action: (BuildMetrics) -> Unit) {
        binding.root.setOnClickListener { action(data) }
    }

    private fun LineDataSet.applyStyle(): LineDataSet = apply {
        lineWidth = 2.5f
        color = ColorTemplate.getHoloBlue()
        setCircleColor(ColorTemplate.getHoloBlue())
        circleRadius = 3f;
        fillAlpha = 65
        fillColor = ColorTemplate.getHoloBlue()
        highLightColor = Color.TRANSPARENT

    }
}
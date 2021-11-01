package com.bitrise.app.ui.activities.build

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitrise.app.R
import com.bitrise.app.databinding.ActivityBuildsBinding
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.activities.BaseActivity
import com.bitrise.app.ui.adapters.list.GenericAdapter
import com.bitrise.app.ui.adapters.list.models.GenericItemAbstract
import com.bitrise.app.ui.factories.*
import com.bitrise.app.ui.factories.ITEM_CHAR_CREDITS_BY_STATUS
import com.bitrise.app.ui.items.BuildEvents
import com.bitrise.app.viewModels.builds.BuildInfo
import com.bitrise.app.viewModels.builds.BuildsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val ACTIVITY_PARAM_APP_MODEL = "param_app_model"
const val ACTIVITY_PARAM_BUILD_MODEL = "param_build_model"

@AndroidEntryPoint
class BuildsActivity : BaseActivity() {

    private val viewModel: BuildsViewModel by viewModels()

    private lateinit var binding: ActivityBuildsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_builds)
        initView()
        subscribe()
        initListeners()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.recyclerViewList.run {
            adapter = GenericAdapter(AppListFactory().apply {
                setListener { data ->
                    when (data) {
                        is BuildEvents.AbortBuild -> viewModel.abortBuild(data.data)
                        is BuildEvents.ViewBuild -> viewModel.openBuildDetail(data.data)
                    }
                }
            })
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun subscribe() {
        intent.getParcelableExtra<AppModel>(ACTIVITY_PARAM_APP_MODEL)
            ?.let { viewModel.setCurrentAppModel(it) }
        subscribeViewModel(viewModel, binding.root)
        viewModel.onBuildsChange.observe(this, ::updateView)
    }

    private fun updateView(builds: Pair<BuildInfo, List<BuildsModel>>) {
        if (builds.first == BuildInfo.LIST) updateBuilds(builds.second)
        else updateMetrics(builds.second)
    }

    private fun updateBuilds(builds: List<BuildsModel>) {
        activeView(binding.textViewList)
        deactivateView(binding.textViewMetrics)
        (binding.recyclerViewList.adapter as GenericAdapter).items =
            builds.sortedByDescending { it.triggeredAt }
                .map { GenericItemAbstract(it, ITEM_BUILD_SELECTOR) }
    }

    private fun updateMetrics(builds: List<BuildsModel>) {
        deactivateView(binding.textViewList)
        activeView(binding.textViewMetrics)
        with((binding.recyclerViewList.adapter as GenericAdapter)) {

            clearAll()

            if (builds.any { it.hasCredits }) {
                addItem(
                    GenericItemAbstract(
                        builds.filter { it.hasCredits }.groupBy { it.statusText },
                        ITEM_CHAR_CREDITS_BY_STATUS
                    )
                )
            }

            addItems(
                builds
                    .groupBy { it.triggeredWorkflow }
                    .filter { it.value.filter { build -> build.isSuccess }.size >= 2 }
                    .map { GenericItemAbstract(it, ITEM_LINE_CHART) }
            )

            addItem(GenericItemAbstract(builds, ITEM_STATUS_PIE_BAR_CHART))
        }
    }

    private fun activeView(view: TextView) {
        view.setTextColor(ContextCompat.getColor(this, R.color.colorSecondary))
        view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        view.elevation = 2f
    }

    private fun deactivateView(view: TextView) {
        view.setTextColor(ContextCompat.getColor(this, R.color.font_black))
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
        view.elevation = 0f
    }


    private fun initListeners() {
        binding.layoutRefresh.setOnRefreshListener { viewModel.updateBuilds() }
        binding.buttonStartBuild.setOnClickListener { viewModel.startBuildView() }
        binding.textViewMetrics.setOnClickListener { viewModel.onMetricsClick() }
        binding.textViewList.setOnClickListener { viewModel.onListClick() }
    }

    /**
     * Overrides
     */

    override fun showLoading(showing: Boolean) {
        binding.layoutRefresh.isRefreshing = showing
    }
}
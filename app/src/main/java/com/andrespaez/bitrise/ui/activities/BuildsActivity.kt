package com.andrespaez.bitrise.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.databinding.ActivityBuildsBinding
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.network.models.BuildsModel
import com.andrespaez.bitrise.ui.adapters.list.GenericAdapter
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemAbstract
import com.andrespaez.bitrise.ui.factories.AppListFactory
import com.andrespaez.bitrise.ui.factories.ITEM_BUILD_SELECTOR
import com.andrespaez.bitrise.ui.items.BuildEvents
import com.andrespaez.bitrise.viewModels.BuildsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val ACTIVITY_PARAM_APP_MODEL = "param_app_model"

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
                    when(data){
                        is BuildEvents.AbortBuild ->viewModel.abortBuild(data.data)
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

    private fun updateView(builds: List<BuildsModel>) {
        (binding.recyclerViewList.adapter as GenericAdapter).setItems(
            builds.sortedByDescending { it.triggeredAt }
                .map { GenericItemAbstract(it, ITEM_BUILD_SELECTOR) }
        )
    }

    private fun initListeners(){
        binding.layoutRefresh.setOnRefreshListener { viewModel.updateBuilds() }
    }

    /**
     * Overrides
     */

    override fun showLoading(showing: Boolean) {
        binding.layoutRefresh.isRefreshing = showing
    }
}
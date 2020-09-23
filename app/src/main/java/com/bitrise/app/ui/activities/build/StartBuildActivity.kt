package com.bitrise.app.ui.activities.build

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bitrise.app.R
import com.bitrise.app.databinding.ActivityStartBuildBinding
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.ui.activities.BaseActivity
import com.bitrise.app.viewModels.builds.StartBuildsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartBuildActivity : BaseActivity() {

    private val viewModel: StartBuildsViewModel by viewModels()

    private lateinit var binding: ActivityStartBuildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_build)
        subscribeViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun subscribeViewModel() {
        subscribeViewModel(viewModel, binding.root)
        initViewModel()
        viewModel.onBranchesChange.observe(
            this, Observer { updateOptions(binding.autoCompleteBranch, it) })

        viewModel.onWorkFlowsChange.observe(
            this,
            Observer { updateOptions(binding.autoCompleteWorkflow, it) })
    }

    private fun initViewModel() {
        intent.getParcelableExtra<AppModel>(ACTIVITY_PARAM_APP_MODEL)
            ?.let { viewModel.setCurrentApp(it) }
    }

    private fun updateOptions(
        view: AppCompatAutoCompleteTextView,
        options: List<String>
    ) {
        view.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                options
            )
        )
    }
}
package com.andrespaez.bitrise.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.databinding.ActivityStartBuildBinding
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.viewModels.builds.StartBuildsViewModel
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
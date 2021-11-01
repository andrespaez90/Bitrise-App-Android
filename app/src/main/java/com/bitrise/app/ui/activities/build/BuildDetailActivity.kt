package com.bitrise.app.ui.activities.build

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bitrise.app.R
import com.bitrise.app.databinding.ActivityBuildDetailBinding
import com.bitrise.app.extensions.biLet
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.activities.BaseActivity
import com.bitrise.app.viewModels.builds.BuildDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuildDetailActivity : BaseActivity() {

    private val viewModel: BuildDetailViewModel by viewModels()

    private lateinit var binding: ActivityBuildDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_build_detail)
        subscribeViewModel()
        configView()
    }

    private fun subscribeViewModel() {
        subscribeViewModel(viewModel, binding.root)
        val app = intent.getParcelableExtra<AppModel>(ACTIVITY_PARAM_APP_MODEL)
        val build = intent.getParcelableExtra<BuildsModel>(ACTIVITY_PARAM_BUILD_MODEL)
        Pair(app, build).biLet { appModel, buildsModel ->
            viewModel.updateBuild(appModel.slug, buildsModel)
        }
    }

    private fun configView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.textViewLog.movementMethod= ScrollingMovementMethod()
    }
}

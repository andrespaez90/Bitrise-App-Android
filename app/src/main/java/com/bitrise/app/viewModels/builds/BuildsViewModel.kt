package com.bitrise.app.viewModels.builds

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.activities.build.*
import com.bitrise.app.viewModels.AndroidViewModel
import com.bitrise.app.viewModels.models.StartActivityModel

private const val KEY_CURRENT_APP_MODEL = "current_app_model"

class BuildsViewModel @ViewModelInject constructor(
    private val appsApi: AppsApi,
    @Assisted private val savedStateHandle: SavedStateHandle
) : AndroidViewModel() {

    private val buildModels = MutableLiveData<List<BuildsModel>>()

    private val currentAppModel: AppModel?
        get() = savedStateHandle.get<AppModel>(KEY_CURRENT_APP_MODEL)


    /**
     * Actions
     */

    fun setCurrentAppModel(appModel: AppModel) {
        savedStateHandle.set(KEY_CURRENT_APP_MODEL, appModel)
        updateBuilds()
    }

    fun updateBuilds() {
        disposables.add(appsApi.getAppBuilds(currentAppModel?.slug.orEmpty())
            .doOnSubscribe { showLoading() }
            .doOnSubscribe { hideLoading() }
            .subscribe(::updateBuild, ::showServiceError)
        )
    }

    private fun updateBuild(builds: List<BuildsModel>) {
        buildModels.postValue(builds)
    }

    fun abortBuild(buildsModel: BuildsModel) {
        disposables.add(appsApi.abortBuild(currentAppModel?.slug.orEmpty(), buildsModel.slug)
            .doOnSubscribe { showLoading() }
            .doOnSubscribe { hideLoading() }
            .subscribe({ updateBuilds() }, ::showServiceError)
        )
    }

    fun startBuildView() {
        startActivity.postValue(
            StartActivityModel(
                StartBuildActivity::class.java,
                bundleOf(
                    ACTIVITY_PARAM_APP_MODEL to currentAppModel
                ),
                START_BUILD_ACTIVITY_CODE
            )
        )
    }

    fun openBuildDetail(data: BuildsModel) {
        startActivity.postValue(StartActivityModel(BuildDetailActivity::class.java).apply {
            bundle = Bundle().apply {
                putParcelable(ACTIVITY_PARAM_APP_MODEL, currentAppModel)
                putParcelable(ACTIVITY_PARAM_BUILD_MODEL, data)
            }
        })
    }

    /**
     * ViewModels
     */

    val onBuildsChange: LiveData<List<BuildsModel>> = buildModels
}
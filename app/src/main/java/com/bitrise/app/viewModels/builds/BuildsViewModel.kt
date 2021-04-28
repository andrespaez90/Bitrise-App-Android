package com.bitrise.app.viewModels.builds

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.ui.activities.build.ACTIVITY_PARAM_APP_MODEL
import com.bitrise.app.ui.activities.build.ACTIVITY_PARAM_BUILD_MODEL
import com.bitrise.app.ui.activities.build.BuildDetailActivity
import com.bitrise.app.ui.activities.build.StartBuildActivity
import com.bitrise.app.viewModels.AndroidViewModel
import com.bitrise.app.viewModels.models.StartActivityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuildsViewModel @Inject constructor(
    private val appsApi: AppsApi,
) : AndroidViewModel() {

    private var currentBuildInfo: BuildInfo = BuildInfo.LIST

    private val buildModels = MutableLiveData<Pair<BuildInfo, List<BuildsModel>>>()

    private lateinit var currentAppModel: AppModel

    /**
     * Actions
     */

    fun setCurrentAppModel(appModel: AppModel) {
        this.currentAppModel = appModel
        updateBuilds()
    }

    fun updateBuilds() {
        disposables.add(appsApi.getAppBuilds(this.currentAppModel.slug)
            .doOnSubscribe { showLoading() }
            .doOnSubscribe { hideLoading() }
            .subscribe(::updateBuild, ::showServiceError)
        )
    }

    private fun updateBuild(builds: List<BuildsModel>) {
        buildModels.postValue(Pair(currentBuildInfo, builds))
    }

    fun abortBuild(buildsModel: BuildsModel) {
        disposables.add(appsApi.abortBuild(currentAppModel.slug, buildsModel.slug)
            .doOnSubscribe { showLoading() }
            .doOnSubscribe { hideLoading() }
            .subscribe({ updateBuilds() }, ::showServiceError)
        )
    }

    fun startBuildView() {
        startActivity.postValue(StartActivityModel(StartBuildActivity::class.java).apply {
            bundle = Bundle().apply { putParcelable(ACTIVITY_PARAM_APP_MODEL, currentAppModel) }
        })
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
     * onClick
     */

    fun onMetricsClick(){
        currentBuildInfo = BuildInfo.METRICS
        buildModels.value?.second?.let {  buildModels.postValue(Pair(currentBuildInfo, it)) }

    }

    fun onListClick(){
        currentBuildInfo = BuildInfo.LIST
        buildModels.value?.second?.let {  buildModels.postValue(Pair(currentBuildInfo, it)) }
    }

    /**
     * ViewModels
     */

    val onBuildsChange: LiveData<Pair<BuildInfo, List<BuildsModel>>> = buildModels
}

enum class BuildInfo(val type: String) {
    LIST("list"),
    METRICS("Metrics"),
}
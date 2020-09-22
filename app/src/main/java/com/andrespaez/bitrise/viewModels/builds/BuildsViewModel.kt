package com.andrespaez.bitrise.viewModels.builds

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.network.api.AppsApi
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.network.models.BuildsModel
import com.andrespaez.bitrise.ui.activities.ACTIVITY_PARAM_APP_MODEL
import com.andrespaez.bitrise.ui.activities.StartBuildActivity
import com.andrespaez.bitrise.viewModels.AndroidViewModel
import com.andrespaez.bitrise.viewModels.models.StartActivityModel

class BuildsViewModel @ViewModelInject constructor(
    private val appsApi: AppsApi,
) : AndroidViewModel() {

    private val buildModels = MutableLiveData<List<BuildsModel>>()

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
        buildModels.postValue(builds)
    }

    fun abortBuild(buildsModel: BuildsModel) {
        disposables.add(appsApi.abortBuild(currentAppModel.slug, buildsModel.slug)
            .doOnSubscribe { showLoading() }
            .doOnSubscribe { hideLoading() }
            .subscribe({ updateBuilds() }, ::showServiceError)
        )
    }

    fun startBuildView(){
        startActivity.postValue(StartActivityModel(StartBuildActivity::class.java).apply {
            bundle = Bundle().apply { putParcelable(ACTIVITY_PARAM_APP_MODEL, currentAppModel) }
        })
    }

    /**
     * ViewModels
     */

    val onBuildsChange: LiveData<List<BuildsModel>> = buildModels
}
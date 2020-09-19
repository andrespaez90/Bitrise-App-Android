package com.andrespaez.bitrise.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.network.api.AppsApi
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.network.models.BuildsModel

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

    /**
     * ViewModels
     */

    val onBuildsChange: LiveData<List<BuildsModel>> = buildModels
}
package com.bitrise.app.viewModels.builds

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.network.models.LogModel
import com.bitrise.app.viewModels.AndroidViewModel

class BuildDetailViewModel @ViewModelInject constructor(
    val appsApi: AppsApi
) : AndroidViewModel() {

    val build = MutableLiveData<BuildsModel>()

    val buildLog = MutableLiveData<String>()

    private lateinit var appSlug: String

    /**
     * Functions
     */

    fun updateBuild(appSlug: String, build: BuildsModel) {
        this.appSlug = appSlug
        this.build.postValue(build)
        getBuildLog(this.appSlug, build)

    }

    fun getBuildLog(appSlug: String = this.appSlug, buildModel: BuildsModel? = build.value) {
        buildModel?.let {
            disposables.add(
                appsApi.getLog(appSlug, buildModel.slug)
                    .doOnSubscribe { showLoading() }
                    .doFinally { hideLoading() }
                    .subscribe(::updateLog, ::showServiceError)
            )
        }
    }

    private fun updateLog(log: LogModel) {
        buildLog.postValue(log.logChunks.joinToString { it.chunk })
    }

}
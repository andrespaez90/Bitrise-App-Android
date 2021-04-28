package com.bitrise.app.viewModels.builds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.BuildsModel
import com.bitrise.app.network.models.LogModel
import com.bitrise.app.viewModels.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuildDetailViewModel @Inject constructor(
    val appsApi: AppsApi
) : AndroidViewModel() {

    private val _build = MutableLiveData<BuildsModel>()

    private val _buildLog = MutableLiveData<String>()

    private lateinit var appSlug: String

    /**
     * Functions
     */

    fun updateBuild(appSlug: String, build: BuildsModel) {
        this.appSlug = appSlug
        this._build.postValue(build)
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
        _buildLog.postValue(log.logChunks.joinToString { it.chunk })
    }

    /**
     * Live Data
     */

    val buildLog: LiveData<String> = _buildLog

    val build: LiveData<BuildsModel> = _build


}
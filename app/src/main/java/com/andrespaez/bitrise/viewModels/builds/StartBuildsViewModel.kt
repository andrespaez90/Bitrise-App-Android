package com.andrespaez.bitrise.viewModels.builds

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.network.api.AppsApi
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.viewModels.AndroidViewModel
import com.andrespaez.bitrise.viewModels.models.FinishActivityModel

class StartBuildsViewModel @ViewModelInject constructor(
    private val appsApi: AppsApi
) : AndroidViewModel() {

    private val branches = MutableLiveData<List<String>>()

    private val workFlows = MutableLiveData<List<String>>()

    val branch = MutableLiveData<String>()

    val workflow = MutableLiveData<String>()

    val message = MutableLiveData<String>()

    private lateinit var currentApp: AppModel


    /**
     * Functions
     */

    fun setCurrentApp(appModel: AppModel) {
        this.currentApp = appModel
        disposables.addAll(

            appsApi.getWorkFlows(appModel.slug)
                .subscribe({ workFlows.postValue(it) }, ::showServiceError),

            appsApi.getBranches(currentApp.slug)
                .subscribe({ branches.postValue(it) }, ::showServiceError)
        )
    }

    fun startBuild() {
        disposables.add(
            appsApi.startBuild(
                currentApp.slug,
                branch = branch.value ?: "",
                workflow = workflow.value ?: "",
                message = message.value ?: "Build from App"
            )
                .doOnSubscribe { showLoading() }
                .doFinally { hideLoading() }
                .subscribe(::onCloseView, ::showServiceError)
        )
    }

    fun onPressBack() {
        closeView.postValue(FinishActivityModel(Activity.RESULT_CANCELED))
    }

    /**
     * Live Data
     */

    val onBranchesChange: LiveData<List<String>> = branches

    val onWorkFlowsChange: LiveData<List<String>> = workFlows

}
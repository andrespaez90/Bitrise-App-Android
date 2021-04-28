package com.bitrise.app.viewModels.builds

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.viewModels.AndroidViewModel
import com.bitrise.app.viewModels.models.FinishActivityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartBuildsViewModel @Inject constructor(
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
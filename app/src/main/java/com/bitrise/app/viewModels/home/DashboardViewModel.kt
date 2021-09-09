package com.bitrise.app.viewModels.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.AppsApi
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.ui.activities.build.ACTIVITY_PARAM_APP_MODEL
import com.bitrise.app.ui.activities.build.BuildsActivity
import com.bitrise.app.viewModels.AndroidViewModel
import com.bitrise.app.viewModels.models.StartActivityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val appsApi: AppsApi
) : AndroidViewModel() {

    private var allApps: List<AppModel> = emptyList()

    private val apps = MutableLiveData<List<AppModel>>()

    private val seekerHandler = Handler(Looper.getMainLooper())

    private val searchTask: Runnable = Runnable { search() }

    var lastQuery = ""
        private set

    init {
        updateApps()
    }

    /**
     * Actions
     */

    fun updateApps() {
        disposables.add(
            appsApi.getApps()
                .doOnSubscribe { showLoading() }
                .doFinally { hideLoading() }
                .subscribe(
                    { it?.let { allApps = it; apps.postValue(it) } },
                    { showServiceError(it) })
        )
    }

    fun findApp(query: String) {
        lastQuery = query
        seekerHandler.removeCallbacks(searchTask)
        if (query.isEmpty()) {
            apps.postValue(allApps)
        } else {
            seekerHandler.postDelayed(searchTask, 500)
        }
    }

    private fun search() {
        if (lastQuery.isNotEmpty()) {
            apps.postValue(allApps.filter { it.title.contains(lastQuery, true) || it.owner.name.contains(lastQuery, true) })
        }
    }

    fun openBuildsApp(appModel: AppModel) {
        startActivity.postValue(StartActivityModel(BuildsActivity::class.java).apply {
            bundle = Bundle().apply { putParcelable(ACTIVITY_PARAM_APP_MODEL, appModel) }
        })
    }


    /**
     * Live Data
     */

    val onAppsChange: LiveData<List<AppModel>> = apps
}
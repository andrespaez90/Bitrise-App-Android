package com.andrespaez.bitrise.viewModels.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.network.api.AppsApi
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.ui.activities.BuildsActivity
import com.andrespaez.bitrise.viewModels.AndroidViewModel
import com.andrespaez.bitrise.viewModels.models.StartActivityModel

class DashboardViewModel @ViewModelInject constructor(
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
            apps.postValue(allApps.filter { it.title.contains(lastQuery) })
        }
    }

    fun openBuildsApp(appModel: AppModel) {
        startActivity.postValue(StartActivityModel(BuildsActivity::class.java).apply {
            bundle = Bundle().apply { putParcelable("key_app", appModel) }
        })
    }


    /**
     * Live Data
     */

    val onAppsChange: LiveData<List<AppModel>> = apps
}
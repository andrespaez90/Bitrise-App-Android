package com.bitrise.app.viewModels

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.R
import com.bitrise.app.data.AuthorizationPreference
import com.bitrise.app.managers.preferences.PrefsManager
import com.bitrise.app.network.api.UserApi
import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.utils.ErrorUtil
import com.bitrise.app.ui.activities.HomeActivity
import com.bitrise.app.ui.factories.SnackBarFactory
import com.bitrise.app.viewModels.models.FinishActivityModel
import com.bitrise.app.viewModels.models.StartActivityModel
import com.google.android.material.snackbar.Snackbar

class MainActivityViewModel @ViewModelInject constructor(
    private val userApi: UserApi,
    private val prefsManager: PrefsManager
) : AndroidViewModel() {

    val accessToken = MutableLiveData<String>()

    init {
        validateToke()
    }

    /**
     * Actions
     */

    fun saveAndValidateToken() {
        prefsManager.set(AuthorizationPreference(), accessToken.value)
        validateToke()
    }

    private fun validateToke() {
        if (prefsManager.getString(AuthorizationPreference()).isNotBlank()) {
            disposables.add(userApi.getProfile()
                .doOnSubscribe { showLoading() }
                .doFinally { hideLoading() }
                .subscribe(::startDashboard, ::showError))
        }
    }

    private fun startDashboard(profile: Profile) {
        startActivity.postValue(StartActivityModel(HomeActivity::class.java))
        closeView.postValue(FinishActivityModel(Activity.RESULT_OK))
    }

    private fun showError(error: Throwable) {
        prefsManager.set(AuthorizationPreference(), "")
        if (ErrorUtil.getError(error)?.errorCode == 401) {
            showSnackBarMessage(
                SnackBarFactory.TYPE_ERROR,
                R.string.error_access_token_validation,
                Snackbar.LENGTH_LONG
            )
        } else {
            showServiceError(error)
        }
    }
}
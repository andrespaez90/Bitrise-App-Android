package com.andrespaez.bitrise.viewModels

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.data.AuthorizationPreference
import com.andrespaez.bitrise.managers.preferences.PrefsManager
import com.andrespaez.bitrise.network.api.UserApi
import com.andrespaez.bitrise.network.models.Profile
import com.andrespaez.bitrise.network.utils.ErrorUtil
import com.andrespaez.bitrise.ui.activities.HomeActivity
import com.andrespaez.bitrise.ui.factories.SnackBarFactory
import com.andrespaez.bitrise.viewModels.models.FinishActivityModel
import com.andrespaez.bitrise.viewModels.models.StartActivityModel
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
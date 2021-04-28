package com.bitrise.app.viewModels.home

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.data.AuthorizationPreference
import com.bitrise.app.managers.preferences.PrefsManager
import com.bitrise.app.network.api.OrganizationApi
import com.bitrise.app.network.api.UserApi
import com.bitrise.app.network.models.Organization
import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.models.UserActivity
import com.bitrise.app.ui.activities.MainActivity
import com.bitrise.app.viewModels.AndroidViewModel
import com.bitrise.app.viewModels.models.StartActivityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefsManager: PrefsManager,
    private val profileApi: UserApi,
    private val organizationApi: OrganizationApi
) : AndroidViewModel() {

    private val _profile = MutableLiveData<Profile>()

    private val activity = MutableLiveData<List<UserActivity>>()

    private val organizations = MutableLiveData<List<Organization>>()

    init {
        disposables.addAll(
            organizationApi.getOrganizations()
                .subscribe(organizations::postValue, ::showServiceError),

            profileApi.getActivity()
                .subscribe(activity::postValue, ::showServiceError)
        )
    }

    fun updateUserProfile(data: Profile? = null) {
        data?.let { _profile.postValue(data) }
            ?: run { updateProfile() }
    }

    private fun updateProfile() {
        disposables.add(
            profileApi.getProfile()
                .subscribe(_profile::postValue, ::showServiceError)
        )
    }

    /**
     * Actions
     */

    fun logOut() {
        prefsManager.set(AuthorizationPreference(), "")
        startActivity.postValue(StartActivityModel(MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    /**
     * Live Data
     */

    val profile: LiveData<Profile> = _profile

    val onActivityChange: LiveData<List<UserActivity>> = activity

    val onOrganizationsChange: LiveData<List<Organization>> = organizations
}
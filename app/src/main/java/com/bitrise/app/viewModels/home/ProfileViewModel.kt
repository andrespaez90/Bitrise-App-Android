package com.bitrise.app.viewModels.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitrise.app.network.api.OrganizationApi
import com.bitrise.app.network.api.UserApi
import com.bitrise.app.network.models.Organization
import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.models.UserActivity
import com.bitrise.app.viewModels.AndroidViewModel

class ProfileViewModel @ViewModelInject constructor(
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
     * Live Data
     */

    val profile: LiveData<Profile> = _profile

    val onActivityChange: LiveData<List<UserActivity>> = activity

    val onOrganizationsChange: LiveData<List<Organization>> = organizations
}
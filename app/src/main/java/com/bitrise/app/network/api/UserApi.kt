package com.bitrise.app.network.api

import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.models.UserActivity
import com.bitrise.app.network.services.UserServices
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class UserApi @Inject constructor(private val services: UserServices) : BaseApi() {

    @CheckReturnValue
    fun getProfile(scheduler: Scheduler? = null): Single<Profile> =
        subscribe(
            services.getProfile().flatMap { Single.just(it.profile) },
            scheduler
        )

    @CheckReturnValue
    fun getActivity(limit: Int = 5, scheduler: Scheduler? = null): Single<List<UserActivity>> =
        subscribe(
            services.getActivities(limit).flatMap { Single.just(it.data) },
            scheduler
        )

}
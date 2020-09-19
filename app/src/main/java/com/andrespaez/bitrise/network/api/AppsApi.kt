package com.andrespaez.bitrise.network.api

import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.network.services.AppsServices
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class AppsApi @Inject constructor(private val services: AppsServices) : BaseApi() {

    @CheckReturnValue
    fun getApps(scheduler: Scheduler? = null): Single<List<AppModel>> =
        subscribe(
            services.getApps().flatMap { Single.just(it.data) },
            scheduler
        )

    @CheckReturnValue
    fun getAppBuilds(appSlug: String, scheduler: Scheduler? = null) =
        subscribe(
            services.getBuilds(appSlug).flatMap { Single.just(it.data) },
            scheduler
        )

    @CheckReturnValue
    fun abortBuild(appSlug: String, buildSlug: String, scheduler: Scheduler? = null) =
        subscribe(
            services.abortBuild(appSlug, buildSlug),
            scheduler
        )
}
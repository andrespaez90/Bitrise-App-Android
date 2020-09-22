package com.bitrise.app.network.api

import com.bitrise.app.network.models.AppModel
import com.bitrise.app.network.models.StartBuildBody
import com.bitrise.app.network.services.AppsServices
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
    fun getBranches(appSlug: String, scheduler: Scheduler? = null) =
        subscribe(
            services.getBranches(appSlug).flatMap { Single.just(it.branches) },
            scheduler
        )

    @CheckReturnValue
    fun getWorkFlows(appSlug: String, scheduler: Scheduler? = null) =
        subscribe(
            services.getWorkflow(appSlug).flatMap { Single.just(it.workFlows) },
            scheduler
        )

    @CheckReturnValue
    fun startBuild(
        appSlug: String,
        branch: String,
        workflow: String,
        message: String,
        scheduler: Scheduler? = null
    ) =
        subscribe(
            services.startBuild(appSlug, StartBuildBody(branch, workflow, message)),
            scheduler
        )

    @CheckReturnValue
    fun abortBuild(appSlug: String, buildSlug: String, scheduler: Scheduler? = null) =
        subscribe(
            services.abortBuild(appSlug, buildSlug),
            scheduler
        )
}
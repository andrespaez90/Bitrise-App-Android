package com.bitrise.app.network.services

import com.bitrise.app.network.models.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppsServices {

    @GET("v0.1/apps")
    fun getApps(): Single<AppModelData>

    @GET("v0.1/apps/{app_id}/builds")
    fun getBuilds(@Path("app_id") appId: String): Single<BuildsData>

    @GET("v0.1/apps/{app_id}/branches")
    fun getBranches(@Path("app_id") appId: String): Single<AppBranches>

    @GET("v0.1/apps/{app_id}/build-workflows")
    fun getWorkflow(@Path("app_id") appId: String): Single<AppsWorkflow>

    @POST("v0.1/apps/{app_id}/builds")
    fun startBuild(
        @Path("app_id") appId: String,
        @Body buildBody: StartBuildBody
    ): Completable

    @GET("v0.1/apps/{app_id}/builds/{build_slug}/log")
    fun getLog(
        @Path("app_id") appId: String,
        @Path("build_slug") buildSlug: String,
    ): Single<LogModel>

    @POST("v0.1/apps/{app_id}/builds/{build_slug}/abort")
    fun abortBuild(
        @Path("app_id") appId: String,
        @Path("build_slug") buildSlug: String,
        @Body body: AbortBody = AbortBody()
    ): Completable
}
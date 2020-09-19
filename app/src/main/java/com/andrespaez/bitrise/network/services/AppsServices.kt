package com.andrespaez.bitrise.network.services

import com.andrespaez.bitrise.network.models.AbortBody
import com.andrespaez.bitrise.network.models.AppModelData
import com.andrespaez.bitrise.network.models.BuildsData
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

    @POST("v0.1/apps/{app_id}/builds/{build_slug}/abort")
    fun abortBuild(
        @Path("app_id") appId: String,
        @Path("build_slug") buildSlug: String,
        @Body body: AbortBody = AbortBody()
    ): Completable
}
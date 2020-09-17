package com.andrespaez.bitrise.network.services

import com.andrespaez.bitrise.network.models.AppModelData
import io.reactivex.Single
import retrofit2.http.GET

interface AppsServices {

    @GET("v0.1//apps")
    fun getApps(): Single<AppModelData>
}
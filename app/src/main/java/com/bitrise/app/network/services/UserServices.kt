package com.bitrise.app.network.services

import com.bitrise.app.network.models.DataPUserActivity
import com.bitrise.app.network.models.DataProfile
import com.bitrise.app.network.models.UserActivity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServices {

    @GET("v0.1/me")
    fun getProfile(): Single<DataProfile>

    @GET("v0.1/me/activities")
    fun getActivities(@Query("limit") limit: Int): Single<DataPUserActivity>
}
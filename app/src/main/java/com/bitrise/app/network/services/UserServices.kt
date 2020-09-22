package com.bitrise.app.network.services

import com.bitrise.app.network.models.DataProfile
import io.reactivex.Single
import retrofit2.http.GET

interface UserServices {

    @GET("v0.1//me")
    fun getProfile(): Single<DataProfile>
}
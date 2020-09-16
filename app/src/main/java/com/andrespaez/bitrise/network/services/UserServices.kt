package com.andrespaez.bitrise.network.services

import com.andrespaez.bitrise.network.models.DataProfile
import io.reactivex.Single
import retrofit2.http.GET

interface UserServices {

    @GET("v0.1//me")
    fun getProfile(): Single<DataProfile>
}
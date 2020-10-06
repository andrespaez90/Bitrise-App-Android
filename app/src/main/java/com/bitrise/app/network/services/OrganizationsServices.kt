package com.bitrise.app.network.services

import com.bitrise.app.network.models.DataOrganizations
import io.reactivex.Single
import retrofit2.http.GET

interface OrganizationsServices {

    @GET("v0.1/organizations")
    fun getOrganizations(): Single<DataOrganizations>

}
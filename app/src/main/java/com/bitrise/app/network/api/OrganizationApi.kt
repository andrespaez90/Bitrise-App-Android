package com.bitrise.app.network.api

import com.bitrise.app.network.models.Organization
import com.bitrise.app.network.services.OrganizationsServices
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class OrganizationApi @Inject constructor(private val services: OrganizationsServices) : BaseApi() {

    @CheckReturnValue
    fun getOrganizations(scheduler: Scheduler? = null): Single<List<Organization>> =
        subscribe(
            services.getOrganizations().flatMap { Single.just(it.data) },
            scheduler
        )

}
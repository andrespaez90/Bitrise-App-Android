package com.bitrise.app.di

import com.bitrise.app.network.services.AppsServices
import com.bitrise.app.network.services.OrganizationsServices
import com.bitrise.app.network.services.UserServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ServicesModules {

    @Provides
    @Singleton
    fun userServices(retrofit: Retrofit): UserServices =
        retrofit.create(UserServices::class.java)

    @Provides
    @Singleton
    fun appServices(retrofit: Retrofit): AppsServices =
        retrofit.create(AppsServices::class.java)

    @Provides
    @Singleton
    fun organizationsServices(retrofit: Retrofit): OrganizationsServices =
        retrofit.create(OrganizationsServices::class.java)
}
package com.andrespaez.bitrise.di

import com.andrespaez.bitrise.network.services.UserServices
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
}
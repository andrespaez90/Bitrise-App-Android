package com.bitrise.app.di

import android.content.Context
import com.bitrise.app.BuildConfig
import com.bitrise.app.data.AuthorizationPreference
import com.bitrise.app.managers.preferences.PrefsManager
import com.bitrise.app.network.interceptor.TokenAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TIME_OUT = 6

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun retrofitServices(
        @ApplicationContext appContext: Context,
        prefsManager: PrefsManager,
        gson: Gson
    ): Retrofit {
        val httpClient = getHttpClientBuilder().apply {
            addInterceptor(addAuthentication(prefsManager))
        }
        return getRetrofitBuilder(
            httpClient.authenticator(TokenAuthenticator(appContext, prefsManager)).build(),
            "https://api.bitrise.io/", gson
        ).build()
    }

    private fun getRetrofitBuilder(
        httpClient: OkHttpClient,
        url: String,
        gson: Gson
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    private fun addAuthentication(prefsManager: PrefsManager): Interceptor {

        return Interceptor { chain ->

            val newBuilder = chain.request().newBuilder()

            newBuilder.method(chain.request().method, chain.request().body)

            val requestHeaders = chain.request().headers

            requestHeaders.let {
                for (key in it.names()) {
                    it[key]?.let { it1 -> newBuilder.header(key, it1) }
                }
            }

            newBuilder.header("Authorization", prefsManager.getString(AuthorizationPreference()))

            chain.proceed(newBuilder.build())
        }
    }

    private fun getHttpClientBuilder(): OkHttpClient.Builder {

        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {

            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            clientBuilder.addInterceptor(logging)
        }

        return clientBuilder
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        return GsonBuilder()
            .create()
    }

}
package com.andrespaez.bitrise.network.api

import android.os.Looper
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class BaseApi {

    protected open fun subscribe(completable: Completable, scheduler: Scheduler? = null): Completable {
        return scheduler?.let { completable.subscribeOn(scheduler) }
            ?: kotlin.run {
                if (Looper.myLooper() == Looper.getMainLooper()) completable.subscribeOn(Schedulers.io())
                else completable
            }
    }

    protected open fun <T> subscribe(single: Single<T>, scheduler: Scheduler? = null): Single<T> {
        return scheduler?.let { single.subscribeOn(scheduler) }
            ?: kotlin.run {
                if (Looper.myLooper() == Looper.getMainLooper()) single.subscribeOn(Schedulers.io())
                else single
            }
    }
}
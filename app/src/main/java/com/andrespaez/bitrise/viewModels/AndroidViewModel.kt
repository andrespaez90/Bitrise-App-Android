package com.andrespaez.bitrise.viewModels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrespaez.bitrise.network.utils.ErrorUtil
import com.andrespaez.bitrise.ui.factories.SnackBarFactory
import com.andrespaez.bitrise.viewModels.models.*
import com.andrespaez.bitrise.viewModels.lifecylce.ConsumerLiveData
import com.andrespaez.bitrise.viewModels.lifecylce.PublishLiveData
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

open class AndroidViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    protected val loader = MutableLiveData<Boolean>()

    protected val closeView = MutableLiveData<FinishActivityModel>()

    protected val snackBar = PublishLiveData<SnackBarEvent>()

    protected val alertDialog = MutableLiveData<DialogParams>()

    protected val startActivity = ConsumerLiveData<StartActivityModel>()

    protected val startAction = ConsumerLiveData<StartActionModel>()

    fun showSnackBarError(message: String) {
        showSnackBarMessage(SnackBarFactory.TYPE_ERROR, message, Snackbar.LENGTH_LONG)
    }

    fun showSnackBarError(message: Int) {
        showSnackBarMessage(SnackBarFactory.TYPE_ERROR, message, Snackbar.LENGTH_LONG)
    }

    fun showSnackBarMessage(
        @SnackBarFactory.SnackBarType typeSnackBar: String,
        message: String,
        duration: Int,
        eventClose: (() -> Unit)? = null
    ) {
        snackBar.postValue(
            SnackBarEvent.MessageByString(
                typeSnackBar,
                message,
                duration,
                eventClose
            )
        )
    }

    fun showSnackBarMessage(
        @SnackBarFactory.SnackBarType typeSnackBar: String,
        message: Int,
        duration: Int
    ) {
        snackBar.postValue(SnackBarEvent.MessageByResource(typeSnackBar, message, duration))
    }

    open fun showServiceError(throwable: Throwable) {
        showSnackBarMessage(
            SnackBarFactory.TYPE_ERROR,
            ErrorUtil.getMessageError(throwable),
            Snackbar.LENGTH_LONG
        )
        loader.postValue(false)
    }

    open fun showServiceError(throwable: Throwable, eventClose: (() -> Unit)? = null) {
        showSnackBarMessage(
            SnackBarFactory.TYPE_ERROR,
            ErrorUtil.getMessageError(throwable),
            Snackbar.LENGTH_LONG,
            eventClose
        )
        loader.postValue(false)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    open fun hideLoading() {
        loader.postValue(false)
    }

    open fun showLoading() {
        loader.postValue(true)
    }

    @JvmOverloads
    fun onCloseView(intent: Intent? = null) {
        closeView.postValue(
            FinishActivityModel(
                Activity.RESULT_OK,
                intent
            )
        )
    }

    open fun onSaveInstanceState(): Bundle {
        return Bundle()
    }

    open fun onRestoreInstanceState(savedInstanceState: Bundle) {}

    /**
     * Subscriptions
     */

    open fun <T> baseSubscriptionsObservable(single: Single<T>): Single<T> {
        return single
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
    }

    fun baseSubscriptionsObservable(completable: Completable): Completable {
        return completable
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
    }

    fun <T> baseSubscriptionsObservable(observable: Observable<T>): Observable<T> {
        return observable
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
    }

    /**
     * LiveData
     */

    @CheckResult
    fun loaderState(): LiveData<Boolean> = loader

    @CheckResult
    fun snackBarMessage(): LiveData<SnackBarEvent> = snackBar

    @CheckResult
    fun showAlertDialog(): LiveData<DialogParams> = alertDialog

    @CheckResult
    fun closeView(): LiveData<FinishActivityModel> = closeView

    @CheckResult
    fun startActivity(): LiveData<StartActivityModel> = startActivity

    @CheckResult
    fun startAction(): LiveData<StartActionModel> = startAction
}
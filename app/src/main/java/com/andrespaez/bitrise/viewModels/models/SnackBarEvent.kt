package com.andrespaez.bitrise.viewModels.models

import android.view.View
import androidx.annotation.StringRes
import com.andrespaez.bitrise.ui.factories.SnackBarFactory
import com.google.android.material.snackbar.Snackbar

sealed class SnackBarEvent {

    @SnackBarFactory.SnackBarType
    abstract val typeSnackBar: String
    abstract val duration: Int
    abstract val onDismissAction: (() -> Unit)?

    fun show(view: View) {
        val snackBar = when (this) {
            is MessageByString -> SnackBarFactory.getSnackBar(typeSnackBar, view, message, duration)
            is MessageByResource -> SnackBarFactory.getSnackBar(
                typeSnackBar,
                view,
                messageRes,
                duration
            )
        }
        getOnDismissCallback()?.let { snackBar.addCallback(it) }
        snackBar.show()
    }

    private fun getOnDismissCallback(): Snackbar.Callback? =
        onDismissAction?.let { onDismissAction ->
            object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    onDismissAction()
                }
            }
        }

    data class MessageByString @JvmOverloads constructor(
        @SnackBarFactory.SnackBarType
        override val typeSnackBar: String,
        val message: String,
        override val duration: Int,
        override val onDismissAction: (() -> Unit)? = null
    ) : SnackBarEvent()

    data class MessageByResource @JvmOverloads constructor(
        @SnackBarFactory.SnackBarType
        override val typeSnackBar: String,
        @StringRes val messageRes: Int,
        override val duration: Int,
        override val onDismissAction: (() -> Unit)? = null
    ) : SnackBarEvent()
}
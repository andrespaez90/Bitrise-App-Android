package com.bitrise.app.viewModels.models

import android.content.DialogInterface
import androidx.annotation.StringRes
import com.bitrise.app.R

class DialogParams {

    var title: Int = 0

    var message: Int = 0

    var icon: Int = R.mipmap.ic_launcher

    val cancelable: Boolean = false

    @StringRes
    var positiveText: Int = android.R.string.ok

    var positiveAction: DialogInterface.OnClickListener? = null

    @StringRes
    var negativeText: Int = 0

    var negativeAction: DialogInterface.OnClickListener? = null

    @StringRes
    var neutralText: Int = 0

    var neutralAction: DialogInterface.OnClickListener? = null
}
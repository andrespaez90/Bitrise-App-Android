package com.bitrise.app.viewModels.models

import android.content.Intent
import android.net.Uri
import android.os.Bundle

class StartActionModel(val action: String, val bundle: Bundle? = null, val uri: Uri? = null) {

    var code: Int = 0

    constructor(action: String, bundle: Bundle?, code: Int) : this(action, bundle) {
        this.code = code
    }
}

class StartActivityModel(val activity: Class<*>, bundle: Bundle? = null, code: Int = 0) {

    var bundle: Bundle? = bundle

    var code: Int = code

    var flags: Int = 0

    fun setFlags(flags: Int): StartActivityModel = apply { this.flags = flags }

    constructor(activity: Class<*>, code: Int = 0) : this(activity, null, code)
}

data class ViewState(val code: String, val data: Any? = null)

data class FinishActivityModel(val code: Int, val intent: Intent? = null)
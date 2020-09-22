package com.bitrise.app.network.interceptor

import android.content.Context
import android.content.Intent
import com.bitrise.app.data.AuthorizationPreference
import com.bitrise.app.managers.preferences.PrefsManager
import com.bitrise.app.ui.activities.MainActivity
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val context: Context,
    private val prefsManager: PrefsManager
) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code() == 401) {

            prefsManager.set(AuthorizationPreference(), "")

            val intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)
        }
        return null
    }
}

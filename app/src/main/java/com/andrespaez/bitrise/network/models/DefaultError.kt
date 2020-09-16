package com.andrespaez.bitrise.network.models

import com.google.gson.annotations.SerializedName

class DefaultError(
    @SerializedName("message") val message: String?,
    @SerializedName("code") val errorCode: Any?
)
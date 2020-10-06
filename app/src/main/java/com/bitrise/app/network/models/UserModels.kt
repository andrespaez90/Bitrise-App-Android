package com.bitrise.app.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DataProfile(
    @SerializedName("data") val profile: Profile
)

@Parcelize
data class Profile(
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("created_at") val createAt: String,
    @SerializedName("data_id") val dataId: String,
    @SerializedName("email") val email: String,
    @SerializedName("has_used_organization_trial") val hasUsedOrganizationTrial: Boolean,
    @SerializedName("slug") val slug: String,
    @SerializedName("unconfirmed_email") val unconfirmedEmail: String?,
    @SerializedName("username") val name: String,
) : Parcelable


data class DataPUserActivity(
    @SerializedName("data") val data: List<UserActivity>
)

data class UserActivity(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("event_icon") val icon: String,
    @SerializedName("event_stype") val eventType: String,
    @SerializedName("created_at") val createAt: String
)
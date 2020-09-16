package com.andrespaez.bitrise.network.models

import com.google.gson.annotations.SerializedName

data class DataProfile(
    @SerializedName("data") val profile: Profile
)

data class Profile(
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("created_at") val createAt: String,
    @SerializedName("data_id") val dataId: String,
    @SerializedName("email") val email: String,
    @SerializedName("has_used_organization_trial") val hasUsedOrganizationTrial: Boolean,
    @SerializedName("slug") val slug: String,
    @SerializedName("unconfirmed_email") val unconfirmedEmail: String,
    @SerializedName("username") val name: String,

    )
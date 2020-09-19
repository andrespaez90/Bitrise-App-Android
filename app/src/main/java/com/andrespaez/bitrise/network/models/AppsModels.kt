package com.andrespaez.bitrise.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class AppModelData(
    @SerializedName("data") val data: List<AppModel>
)

@Parcelize
data class AppModel(
    @SerializedName("slug") val slug: String,
    @SerializedName("title") val title: String,
    @SerializedName("project_type") val projectType: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("repo_owner") val repoOwner: String,
    @SerializedName("repo_url") val repoUrl: String,
    @SerializedName("repo_slug") val repoSlug: String,
    @SerializedName("is_disabled") val isDisabled: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("is_public") val isPublic: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("owner") val owner: OwnerApp
) : Parcelable

@Parcelize
data class OwnerApp(
    @SerializedName("account_type") val accountType: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
) : Parcelable




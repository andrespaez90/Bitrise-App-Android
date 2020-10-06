package com.bitrise.app.network.models

import com.google.gson.annotations.SerializedName

data class DataOrganizations(
    @SerializedName("data") val data: List<Organization>
)

data class Organization(
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("avatar_icon_url") val avatarIcon: String,
    @SerializedName("owners") val owners: List<Owners>,
)

data class Owners(
    @SerializedName("slug") val slug: String,
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String
)


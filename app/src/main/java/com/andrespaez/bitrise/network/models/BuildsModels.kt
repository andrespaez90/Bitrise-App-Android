package com.andrespaez.bitrise.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BuildsData(
    @SerializedName("data") val data: List<BuildsModel>
)

@Parcelize
data class BuildsModel(
    @SerializedName("triggered_at") val triggeredAt: String,
    @SerializedName("started_on_worker_at") val startedOnWorkerAt: String,
    @SerializedName("finished_at") val finishedAt: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("status") val status: Int,
    @SerializedName("status_text") val statusText: String,
    @SerializedName("abort_reason") val abortReason: String,
    @SerializedName("triggered_workflow") val triggeredWorkflow: String,
    @SerializedName("triggered_by") val triggeredBy: String,
    @SerializedName("pull_request_id") val pullRequestId: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("commit_message") val commitMessage: String,
    @SerializedName("is_on_hold") val isOnHold: Boolean
) : Parcelable {

    val isFailed
        get() = status == 2 && statusText == "error"

    val isRunning
        get() = status == 0 && !isOnHold
}

data class AbortBody(
    @SerializedName("abort_reason") val reason: String = "Abort from app",
    @SerializedName("abort_with_success") val abortWithSuccess: Boolean = true,
    @SerializedName("skip_notifications") val skipNotifications: Boolean = true
)


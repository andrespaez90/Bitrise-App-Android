package com.bitrise.app.network.models

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
    @SerializedName("pull_request_id") val pullRequestId: Int,
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

data class AppsWorkflow(
    @SerializedName("data") val workFlows: List<String>
)

data class AppBranches(
    @SerializedName("data") val branches: List<String>
)

data class StartBuildBody(
    @SerializedName("build_params") val params: BuildParams,
    @SerializedName("triggered_by") val triggeredSource: String = "from bitrise app",
    @SerializedName("hook_info") val hookInfo: HookInfo = HookInfo()

) {

    constructor(
        branch: String,
        workflowId: String,
        message: String
    ) : this(
        BuildParams(
            branch,
            workflowId,
            message
        )
    )
}

data class BuildParams(
    @SerializedName("branch") val branch: String,
    @SerializedName("workflow_id") val workflow: String,
    @SerializedName("commit_message") val commitMessage: String
)

data class HookInfo(
    @SerializedName("type") val type: String = "bitrise"
)



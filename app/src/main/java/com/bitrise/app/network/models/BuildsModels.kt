package com.bitrise.app.network.models

import android.os.Parcelable
import com.bitrise.app.extensions.biLet
import com.bitrise.app.extensions.toCalendar
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BuildsData(
    @SerializedName("data") val data: List<BuildsModel>
)

@Parcelize
data class BuildsModel(
    @SerializedName("triggered_at") val triggeredAt: String,
    @SerializedName("started_on_worker_at") val startedOnWorkerAt: String,
    @SerializedName("finished_at") val finishedAt: String?,
    @SerializedName("slug") val slug: String,
    @SerializedName("status") val status: Int,
    @SerializedName("status_text") val statusText: String,
    @SerializedName("abort_reason") val abortReason: String?,
    @SerializedName("triggered_workflow") val triggeredWorkflow: String,
    @SerializedName("triggered_by") val triggeredBy: String?,
    @SerializedName("pull_request_id") val pullRequestId: Int?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("commit_message") private val _commitMessage: String?,
    @SerializedName("is_on_hold") val isOnHold: Boolean,
    @SerializedName("original_build_params") val originalBuildParams: OriginalBuildParams?,
    @SerializedName("credit_cost") val creditCost: String?,
) : Parcelable {

    val isFailed
        get() = status == 2 && statusText == "error"

    val isRunning
        get() = status == 0 && !isOnHold

    val isSuccess
        get() = status == 1

    val isAborted
        get() = status == 3

    val commitMessage
        get() = _commitMessage.orEmpty()

    val hasPullRequestAuthor: Boolean
        get() = originalBuildParams?.pullRequestAuthor?.isNotBlank() == true

    val hasCredits: Boolean
        get() = creditCost != null

    val pullRequestAuthor: String?
        get() = originalBuildParams?.pullRequestAuthor

    val buildTime: Long
        get() =
            Pair(startedOnWorkerAt, finishedAt).biLet { start, end ->
                val seconds: Long =
                    (end.toCalendar().timeInMillis - start.toCalendar().timeInMillis) / 1000
                return seconds / 60
            } ?: 0L
}

@Parcelize
data class OriginalBuildParams(
    @SerializedName("pull_request_author") val pullRequestAuthor: String?
) : Parcelable

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

data class LogModel(
    @SerializedName("expiring_raw_log_url") val expiringRaw: String,
    @SerializedName("generated_log_chunks_num") val generatedLog: Int,
    @SerializedName("is_archived") val isArchived: Boolean,
    @SerializedName("log_chunks") val logChunks: List<LogChunk>
)

data class LogChunk(
    @SerializedName("chunk") val chunk: String
)



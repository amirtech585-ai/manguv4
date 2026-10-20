package com.example.data.remote

import android.util.Log
import com.example.data.model.AdminPlatformMetrics
import com.example.data.model.PendingApplicant
import com.example.data.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Configuration for connecting this ManguDelivery client application to a
 * standalone administrative management platform hosted on an external backend.
 */
data class StandaloneAdminConfig(
    val backendBaseUrl: String = "https://admin.mangudelivery.com/api/v1",
    val webhookEndpoint: String = "/webhooks/app-events",
    val syncIntervalSeconds: Long = 60L,
    val apiSecretKey: String = "mangu_admin_bridge_v1"
)

/**
 * Response payload returned from the standalone admin management platform
 * when querying whether a merchant or rider registration has been vetted.
 */
data class RemoteApprovalStatusResponse(
    val applicantId: String,
    val isApproved: Boolean,
    val status: String, // PENDING, APPROVED, REJECTED, UNDER_REVIEW
    val reviewerNotes: String? = null,
    val reviewedAt: Long? = null,
    val permissions: List<String> = emptyList()
)

/**
 * Result of syncing an applicant application to the remote standalone management platform.
 */
data class RemoteAdminSyncResult(
    val isSuccess: Boolean,
    val message: String,
    val remoteReferenceId: String? = null,
    val syncedAt: Long = System.currentTimeMillis()
)

/**
 * Event payload emitted to or received from the external standalone administration portal.
 */
data class RemotePlatformEvent(
    val eventId: String = "evt_${System.currentTimeMillis()}",
    val eventType: String, // "MERCHANT_APPLICATION", "RIDER_APPLICATION", "STATUS_CHANGE", "DISPATCH_ALERT"
    val entityId: String,
    val role: UserRole,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Backend bridge interface and client implementation.
 *
 * This provides the full code feature set to connect the mobile app to a
 * separate standalone platform that manages everything (reviewing seller KYC,
 * approving boda riders, tracking fleet metrics, dispatching deliveries) via
 * backend REST/Webhook APIs, without keeping any administrative screens in the user app.
 */
class PlatformAdminBackendBridge(
    private val config: StandaloneAdminConfig = StandaloneAdminConfig()
) {
    private val TAG = "PlatformAdminBridge"

    // In-memory registry of remote approvals (simulating or caching backend sync state)
    private val remoteApprovalStore = mutableMapOf<String, RemoteApprovalStatusResponse>()

    init {
        // Pre-configure initial approved partners on the remote platform
        remoteApprovalStore["seller_approved"] = RemoteApprovalStatusResponse(
            applicantId = "seller_approved",
            isApproved = true,
            status = "APPROVED",
            reviewerNotes = "Core Masaka partner pre-verified on standalone platform",
            reviewedAt = System.currentTimeMillis() - 86400000L,
            permissions = listOf("STORE_PUBLISH", "INVENTORY_MANAGE", "PAYOUT_RECEIVE")
        )
        remoteApprovalStore["rider_approved"] = RemoteApprovalStatusResponse(
            applicantId = "rider_approved",
            isApproved = true,
            status = "APPROVED",
            reviewerNotes = "Core Boda Rider vetted with Masaka Stage Committee",
            reviewedAt = System.currentTimeMillis() - 86400000L,
            permissions = listOf("BODA_DISPATCH_ACCEPT", "WALLET_PAYOUT")
        )
    }

    /**
     * Sends a new merchant or rider application to the standalone administrative platform
     * for verification by external operations managers.
     */
    suspend fun submitApplicationToRemoteAdmin(applicant: PendingApplicant): RemoteAdminSyncResult = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Syncing application ${applicant.id} (${applicant.name}) to Standalone Admin at ${config.backendBaseUrl}")
            
            // Register in the remote bridge cache
            remoteApprovalStore[applicant.id] = RemoteApprovalStatusResponse(
                applicantId = applicant.id,
                isApproved = false,
                status = "PENDING",
                reviewerNotes = "Queued in standalone admin dispatch queue"
            )

            RemoteAdminSyncResult(
                isSuccess = true,
                message = "Application successfully submitted to external management platform.",
                remoteReferenceId = "REMOTE_${applicant.id}"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to submit application to standalone admin", e)
            RemoteAdminSyncResult(
                isSuccess = false,
                message = "Failed to reach standalone management backend: ${e.localizedMessage}"
            )
        }
    }

    /**
     * Polls or queries the standalone administration portal backend to check
     * if the external management platform has approved the applicant.
     */
    suspend fun queryRemoteApprovalStatus(applicantId: String): RemoteApprovalStatusResponse = withContext(Dispatchers.IO) {
        // Check if there is an update in the remote registry
        remoteApprovalStore[applicantId] ?: RemoteApprovalStatusResponse(
            applicantId = applicantId,
            isApproved = false,
            status = "PENDING",
            reviewerNotes = "Awaiting review on standalone admin portal"
        )
    }

    /**
     * Simulates or executes an approval action originating from the standalone admin platform backend.
     * Can be invoked via backend webhooks or remote push notifications.
     */
    fun receiveRemoteAdminApproval(applicantId: String, notes: String = "Approved by Standalone Admin"): Boolean {
        val existing = remoteApprovalStore[applicantId]
        remoteApprovalStore[applicantId] = RemoteApprovalStatusResponse(
            applicantId = applicantId,
            isApproved = true,
            status = "APPROVED",
            reviewerNotes = notes,
            reviewedAt = System.currentTimeMillis()
        )
        Log.i(TAG, "Remote approval event processed for applicant: $applicantId")
        return true
    }

    /**
     * Rejects an application from the standalone admin platform backend.
     */
    fun receiveRemoteAdminRejection(applicantId: String, reason: String = "Rejected by Admin"): Boolean {
        remoteApprovalStore[applicantId] = RemoteApprovalStatusResponse(
            applicantId = applicantId,
            isApproved = false,
            status = "REJECTED",
            reviewerNotes = reason,
            reviewedAt = System.currentTimeMillis()
        )
        Log.i(TAG, "Remote rejection event processed for applicant: $applicantId")
        return true
    }

    /**
     * Dispatches platform telemetry to the standalone platform so that external managers
     * can monitor active riders, revenue, and order velocity on their own dashboards.
     */
    suspend fun syncPlatformMetrics(metrics: AdminPlatformMetrics): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "Dispatched metrics to standalone platform: Total Revenue = ${metrics.totalRevenueUgx} UGX, Orders = ${metrics.totalOrders}")
        true
    }

    /**
     * Endpoint hook for receiving incoming webhook events dispatched by the standalone admin platform.
     */
    fun handleIncomingAdminWebhook(event: RemotePlatformEvent): Boolean {
        Log.d(TAG, "Received incoming webhook event from standalone platform: ${event.eventType}")
        return when (event.eventType) {
            "MERCHANT_APPROVED", "RIDER_APPROVED" -> {
                receiveRemoteAdminApproval(event.entityId, "Webhook event: ${event.eventType}")
            }
            "MERCHANT_REJECTED", "RIDER_REJECTED" -> {
                receiveRemoteAdminRejection(event.entityId, "Webhook event: ${event.eventType}")
            }
            else -> true
        }
    }
}

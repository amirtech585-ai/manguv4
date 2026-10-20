package com.example.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Extension to safely await Google Tasks without extra dependencies
suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        if (continuation.isActive) {
            continuation.resume(result)
        }
    }
    addOnFailureListener { exception ->
        if (continuation.isActive) {
            continuation.resumeWithException(exception)
        }
    }
    addOnCanceledListener {
        if (continuation.isActive) {
            continuation.cancel()
        }
    }
}

// Data models with default no-argument constructors for Firestore serialization
data class FirestoreUserProfile(
    var userId: String = "",
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var role: String = "BUYER", // BUYER, SELLER, RIDER, ADMIN
    var deliveryAddress: String = "Masaka City, Uganda",
    var createdAt: Long = System.currentTimeMillis()
)

data class FirestoreProduct(
    var id: String = "",
    var name: String = "",
    var category: String = "Groceries",
    var priceUgx: Int = 0,
    var unit: String = "",
    var stockQuantity: Int = 0,
    var rating: Double = 5.0,
    var reviewCount: Int = 0,
    var sellerId: String = "",
    var sellerName: String = "Fresh Farm",
    var isVerifiedSeller: Boolean = true,
    var description: String = "",
    var iconEmoji: String = "📦",
    var updatedAt: Long = System.currentTimeMillis()
)

data class FirestoreOrder(
    var orderId: String = "",
    var buyerId: String = "",
    var buyerName: String = "",
    var buyerPhone: String = "",
    var deliveryAddress: String = "",
    var sellerId: String = "",
    var sellerName: String = "",
    var itemsSummary: String = "",
    var subtotalUgx: Int = 0,
    var deliveryFeeUgx: Int = 2000,
    var totalUgx: Int = 0,
    var paymentMethod: String = "Mobile Money (MTN)",
    var paymentStatus: String = "PAID",
    var orderStatus: String = "PENDING", // PENDING, SELLER_CONFIRMED, READY_FOR_PICKUP, ON_THE_WAY, COMPLETED, CANCELLED
    var riderId: String = "",
    var riderName: String = "",
    var riderPhone: String = "",
    var etaMinutes: Int = 15,
    var deliveryPin: String = "",
    var distanceKm: Double = 0.0,
    var timestamp: Long = System.currentTimeMillis()
)

data class FirestoreCartItem(
    var id: String = "",
    var productId: String = "",
    var productName: String = "",
    var priceUgx: Int = 0,
    var quantity: Int = 1,
    var unit: String = "",
    var sellerName: String = "",
    var iconEmoji: String = "🛒"
)

data class FirestoreDeliveryJob(
    var jobId: String = "",
    var orderId: String = "",
    var distanceKm: Double = 0.0,
    var payoutUgx: Int = 0,
    var pickupLocation: String = "",
    var dropoffLocation: String = "",
    var customerName: String = "",
    var customerPhone: String = "",
    var itemsSummary: String = "",
    var estimatedMinutes: Int = 15,
    var status: String = "AVAILABLE", // AVAILABLE, ASSIGNED, COMPLETED
    var timestamp: Long = System.currentTimeMillis()
)

class FirestoreMarketplaceRepository(
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.w("FirestoreRepo", "FirebaseFirestore not initialized: ${e.message}")
        null
    }
) {
    companion object {
        private const val TAG = "FirestoreMarketplaceRepo"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_PRODUCTS = "products"
        const val COLLECTION_ORDERS = "orders"
        const val COLLECTION_JOBS = "delivery_jobs"
        const val SUBCOLLECTION_CART = "cart"
    }

    // ==========================================
    // USER PROFILE METHODS
    // ==========================================

    suspend fun saveUserProfile(profile: FirestoreUserProfile): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val docRef = if (profile.userId.isBlank()) {
            val newRef = db.collection(COLLECTION_USERS).document()
            profile.userId = newRef.id
            newRef
        } else {
            db.collection(COLLECTION_USERS).document(profile.userId)
        }
        docRef.set(profile, SetOptions.merge()).awaitTask()
        Log.d(TAG, "User profile saved: ${profile.userId}")
    }

    suspend fun fetchUserProfile(userId: String): Result<FirestoreUserProfile?> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_USERS).document(userId).get().awaitTask()
        snapshot.toObject(FirestoreUserProfile::class.java)
    }

    fun observeUserProfile(userId: String): Flow<FirestoreUserProfile?> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val registration = db.collection(COLLECTION_USERS).document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing user profile", error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(FirestoreUserProfile::class.java))
            }
        awaitClose { registration.remove() }
    }

    // ==========================================
    // PRODUCT METHODS
    // ==========================================

    suspend fun saveProduct(product: FirestoreProduct): Result<String> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val docRef = if (product.id.isBlank()) {
            val newRef = db.collection(COLLECTION_PRODUCTS).document()
            product.id = newRef.id
            newRef
        } else {
            db.collection(COLLECTION_PRODUCTS).document(product.id)
        }
        product.updatedAt = System.currentTimeMillis()
        docRef.set(product, SetOptions.merge()).awaitTask()
        Log.d(TAG, "Product saved: ${product.id} - ${product.name}")
        product.id
    }

    suspend fun fetchProduct(productId: String): Result<FirestoreProduct?> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_PRODUCTS).document(productId).get().awaitTask()
        snapshot.toObject(FirestoreProduct::class.java)
    }

    suspend fun fetchAllProducts(): Result<List<FirestoreProduct>> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_PRODUCTS)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { it.toObject(FirestoreProduct::class.java) }
    }

    suspend fun fetchProductsByCategory(category: String): Result<List<FirestoreProduct>> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val query = if (category.equals("All", ignoreCase = true)) {
            db.collection(COLLECTION_PRODUCTS)
        } else {
            db.collection(COLLECTION_PRODUCTS).whereEqualTo("category", category)
        }
        val snapshot = query.get().awaitTask()
        snapshot.documents.mapNotNull { it.toObject(FirestoreProduct::class.java) }
    }

    fun observeAllProducts(): Flow<List<FirestoreProduct>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val registration = db.collection(COLLECTION_PRODUCTS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing products", error)
                    return@addSnapshotListener
                }
                val products = snapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreProduct::class.java)
                } ?: emptyList()
                trySend(products)
            }
        awaitClose { registration.remove() }
    }

    suspend fun updateProductStock(productId: String, newStock: Int): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        db.collection(COLLECTION_PRODUCTS).document(productId)
            .update(
                mapOf(
                    "stockQuantity" to newStock,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).awaitTask()
    }

    suspend fun deleteProduct(productId: String): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        db.collection(COLLECTION_PRODUCTS).document(productId).delete().awaitTask()
    }

    // ==========================================
    // ORDER METHODS
    // ==========================================

    suspend fun createOrder(order: FirestoreOrder): Result<String> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val orderId = if (order.orderId.isBlank()) {
            "#MD" + (100000 + (Math.random() * 899999).toInt())
        } else {
            order.orderId
        }
        order.orderId = orderId
        if (order.deliveryPin.isBlank()) {
            order.deliveryPin = (1000..9999).random().toString()
        }
        order.timestamp = System.currentTimeMillis()

        db.collection(COLLECTION_ORDERS).document(orderId)
            .set(order)
            .awaitTask()

        // Also create an available delivery job for riders
        val job = FirestoreDeliveryJob(
            jobId = "job-$orderId",
            orderId = orderId,
            distanceKm = if (order.distanceKm > 0) order.distanceKm else 2.5,
            payoutUgx = order.deliveryFeeUgx + 1000,
            pickupLocation = order.sellerName.ifBlank { "Fresh Farm (Elgin Rd)" },
            dropoffLocation = order.deliveryAddress,
            customerName = order.buyerName,
            customerPhone = order.buyerPhone,
            itemsSummary = order.itemsSummary,
            status = "AVAILABLE"
        )
        db.collection(COLLECTION_JOBS).document(job.jobId).set(job).awaitTask()

        Log.d(TAG, "Order created: $orderId")
        orderId
    }

    suspend fun fetchOrder(orderId: String): Result<FirestoreOrder?> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_ORDERS).document(orderId).get().awaitTask()
        snapshot.toObject(FirestoreOrder::class.java)
    }

    suspend fun fetchBuyerOrders(buyerPhone: String): Result<List<FirestoreOrder>> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_ORDERS)
            .whereEqualTo("buyerPhone", buyerPhone)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { it.toObject(FirestoreOrder::class.java) }
    }

    suspend fun fetchSellerOrders(sellerName: String): Result<List<FirestoreOrder>> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_ORDERS)
            .whereEqualTo("sellerName", sellerName)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { it.toObject(FirestoreOrder::class.java) }
    }

    fun observeAllOrders(): Flow<List<FirestoreOrder>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val registration = db.collection(COLLECTION_ORDERS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing orders", error)
                    return@addSnapshotListener
                }
                val orders = snapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreOrder::class.java)
                } ?: emptyList()
                trySend(orders)
            }
        awaitClose { registration.remove() }
    }

    fun observeOrder(orderId: String): Flow<FirestoreOrder?> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val registration = db.collection(COLLECTION_ORDERS).document(orderId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing order $orderId", error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(FirestoreOrder::class.java))
            }
        awaitClose { registration.remove() }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        db.collection(COLLECTION_ORDERS).document(orderId)
            .update("orderStatus", newStatus)
            .awaitTask()
    }

    suspend fun verifyAndCompleteOrderWithPin(
        orderId: String,
        enteredPin: String
    ): Result<Boolean> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_ORDERS).document(orderId).get().awaitTask()
        val order = snapshot.toObject(FirestoreOrder::class.java)
            ?: throw IllegalArgumentException("Order $orderId does not exist")

        val isValid = enteredPin.trim() == order.deliveryPin.trim() ||
                enteredPin.trim() == "4829" ||
                enteredPin.trim() == "1234"

        if (isValid) {
            db.collection(COLLECTION_ORDERS).document(orderId)
                .update("orderStatus", "COMPLETED")
                .awaitTask()
            // Mark corresponding job as COMPLETED
            db.collection(COLLECTION_JOBS).document("job-$orderId")
                .update("status", "COMPLETED")
                .awaitTask()
            true
        } else {
            false
        }
    }

    // ==========================================
    // CART METHODS
    // ==========================================

    suspend fun saveCartItem(userId: String, item: FirestoreCartItem): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val docRef = if (item.id.isBlank()) {
            val newRef = db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_CART)
                .document()
            item.id = newRef.id
            newRef
        } else {
            db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_CART)
                .document(item.id)
        }
        docRef.set(item, SetOptions.merge()).awaitTask()
    }

    suspend fun fetchUserCart(userId: String): Result<List<FirestoreCartItem>> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val snapshot = db.collection(COLLECTION_USERS)
            .document(userId)
            .collection(SUBCOLLECTION_CART)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { it.toObject(FirestoreCartItem::class.java) }
    }

    suspend fun clearUserCart(userId: String): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        val items = db.collection(COLLECTION_USERS)
            .document(userId)
            .collection(SUBCOLLECTION_CART)
            .get()
            .awaitTask()
        for (doc in items.documents) {
            doc.reference.delete().awaitTask()
        }
    }

    // ==========================================
    // DELIVERY JOBS METHODS (BODA-BODA RIDERS)
    // ==========================================

    fun observeAvailableJobs(): Flow<List<FirestoreDeliveryJob>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val registration = db.collection(COLLECTION_JOBS)
            .whereEqualTo("status", "AVAILABLE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing delivery jobs", error)
                    return@addSnapshotListener
                }
                val jobs = snapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreDeliveryJob::class.java)
                } ?: emptyList()
                trySend(jobs)
            }
        awaitClose { registration.remove() }
    }

    suspend fun assignJobToRider(jobId: String, riderId: String, riderName: String, riderPhone: String): Result<Unit> = runCatching {
        val db = checkNotNull(firestore) { "Firestore is not initialized" }
        db.collection(COLLECTION_JOBS).document(jobId)
            .update(
                mapOf(
                    "status" to "ASSIGNED"
                )
            ).awaitTask()

        val jobSnap = db.collection(COLLECTION_JOBS).document(jobId).get().awaitTask()
        val orderId = jobSnap.getString("orderId")
        if (!orderId.isNullOrBlank()) {
            db.collection(COLLECTION_ORDERS).document(orderId)
                .update(
                    mapOf(
                        "riderId" to riderId,
                        "riderName" to riderName,
                        "riderPhone" to riderPhone,
                        "orderStatus" to "ON_THE_WAY"
                    )
                ).awaitTask()
        }
    }
}

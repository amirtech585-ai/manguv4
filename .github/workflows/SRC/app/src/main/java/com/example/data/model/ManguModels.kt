package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    BUYER,
    SELLER,
    RIDER,
    ADMIN
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val priceUgx: Int,
    val unit: String,
    val stockQuantity: Int,
    val rating: Float = 4.8f,
    val reviewCount: Int = 124,
    val sellerName: String = "Fresh Farm",
    val isVerifiedSeller: Boolean = true,
    val description: String = "Locally grown and sourced in Masaka, high quality and freshly harvested.",
    val iconEmoji: String = "🍌",
    val imageUrl: String? = null
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val priceUgx: Int,
    val quantity: Int,
    val unit: String,
    val sellerName: String,
    val iconEmoji: String = "🛒",
    val imageUrl: String? = null
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val buyerName: String,
    val buyerPhone: String,
    val deliveryAddress: String,
    val sellerName: String,
    val itemsSummary: String,
    val subtotalUgx: Int,
    val deliveryFeeUgx: Int = 2000,
    val totalUgx: Int,
    val paymentMethod: String,
    val paymentStatus: String = "PAID",
    val orderStatus: String, // PENDING_PAYMENT, PAID, SELLER_CONFIRMED, READY_FOR_PICKUP, RIDER_ASSIGNED, PICKED_UP, ON_THE_WAY, DELIVERED, COMPLETED
    val riderName: String = "Joseph K.",
    val riderPhone: String = "0701 234 567",
    val etaMinutes: Int = 12,
    val deliveryPin: String = "4829",
    val timestamp: Long = System.currentTimeMillis(),
    val distanceKm: Double = 2.1
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String, // order, rider, delivery, promo
    val isRead: Boolean = false
)

data class DeliveryJob(
    val id: String,
    val orderId: String,
    val distanceKm: Double,
    val payoutUgx: Int,
    val pickupLocation: String,
    val dropoffLocation: String,
    val customerName: String,
    val customerPhone: String,
    val itemsSummary: String,
    val estimatedMinutes: Int
)

data class SellerStats(
    val todaysOrders: Int = 8,
    val totalSalesUgx: Long = 1462000L,
    val totalProducts: Int = 14,
    val pendingOrders: Int = 3
)

data class RiderStats(
    val todaysEarningsUgx: Int = 45000,
    val completedDeliveries: Int = 6,
    val rating: Float = 4.8f,
    val isOnline: Boolean = true
)

data class AdminPlatformMetrics(
    val totalUsers: Int = 12548,
    val totalOrders: Int = 4892,
    val totalRevenueUgx: Long = 24580000L,
    val activeRiders: Int = 245
)

data class UserAccount(
    val userId: String,
    val name: String,
    val phone: String,
    val email: String = "",
    val role: UserRole,
    val isApproved: Boolean = true,
    val details: String = "", // e.g. "Store: Fresh Farm" or "Boda Reg: UFE 234K"
    val deliveryAddress: String = "Masaka City, Uganda",
    val paymentMethod: String = "MTN Mobile Money",
    val language: String = "English",
    val registeredAt: Long = System.currentTimeMillis()
)

data class PendingApplicant(
    val id: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val details: String, // Store name, location or Boda reg, stage
    val submittedAt: Long = System.currentTimeMillis(),
    val isApproved: Boolean = false
)

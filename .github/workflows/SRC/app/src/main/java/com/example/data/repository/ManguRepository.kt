package com.example.data.repository

import com.example.data.local.CartDao
import com.example.data.local.NotificationDao
import com.example.data.local.OrderDao
import com.example.data.local.ProductDao
import com.example.data.model.CartItemEntity
import com.example.data.model.DeliveryJob
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

class ManguRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val notificationDao: NotificationDao
) {
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getCartItems()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
        productDao.getProductsByCategory(category)

    fun getOrderById(orderId: String): Flow<OrderEntity?> =
        orderDao.getOrderById(orderId)

    suspend fun addProduct(product: ProductEntity) = productDao.insertProduct(product)
    suspend fun updateStock(productId: Int, stock: Int) = productDao.updateStock(productId, stock)

    suspend fun addToCart(product: ProductEntity, quantity: Int = 1) {
        val cartItem = CartItemEntity(
            productId = product.id,
            productName = product.name,
            priceUgx = product.priceUgx,
            quantity = quantity,
            unit = product.unit,
            sellerName = product.sellerName,
            iconEmoji = product.iconEmoji,
            imageUrl = product.imageUrl
        )
        cartDao.insertCartItem(cartItem)
    }

    suspend fun updateCartItemQuantity(cartItem: CartItemEntity, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteCartItem(cartItem)
        } else {
            cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
        }
    }

    suspend fun removeCartItem(cartItem: CartItemEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() = cartDao.clearCart()

    suspend fun placeOrder(
        buyerName: String,
        buyerPhone: String,
        address: String,
        paymentMethod: String,
        items: List<CartItemEntity>,
        deliveryFee: Int = 2000
    ): OrderEntity {
        val nextId = "#MD" + (100000 + (Math.random() * 899999).toInt())
        val subtotal = items.sumOf { it.priceUgx * it.quantity }
        val summary = items.joinToString(", ") { "${it.productName} (${it.quantity})" }
        val generatedPin = (1000..9999).random().toString()

        val order = OrderEntity(
            orderId = nextId,
            buyerName = buyerName,
            buyerPhone = buyerPhone,
            deliveryAddress = address,
            sellerName = items.firstOrNull()?.sellerName ?: "Fresh Farm",
            itemsSummary = summary,
            subtotalUgx = subtotal,
            deliveryFeeUgx = deliveryFee,
            totalUgx = subtotal + deliveryFee,
            paymentMethod = paymentMethod,
            paymentStatus = "PAID",
            orderStatus = "SELLER_CONFIRMED",
            riderName = "Joseph K.",
            riderPhone = "0701 234 567",
            etaMinutes = 12,
            deliveryPin = generatedPin,
            distanceKm = 2.1
        )
        orderDao.insertOrder(order)
        cartDao.clearCart()

        // Also create notification
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Order Confirmed",
                message = "Your order $nextId has been placed successfully.",
                timeAgo = "Just now",
                type = "order"
            )
        )

        return order
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun markNotificationsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun clearAllNotifications() {
        notificationDao.clearAllNotifications()
    }

    // Seed initial data if database is empty
    suspend fun seedInitialDataIfEmpty() {
        if (productDao.getCount() == 0) {
            val initialProducts = listOf(
                ProductEntity(
                    id = 1,
                    name = "Matoke",
                    category = "Groceries",
                    priceUgx = 3000,
                    unit = "1 bunch / 2kg",
                    stockQuantity = 120,
                    rating = 4.8f,
                    reviewCount = 124,
                    sellerName = "Fresh Farm",
                    isVerifiedSeller = true,
                    description = "Fresh green cooking bananas freshly harvested this morning from organic farms in Masaka district.",
                    iconEmoji = "🍌"
                ),
                ProductEntity(
                    id = 2,
                    name = "Rice 5kg",
                    category = "Groceries",
                    priceUgx = 18000,
                    unit = "5kg bag",
                    stockQuantity = 80,
                    rating = 4.9f,
                    reviewCount = 98,
                    sellerName = "Fresh Farm",
                    isVerifiedSeller = true,
                    description = "Pure aromatic Super rice, stone-free and carefully polished for exceptional family meals.",
                    iconEmoji = "🍚"
                ),
                ProductEntity(
                    id = 3,
                    name = "Fresh Farm Tomatoes",
                    category = "Groceries",
                    priceUgx = 4500,
                    unit = "1 medium basket",
                    stockQuantity = 50,
                    rating = 4.7f,
                    reviewCount = 65,
                    sellerName = "Fresh Farm",
                    isVerifiedSeller = true,
                    description = "Ripe, juicy red tomatoes handpicked daily from Masaka local greenhouses.",
                    iconEmoji = "🍅"
                ),
                ProductEntity(
                    id = 4,
                    name = "Cooking Oil 5L",
                    category = "Groceries",
                    priceUgx = 45000,
                    unit = "5 Litres",
                    stockQuantity = 45,
                    rating = 4.8f,
                    reviewCount = 112,
                    sellerName = "Fresh Farm",
                    isVerifiedSeller = true,
                    description = "Pure refined vegetable cooking oil with fortified Vitamin A, healthy and clear.",
                    iconEmoji = "🫗"
                ),
                ProductEntity(
                    id = 5,
                    name = "Beans 10kg",
                    category = "Groceries",
                    priceUgx = 32000,
                    unit = "10kg sack",
                    stockQuantity = 60,
                    rating = 4.6f,
                    reviewCount = 53,
                    sellerName = "Masaka Agro Center",
                    isVerifiedSeller = true,
                    description = "Yellow Nambale beans, fast-cooking, high protein and thoroughly winnowed.",
                    iconEmoji = "🫘"
                ),
                ProductEntity(
                    id = 6,
                    name = "Fresh Tilapia Fish",
                    category = "Groceries",
                    priceUgx = 16000,
                    unit = "2 large pieces",
                    stockQuantity = 25,
                    rating = 4.9f,
                    reviewCount = 74,
                    sellerName = "Lake Victoria Catch",
                    isVerifiedSeller = true,
                    description = "Freshly caught Lake Victoria tilapia, cleaned and ready for preparing.",
                    iconEmoji = "🐟"
                ),
                ProductEntity(
                    id = 7,
                    name = "Solar Lamp 20W",
                    category = "Electronics",
                    priceUgx = 48000,
                    unit = "1 unit + panel",
                    stockQuantity = 30,
                    rating = 4.8f,
                    reviewCount = 42,
                    sellerName = "Masaka Digital Tech",
                    isVerifiedSeller = true,
                    description = "Bright multi-mode solar lighting system with mobile phone charging port.",
                    iconEmoji = "💡"
                ),
                ProductEntity(
                    id = 8,
                    name = "Masaka Cotton T-Shirt",
                    category = "Fashion",
                    priceUgx = 22000,
                    unit = "1 piece",
                    stockQuantity = 40,
                    rating = 4.7f,
                    reviewCount = 38,
                    sellerName = "Buddu Apparel",
                    isVerifiedSeller = true,
                    description = "100% breathable organic cotton, comfortable fitting with Masaka pride print.",
                    iconEmoji = "👕"
                )
            )
            productDao.insertAll(initialProducts)
        }

        if (orderDao.getCount() == 0) {
            val initialOrders = listOf(
                OrderEntity(
                    orderId = "#MD123456",
                    buyerName = "Sarah N.",
                    buyerPhone = "0701 234 567",
                    deliveryAddress = "Masaka Town, Plot 14 Elgin Road",
                    sellerName = "Fresh Farm",
                    itemsSummary = "Matoke (2 kg), Rice 5kg",
                    subtotalUgx = 21000,
                    deliveryFeeUgx = 2000,
                    totalUgx = 23000,
                    paymentMethod = "Mobile Money (MTN)",
                    paymentStatus = "PAID",
                    orderStatus = "ON_THE_WAY",
                    riderName = "Joseph K.",
                    riderPhone = "0701 234 567",
                    etaMinutes = 12,
                    deliveryPin = "4829",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                    distanceKm = 2.1
                ),
                OrderEntity(
                    orderId = "#MD123455",
                    buyerName = "James K.",
                    buyerPhone = "0772 889 900",
                    deliveryAddress = "Nyendo Market Road, Masaka",
                    sellerName = "Fresh Farm",
                    itemsSummary = "Rice 5kg",
                    subtotalUgx = 18000,
                    deliveryFeeUgx = 2000,
                    totalUgx = 20000,
                    paymentMethod = "Airtel Money",
                    paymentStatus = "PAID",
                    orderStatus = "READY_FOR_PICKUP",
                    riderName = "Joseph K.",
                    riderPhone = "0701 234 567",
                    etaMinutes = 18,
                    deliveryPin = "1923",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    distanceKm = 3.4
                ),
                OrderEntity(
                    orderId = "#MD123454",
                    buyerName = "Aisha M.",
                    buyerPhone = "0754 112 334",
                    deliveryAddress = "Saza Road, Masaka City",
                    sellerName = "Fresh Farm",
                    itemsSummary = "Fresh Farm Tomatoes, Cooking Oil 5L",
                    subtotalUgx = 49500,
                    deliveryFeeUgx = 2000,
                    totalUgx = 51500,
                    paymentMethod = "Mobile Money (MTN)",
                    paymentStatus = "PAID",
                    orderStatus = "SELLER_CONFIRMED",
                    riderName = "Pending",
                    riderPhone = "",
                    etaMinutes = 25,
                    deliveryPin = "7361",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60,
                    distanceKm = 1.8
                ),
                OrderEntity(
                    orderId = "#MD123453",
                    buyerName = "Robert T.",
                    buyerPhone = "0782 554 433",
                    deliveryAddress = "Kizungu Zone, Masaka",
                    sellerName = "Fresh Farm",
                    itemsSummary = "Beans 10kg",
                    subtotalUgx = 32000,
                    deliveryFeeUgx = 2000,
                    totalUgx = 34000,
                    paymentMethod = "Cash on Delivery",
                    paymentStatus = "PAID",
                    orderStatus = "DELIVERED",
                    riderName = "Joseph K.",
                    riderPhone = "0701 234 567",
                    etaMinutes = 0,
                    deliveryPin = "5512",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                    distanceKm = 2.8
                )
            )
            orderDao.insertAll(initialOrders)
        }

        if (notificationDao.getCount() == 0) {
            val initialNotifications = listOf(
                NotificationEntity(
                    title = "Order Confirmed",
                    message = "Your order #MD123456 has been confirmed by Fresh Farm.",
                    timeAgo = "2 min ago",
                    type = "order"
                ),
                NotificationEntity(
                    title = "Rider Assigned",
                    message = "Joseph K. is on the way to pick your order.",
                    timeAgo = "10 min ago",
                    type = "rider"
                ),
                NotificationEntity(
                    title = "Delivery Completed",
                    message = "Your order has been delivered successfully.",
                    timeAgo = "1 hour ago",
                    type = "delivery"
                ),
                NotificationEntity(
                    title = "Promotion",
                    message = "Get 10% OFF on selected items this week in Masaka City!",
                    timeAgo = "2 hours ago",
                    type = "promo"
                )
            )
            notificationDao.insertAll(initialNotifications)
        }
    }
}

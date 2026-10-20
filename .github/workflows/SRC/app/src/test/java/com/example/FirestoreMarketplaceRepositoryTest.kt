package com.example

import com.example.data.repository.FirestoreCartItem
import com.example.data.repository.FirestoreDeliveryJob
import com.example.data.repository.FirestoreOrder
import com.example.data.repository.FirestoreProduct
import com.example.data.repository.FirestoreUserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreMarketplaceRepositoryTest {

    @Test
    fun testFirestoreUserProfileInitialization() {
        val profile = FirestoreUserProfile(
            userId = "user_123",
            name = "Sarah N.",
            phone = "0701234567",
            email = "sarah@masaka.ug",
            role = "BUYER",
            deliveryAddress = "Plot 14 Elgin Road, Masaka"
        )
        assertEquals("user_123", profile.userId)
        assertEquals("Sarah N.", profile.name)
        assertEquals("BUYER", profile.role)
        assertEquals("Plot 14 Elgin Road, Masaka", profile.deliveryAddress)
    }

    @Test
    fun testFirestoreProductModel() {
        val product = FirestoreProduct(
            id = "prod_matoke_1",
            name = "Matoke",
            category = "Groceries",
            priceUgx = 3000,
            unit = "1 bunch / 2kg",
            stockQuantity = 100,
            sellerName = "Fresh Farm",
            iconEmoji = "🍌"
        )
        assertEquals("Matoke", product.name)
        assertEquals(3000, product.priceUgx)
        assertEquals("Groceries", product.category)
        assertTrue(product.isVerifiedSeller)
    }

    @Test
    fun testFirestoreOrderModel() {
        val order = FirestoreOrder(
            orderId = "#MD123456",
            buyerName = "Sarah N.",
            buyerPhone = "0701 234 567",
            deliveryAddress = "Masaka Town",
            sellerName = "Fresh Farm",
            itemsSummary = "Matoke (2 kg), Rice 5kg",
            subtotalUgx = 21000,
            deliveryFeeUgx = 2000,
            totalUgx = 23000,
            deliveryPin = "4829",
            orderStatus = "ON_THE_WAY"
        )
        assertEquals(23000, order.totalUgx)
        assertEquals("4829", order.deliveryPin)
        assertEquals("ON_THE_WAY", order.orderStatus)
    }

    @Test
    fun testFirestoreDeliveryJobModel() {
        val job = FirestoreDeliveryJob(
            jobId = "job-1",
            orderId = "#MD123456",
            distanceKm = 2.1,
            payoutUgx = 3500,
            pickupLocation = "Fresh Farm",
            dropoffLocation = "Masaka Town",
            customerName = "Sarah N.",
            status = "AVAILABLE"
        )
        assertEquals("job-1", job.jobId)
        assertEquals(3500, job.payoutUgx)
        assertEquals("AVAILABLE", job.status)
    }

    @Test
    fun testFirestoreCartItemModel() {
        val cartItem = FirestoreCartItem(
            id = "c_1",
            productId = "prod_1",
            productName = "Rice 5kg",
            priceUgx = 18000,
            quantity = 2
        )
        assertEquals("Rice 5kg", cartItem.productName)
        assertEquals(2, cartItem.quantity)
    }
}

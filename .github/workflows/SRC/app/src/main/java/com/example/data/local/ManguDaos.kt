package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CartItemEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET stockQuantity = :stock WHERE id = :productId")
    suspend fun updateStock(productId: Int, stock: Int)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id ASC")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    suspend fun deleteById(cartItemId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getCount(): Int
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getCount(): Int
}

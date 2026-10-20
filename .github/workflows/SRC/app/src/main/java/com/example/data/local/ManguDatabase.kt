package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CartItemEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ManguDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: ManguDatabase? = null

        fun getDatabase(context: Context): ManguDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ManguDatabase::class.java,
                    "mangu_delivery_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

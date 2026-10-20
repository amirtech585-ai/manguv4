package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ManguDatabase
import com.example.data.model.CartItemEntity
import com.example.data.model.DeliveryJob
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PendingApplicant
import com.example.data.model.ProductEntity
import com.example.data.model.RiderStats
import com.example.data.model.SellerStats
import com.example.data.model.ThemeMode
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.remote.PlatformAdminBackendBridge
import com.example.data.repository.ManguRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AppScreen {
    // Auth & Onboarding flow
    object OnboardingRegister : AppScreen
    data class PendingApproval(val role: UserRole, val name: String, val details: String) : AppScreen

    // Buyer flow
    object BuyerHome : AppScreen
    data class ProductDetail(val product: ProductEntity) : AppScreen
    object Cart : AppScreen
    object Checkout : AppScreen
    data class DeliveryTracking(val orderId: String) : AppScreen
    data class OrderComplete(val orderId: String) : AppScreen

    // Seller flow
    object SellerDashboard : AppScreen
    object AddProduct : AppScreen
    object SellerOrders : AppScreen
    data class OrderDetail(val orderId: String) : AppScreen
    object SellerInventory : AppScreen
    object StoreSettings : AppScreen

    // Rider flow
    object RiderDashboard : AppScreen
    object AvailableJobs : AppScreen
    data class CurrentDelivery(val orderId: String) : AppScreen
    data class PinConfirmation(val orderId: String) : AppScreen
    data class RiderDeliveryComplete(val orderId: String, val payoutUgx: Int) : AppScreen

    // Additional tabs/pages
    object Account : AppScreen
    object Notifications : AppScreen
    object Support : AppScreen
    object Settings : AppScreen
}

class ManguViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ManguRepository = run {
        val db = ManguDatabase.getDatabase(application)
        ManguRepository(
            productDao = db.productDao(),
            cartDao = db.cartDao(),
            orderDao = db.orderDao(),
            notificationDao = db.notificationDao()
        )
    }
    private val prefs = application.getSharedPreferences("mangu_auth_prefs", Context.MODE_PRIVATE)

    // Current logged-in user account state
    private val _currentUserAccount = MutableStateFlow<UserAccount?>(null)
    val currentUserAccount: StateFlow<UserAccount?> = _currentUserAccount.asStateFlow()

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.BUYER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Screen navigation stack
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.OnboardingRegister)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Theme Mode state (persisted)
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Backstack history for robust navigation
    private val screenStack = mutableListOf<AppScreen>()

    // Pending Applicants for Admin Approval
    private val _pendingApplicants = MutableStateFlow<List<PendingApplicant>>(
        listOf(
            PendingApplicant(
                id = "app_1",
                name = "Masaka Fresh Grocers",
                phone = "0782 112 233",
                role = UserRole.SELLER,
                details = "Proprietor: John K. • Location: Elgin Road Central",
                submittedAt = System.currentTimeMillis() - 86400000L,
                isApproved = false
            ),
            PendingApplicant(
                id = "app_2",
                name = "Musa B. Boda Services",
                phone = "0702 445 566",
                role = UserRole.RIDER,
                details = "Motorcycle Reg: UDF 891L • Stage: Nyendo Stage",
                submittedAt = System.currentTimeMillis() - 36000000L,
                isApproved = false
            )
        )
    )
    val pendingApplicants: StateFlow<List<PendingApplicant>> = _pendingApplicants.asStateFlow()

    // Products & filtering
    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Rider state
    private val _riderStats = MutableStateFlow(RiderStats())
    val riderStats: StateFlow<RiderStats> = _riderStats.asStateFlow()

    // Seller stats
    private val _sellerStats = MutableStateFlow(SellerStats())
    val sellerStats: StateFlow<SellerStats> = _sellerStats.asStateFlow()

    // Available jobs for riders
    private val _availableJobs = MutableStateFlow(
        listOf(
            DeliveryJob(
                id = "job-1",
                orderId = "#MD123456",
                distanceKm = 2.1,
                payoutUgx = 3500,
                pickupLocation = "Fresh Farm, Elgin Rd",
                dropoffLocation = "Masaka Town Central",
                customerName = "Sarah N.",
                customerPhone = "0701 234 567",
                itemsSummary = "Matoke (2 kg), Rice 5kg",
                estimatedMinutes = 15
            ),
            DeliveryJob(
                id = "job-2",
                orderId = "#MD123455",
                distanceKm = 3.1,
                payoutUgx = 3000,
                pickupLocation = "Fresh Farm, Elgin Rd",
                dropoffLocation = "Nyendo Market Area",
                customerName = "Peter K.",
                customerPhone = "0752 987 654",
                itemsSummary = "Fresh Tomatoes (1 kg), Cooking Oil (1L)",
                estimatedMinutes = 20
            )
        )
    )
    val availableJobs: StateFlow<List<DeliveryJob>> = _availableJobs.asStateFlow()

    // Feedback message state
    private val _userFeedback = MutableStateFlow<String?>(null)
    val userFeedback: StateFlow<String?> = _userFeedback.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
        loadSavedThemeMode()
        loadSavedUserSession()
    }

    private fun loadSavedThemeMode() {
        val savedTheme = prefs.getString("app_theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        _themeMode.value = try {
            ThemeMode.valueOf(savedTheme)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("app_theme_mode", mode.name).apply()
        val label = when (mode) {
            ThemeMode.LIGHT -> "Light mode enabled"
            ThemeMode.DARK -> "Dark mode enabled"
            ThemeMode.SYSTEM -> "System theme enabled"
        }
        _userFeedback.value = label
    }

    fun toggleTheme(isCurrentlyDark: Boolean) {
        val nextMode = if (isCurrentlyDark) ThemeMode.LIGHT else ThemeMode.DARK
        setThemeMode(nextMode)
    }

    private fun loadSavedUserSession() {
        val userId = prefs.getString("user_id", null)
        val roleStr = prefs.getString("user_role", null)
        val name = prefs.getString("user_name", "") ?: ""
        val phone = prefs.getString("user_phone", "") ?: ""
        val address = prefs.getString("user_address", "Masaka City, Uganda") ?: "Masaka City, Uganda"
        val isApproved = prefs.getBoolean("user_is_approved", true)
        val details = prefs.getString("user_details", "") ?: ""
        val paymentMethod = prefs.getString("user_payment_method", "MTN Mobile Money") ?: "MTN Mobile Money"
        val language = prefs.getString("user_language", "English") ?: "English"

        if (userId != null && roleStr != null) {
            val role = try {
                UserRole.valueOf(roleStr)
            } catch (e: Exception) {
                UserRole.BUYER
            }
            val account = UserAccount(
                userId = userId,
                name = name,
                phone = phone,
                role = role,
                isApproved = isApproved,
                details = details,
                deliveryAddress = address,
                paymentMethod = paymentMethod,
                language = language
            )
            _currentUserAccount.value = account
            _currentRole.value = role

            if (!isApproved && (role == UserRole.SELLER || role == UserRole.RIDER)) {
                _currentScreen.value = AppScreen.PendingApproval(role, name, details)
            } else {
                _currentScreen.value = when (role) {
                    UserRole.BUYER -> AppScreen.BuyerHome
                    UserRole.SELLER -> AppScreen.SellerDashboard
                    UserRole.RIDER -> AppScreen.RiderDashboard
                    UserRole.ADMIN -> AppScreen.BuyerHome
                }
            }
        } else {
            _currentScreen.value = AppScreen.OnboardingRegister
        }
    }

    private fun saveUserToPrefs(account: UserAccount) {
        prefs.edit()
            .putString("user_id", account.userId)
            .putString("user_role", account.role.name)
            .putString("user_name", account.name)
            .putString("user_phone", account.phone)
            .putString("user_address", account.deliveryAddress)
            .putBoolean("user_is_approved", account.isApproved)
            .putString("user_details", account.details)
            .putString("user_payment_method", account.paymentMethod)
            .putString("user_language", account.language)
            .apply()
    }

    private fun clearUserPrefs() {
        val currentTheme = prefs.getString("app_theme_mode", ThemeMode.SYSTEM.name)
        prefs.edit().clear().apply()
        if (currentTheme != null) {
            prefs.edit().putString("app_theme_mode", currentTheme).apply()
        }
    }

    // ==========================================
    // AUTH & ROLE ONBOARDING METHODS
    // ==========================================

    fun registerBuyer(name: String, phone: String, address: String) {
        val account = UserAccount(
            userId = "buyer_${System.currentTimeMillis()}",
            name = name,
            phone = phone,
            role = UserRole.BUYER,
            isApproved = true,
            deliveryAddress = address
        )
        saveUserToPrefs(account)
        _currentUserAccount.value = account
        _currentRole.value = UserRole.BUYER
        screenStack.clear()
        _currentScreen.value = AppScreen.BuyerHome
        _userFeedback.value = "Buyer account registered! Welcome to ManguDelivery, $name."
    }

    fun submitSellerApplication(storeName: String, ownerName: String, phone: String, location: String) {
        val applicantId = "app_seller_${System.currentTimeMillis()}"
        val applicant = PendingApplicant(
            id = applicantId,
            name = storeName,
            phone = phone,
            role = UserRole.SELLER,
            details = "Proprietor: $ownerName • Masaka Location: $location",
            submittedAt = System.currentTimeMillis(),
            isApproved = false
        )
        _pendingApplicants.value = listOf(applicant) + _pendingApplicants.value

        val account = UserAccount(
            userId = applicantId,
            name = storeName,
            phone = phone,
            role = UserRole.SELLER,
            isApproved = false,
            details = "Owner: $ownerName • $location"
        )
        saveUserToPrefs(account)
        _currentUserAccount.value = account
        _currentRole.value = UserRole.SELLER
        screenStack.clear()
        _currentScreen.value = AppScreen.PendingApproval(UserRole.SELLER, storeName, "Owner: $ownerName • $location")
        
        viewModelScope.launch {
            backendAdminBridge.submitApplicationToRemoteAdmin(applicant)
        }
        _userFeedback.value = "Store application registered and synced to central management platform for review."
    }

    fun loginSeller(storeNameOrPhone: String) {
        // Check if pre-approved demo "Fresh Farm" or matches an approved applicant
        val query = storeNameOrPhone.trim().lowercase()
        val isFreshFarm = query.contains("fresh") || query.contains("farm") || query.contains("0782")
        val approvedApplicant = _pendingApplicants.value.find {
            it.role == UserRole.SELLER && it.isApproved &&
                    (it.name.lowercase().contains(query) || it.phone.contains(query))
        }

        if (isFreshFarm || approvedApplicant != null) {
            val name = if (isFreshFarm) "Fresh Farm" else approvedApplicant!!.name
            val phone = if (isFreshFarm) "0782 554 433" else approvedApplicant!!.phone
            val account = UserAccount(
                userId = "seller_approved",
                name = name,
                phone = phone,
                role = UserRole.SELLER,
                isApproved = true,
                details = "Verified Local Seller • Elgin Road, Masaka"
            )
            saveUserToPrefs(account)
            _currentUserAccount.value = account
            _currentRole.value = UserRole.SELLER
            screenStack.clear()
            _currentScreen.value = AppScreen.SellerDashboard
            _userFeedback.value = "Logged in to Store: $name"
        } else {
            // Check if there is a pending applicant
            val pendingApp = _pendingApplicants.value.find {
                it.role == UserRole.SELLER &&
                        (it.name.lowercase().contains(query) || it.phone.contains(query))
            }
            if (pendingApp != null) {
                val account = UserAccount(
                    userId = pendingApp.id,
                    name = pendingApp.name,
                    phone = pendingApp.phone,
                    role = UserRole.SELLER,
                    isApproved = false,
                    details = pendingApp.details
                )
                saveUserToPrefs(account)
                _currentUserAccount.value = account
                _currentRole.value = UserRole.SELLER
                _currentScreen.value = AppScreen.PendingApproval(UserRole.SELLER, pendingApp.name, pendingApp.details)
            } else {
                // If not found, submit as pending application
                submitSellerApplication(storeNameOrPhone, "Merchant", "0782 554 433", "Masaka City")
            }
        }
    }

    fun submitRiderApplication(riderName: String, phone: String, bodaReg: String, stage: String) {
        val applicantId = "app_rider_${System.currentTimeMillis()}"
        val applicant = PendingApplicant(
            id = applicantId,
            name = riderName,
            phone = phone,
            role = UserRole.RIDER,
            details = "Reg No: $bodaReg • Stage: $stage",
            submittedAt = System.currentTimeMillis(),
            isApproved = false
        )
        _pendingApplicants.value = listOf(applicant) + _pendingApplicants.value

        val account = UserAccount(
            userId = applicantId,
            name = riderName,
            phone = phone,
            role = UserRole.RIDER,
            isApproved = false,
            details = "Motorcycle Reg: $bodaReg • $stage"
        )
        saveUserToPrefs(account)
        _currentUserAccount.value = account
        _currentRole.value = UserRole.RIDER
        screenStack.clear()
        _currentScreen.value = AppScreen.PendingApproval(UserRole.RIDER, riderName, "Boda Reg: $bodaReg • $stage")

        viewModelScope.launch {
            backendAdminBridge.submitApplicationToRemoteAdmin(applicant)
        }
        _userFeedback.value = "Rider application registered and synced to central management platform for verification."
    }

    fun loginRider(phoneOrReg: String) {
        val query = phoneOrReg.trim().lowercase()
        val isJoseph = query.contains("joseph") || query.contains("234k") || query.contains("0701")
        val approvedApplicant = _pendingApplicants.value.find {
            it.role == UserRole.RIDER && it.isApproved &&
                    (it.name.lowercase().contains(query) || it.details.lowercase().contains(query) || it.phone.contains(query))
        }

        if (isJoseph || approvedApplicant != null) {
            val name = if (isJoseph) "Joseph Kigozi" else approvedApplicant!!.name
            val phone = if (isJoseph) "0701 234 567" else approvedApplicant!!.phone
            val account = UserAccount(
                userId = "rider_approved",
                name = name,
                phone = phone,
                role = UserRole.RIDER,
                isApproved = true,
                details = "Boda Reg #UFE 234K • Nyendo Stage"
            )
            saveUserToPrefs(account)
            _currentUserAccount.value = account
            _currentRole.value = UserRole.RIDER
            screenStack.clear()
            _currentScreen.value = AppScreen.RiderDashboard
            _userFeedback.value = "Welcome back, Rider $name!"
        } else {
            val pendingApp = _pendingApplicants.value.find {
                it.role == UserRole.RIDER &&
                        (it.name.lowercase().contains(query) || it.phone.contains(query))
            }
            if (pendingApp != null) {
                val account = UserAccount(
                    userId = pendingApp.id,
                    name = pendingApp.name,
                    phone = pendingApp.phone,
                    role = UserRole.RIDER,
                    isApproved = false,
                    details = pendingApp.details
                )
                saveUserToPrefs(account)
                _currentUserAccount.value = account
                _currentRole.value = UserRole.RIDER
                _currentScreen.value = AppScreen.PendingApproval(UserRole.RIDER, pendingApp.name, pendingApp.details)
            } else {
                submitRiderApplication(phoneOrReg, "0701 234 567", "UFE 234K", "Masaka Central Stage")
            }
        }
    }

    // Buyer deletes their account to change role
    fun deleteBuyerAccountAndReset() {
        clearUserPrefs()
        _currentUserAccount.value = null
        screenStack.clear()
        _currentScreen.value = AppScreen.OnboardingRegister
        _userFeedback.value = "Account deleted. You can now select a new role to register."
    }

    fun logoutUser() {
        clearUserPrefs()
        _currentUserAccount.value = null
        screenStack.clear()
        _currentScreen.value = AppScreen.OnboardingRegister
        _userFeedback.value = "Logged out successfully."
    }

    // ==========================================
    // USER SETTINGS & PREFERENCES (ALL USERS)
    // ==========================================

    fun updatePaymentMethod(newMethod: String) {
        val current = _currentUserAccount.value
        if (current != null) {
            val updated = current.copy(paymentMethod = newMethod)
            saveUserToPrefs(updated)
            _currentUserAccount.value = updated
            _userFeedback.value = "Default payment method set to $newMethod"
        }
    }

    fun updateDeliveryAddress(newAddress: String) {
        val current = _currentUserAccount.value
        if (current != null) {
            val updated = current.copy(deliveryAddress = newAddress)
            saveUserToPrefs(updated)
            _currentUserAccount.value = updated
            _userFeedback.value = "Delivery address updated to $newAddress"
        }
    }

    fun updateLanguage(newLanguage: String) {
        val current = _currentUserAccount.value
        if (current != null) {
            val updated = current.copy(language = newLanguage)
            saveUserToPrefs(updated)
            _currentUserAccount.value = updated
            _userFeedback.value = "Language set to $newLanguage"
        }
    }

    fun updateProfile(name: String, phone: String, details: String = "") {
        val current = _currentUserAccount.value
        if (current != null) {
            val updated = current.copy(
                name = name,
                phone = phone,
                details = if (details.isNotBlank()) details else current.details
            )
            saveUserToPrefs(updated)
            _currentUserAccount.value = updated
            _userFeedback.value = "Profile updated successfully."
        }
    }

    // ==========================================
    // STANDALONE PLATFORM BACKEND INTEGRATION
    // (Managed externally by standalone admin platform)
    // ==========================================

    val backendAdminBridge = PlatformAdminBackendBridge()

    fun checkApprovalStatus() {
        val currentAcc = _currentUserAccount.value ?: return
        if (currentAcc.isApproved) return

        viewModelScope.launch {
            val remoteStatus = backendAdminBridge.queryRemoteApprovalStatus(currentAcc.userId)
            val isRemoteApproved = remoteStatus.isApproved
            val localMatch = _pendingApplicants.value.find { it.id == currentAcc.userId || it.name == currentAcc.name }
            val isLocalApproved = localMatch?.isApproved == true

            if (isRemoteApproved || isLocalApproved) {
                val approvedAcc = currentAcc.copy(isApproved = true)
                saveUserToPrefs(approvedAcc)
                _currentUserAccount.value = approvedAcc
                _userFeedback.value = "Congratulations! Your account has been verified by the platform managers."
                _currentScreen.value = if (currentAcc.role == UserRole.SELLER) AppScreen.SellerDashboard else AppScreen.RiderDashboard
            } else {
                _userFeedback.value = "Application status: ${remoteStatus.status}. Verification is managed by the central standalone platform."
            }
        }
    }

    /**
     * Handler for remote approval webhooks sent from the standalone admin platform.
     */
    fun receiveRemoteAdminApproval(applicantId: String, notes: String = "Approved via Standalone Admin Backend") {
        backendAdminBridge.receiveRemoteAdminApproval(applicantId, notes)
        _pendingApplicants.value = _pendingApplicants.value.map { app ->
            if (app.id == applicantId) app.copy(isApproved = true) else app
        }
        val currentAcc = _currentUserAccount.value
        if (currentAcc != null && (currentAcc.userId == applicantId || currentAcc.name == _pendingApplicants.value.find { it.id == applicantId }?.name)) {
            val approvedAcc = currentAcc.copy(isApproved = true)
            saveUserToPrefs(approvedAcc)
            _currentUserAccount.value = approvedAcc
            _currentScreen.value = if (currentAcc.role == UserRole.SELLER) AppScreen.SellerDashboard else AppScreen.RiderDashboard
        }
    }

    /**
     * Helper feature enabling external standalone backends to simulate approval testing.
     */
    fun simulateRemoteApproval(applicantId: String) {
        receiveRemoteAdminApproval(applicantId)
    }

    // Navigation
    fun navigateTo(screen: AppScreen) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            return true
        }
        return false
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
        screenStack.clear()
        _currentScreen.value = when (role) {
            UserRole.BUYER -> AppScreen.BuyerHome
            UserRole.SELLER -> AppScreen.SellerDashboard
            UserRole.RIDER -> AppScreen.RiderDashboard
            UserRole.ADMIN -> AppScreen.BuyerHome
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Cart actions
    fun addToCart(product: ProductEntity, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(product, quantity)
            _userFeedback.value = "Added ${product.name} ($quantity ${product.unit}) to cart"
        }
    }

    fun updateCartQuantity(cartItem: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(cartItem, newQuantity)
        }
    }

    fun removeCartItem(cartItem: CartItemEntity) {
        viewModelScope.launch {
            repository.removeCartItem(cartItem)
        }
    }

    fun placeOrder(
        buyerName: String = "Sarah N.",
        buyerPhone: String = "0701 234 567",
        address: String = "Masaka, Uganda (Elgin Road)",
        paymentMethod: String = "Mobile Money (MTN)"
    ) {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isNotEmpty()) {
                val order = repository.placeOrder(buyerName, buyerPhone, address, paymentMethod, items)
                navigateTo(AppScreen.DeliveryTracking(order.orderId))
                _userFeedback.value = "Order placed successfully! Track order #${order.orderId}"
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    // Seller actions
    fun addProduct(
        name: String,
        category: String,
        price: Int,
        stock: Int,
        unit: String,
        imageUrl: String? = null,
        description: String = ""
    ) {
        viewModelScope.launch {
            val sellerStore = _currentUserAccount.value?.name?.ifBlank { "Fresh Farm" } ?: "Fresh Farm"
            val newProduct = ProductEntity(
                name = name,
                category = category,
                priceUgx = price,
                unit = unit,
                stockQuantity = stock,
                sellerName = sellerStore,
                isVerifiedSeller = true,
                description = if (description.isNotBlank()) description else "High quality item available in Masaka.",
                iconEmoji = when (category) {
                    "Groceries" -> "🥬"
                    "Electronics" -> "⚡"
                    "Fashion" -> "🛍️"
                    else -> "📦"
                },
                imageUrl = imageUrl
            )
            repository.addProduct(newProduct)
            _userFeedback.value = "Product '$name' added to store!"
            navigateTo(AppScreen.SellerDashboard)
        }
    }

    fun updateProductStock(productId: Int, newStock: Int) {
        viewModelScope.launch {
            repository.updateStock(productId, newStock)
            _userFeedback.value = "Stock updated successfully."
        }
    }

    // Rider actions
    fun toggleRiderOnline() {
        val current = _riderStats.value
        _riderStats.value = current.copy(isOnline = !current.isOnline)
        _userFeedback.value = if (!current.isOnline) "You are now ONLINE. Searching for orders..." else "You are now OFFLINE."
    }

    fun acceptDeliveryJob(job: DeliveryJob) {
        viewModelScope.launch {
            repository.updateOrderStatus(job.orderId, "PICKED_UP")
            _availableJobs.value = _availableJobs.value.filter { it.id != job.id }
            navigateTo(AppScreen.CurrentDelivery(job.orderId))
            _userFeedback.value = "Job accepted! Head to ${job.pickupLocation}"
        }
    }

    fun verifyDeliveryPin(orderId: String, enteredPin: String, actualPin: String): Boolean {
        if (enteredPin.trim() == actualPin.trim()) {
            viewModelScope.launch {
                repository.updateOrderStatus(orderId, "COMPLETED")
                val stats = _riderStats.value
                _riderStats.value = stats.copy(
                    todaysEarningsUgx = stats.todaysEarningsUgx + 3500,
                    completedDeliveries = stats.completedDeliveries + 1
                )
            }
            navigateTo(AppScreen.RiderDeliveryComplete(orderId, 3500))
            return true
        }
        _userFeedback.value = "Incorrect PIN! Please ask the customer for the correct 4-digit code."
        return false
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
            _userFeedback.value = "All notifications cleared."
        }
    }

    fun clearFeedback() {
        _userFeedback.value = null
    }
}

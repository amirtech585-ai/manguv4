package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.model.ThemeMode
import com.example.data.model.UserRole
import com.example.ui.auth.OnboardingRegisterScreen
import com.example.ui.auth.PendingApprovalScreen
import com.example.ui.buyer.BuyerHomeScreen
import com.example.ui.buyer.CartScreen
import com.example.ui.buyer.CheckoutScreen
import com.example.ui.buyer.DeliveryTrackingScreen
import com.example.ui.buyer.OrderCompleteScreen
import com.example.ui.buyer.ProductDetailScreen
import com.example.ui.common.AccountScreen
import com.example.ui.common.NotificationsScreen
import com.example.ui.components.ManguBottomNav
import com.example.ui.components.ManguHeaderBar
import com.example.ui.rider.AvailableJobsScreen
import com.example.ui.rider.RiderCurrentDeliveryScreen
import com.example.ui.rider.RiderDashboardScreen
import com.example.ui.rider.RiderDeliveryCompleteScreen
import com.example.ui.rider.RiderPinConfirmationScreen
import com.example.ui.seller.AddProductScreen
import com.example.ui.seller.OrderDetailScreen
import com.example.ui.seller.SellerDashboardScreen
import com.example.ui.seller.SellerInventoryScreen
import com.example.ui.seller.SellerOrdersScreen
import com.example.ui.seller.StoreSettingsScreen
import com.example.ui.theme.ManguDeliveryTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ManguViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ManguViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            ManguDeliveryTheme(themeMode = themeMode) {
                ManguDeliveryApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ManguDeliveryApp(viewModel: ManguViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val isDarkMode = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemDark
    }

    val currentUserAccount by viewModel.currentUserAccount.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val riderStats by viewModel.riderStats.collectAsState()
    val sellerStats by viewModel.sellerStats.collectAsState()
    val availableJobs by viewModel.availableJobs.collectAsState()
    val feedbackMessage by viewModel.userFeedback.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedback()
        }
    }

    val isAuthScreen = currentScreen is AppScreen.OnboardingRegister || currentScreen is AppScreen.PendingApproval

    val isRootScreen = when (currentScreen) {
        is AppScreen.BuyerHome,
        is AppScreen.SellerDashboard,
        is AppScreen.RiderDashboard,
        is AppScreen.OnboardingRegister -> true
        else -> false
    }

    BackHandler(enabled = !isRootScreen) {
        viewModel.navigateBack()
    }

    val screenTitle = when (currentScreen) {
        is AppScreen.ProductDetail -> "Product Details"
        is AppScreen.Cart -> "My Cart"
        is AppScreen.Checkout -> "Checkout"
        is AppScreen.DeliveryTracking -> "Delivery Tracking"
        is AppScreen.OrderComplete -> "Order Complete"
        is AppScreen.AddProduct -> "Add Product"
        is AppScreen.SellerOrders -> "Store Orders"
        is AppScreen.SellerInventory -> "Inventory"
        is AppScreen.StoreSettings -> "Store Settings"
        is AppScreen.AvailableJobs -> "Available Jobs"
        is AppScreen.CurrentDelivery -> "Active Delivery"
        is AppScreen.PinConfirmation -> "Verify PIN"
        is AppScreen.RiderDeliveryComplete -> "Delivery Summary"
        is AppScreen.Account -> "My Account"
        is AppScreen.Notifications -> "Notifications"
        else -> null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isAuthScreen) {
                ManguHeaderBar(
                    role = currentRole,
                    title = screenTitle,
                    showBack = !isRootScreen,
                    unreadNotifications = notifications.count { !it.isRead },
                    cartItemCount = cartItems.sumOf { it.quantity },
                    isDarkMode = isDarkMode,
                    onToggleTheme = { viewModel.toggleTheme(isDarkMode) },
                    onBackClick = { viewModel.navigateBack() },
                    onNotificationClick = { viewModel.navigateTo(AppScreen.Notifications) },
                    onCartClick = { viewModel.navigateTo(AppScreen.Cart) }
                )
            }
        },
        bottomBar = {
            if (!isAuthScreen) {
                ManguBottomNav(
                    role = currentRole,
                    currentScreen = currentScreen,
                    cartItemCount = cartItems.sumOf { it.quantity },
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                // Auth & Onboarding flow
                is AppScreen.OnboardingRegister -> {
                    OnboardingRegisterScreen(
                        isDarkMode = isDarkMode,
                        onToggleTheme = { viewModel.toggleTheme(isDarkMode) },
                        onRegisterBuyer = { name, phone, address ->
                            viewModel.registerBuyer(name, phone, address)
                        },
                        onSubmitSellerApplication = { storeName, owner, phone, loc ->
                            viewModel.submitSellerApplication(storeName, owner, phone, loc)
                        },
                        onLoginSeller = { storeNameOrPhone ->
                            viewModel.loginSeller(storeNameOrPhone)
                        },
                        onSubmitRiderApplication = { rider, phone, reg, stage ->
                            viewModel.submitRiderApplication(rider, phone, reg, stage)
                        },
                        onLoginRider = { phoneOrReg ->
                            viewModel.loginRider(phoneOrReg)
                        }
                    )
                }

                is AppScreen.PendingApproval -> {
                    PendingApprovalScreen(
                        role = screen.role,
                        name = screen.name,
                        details = screen.details,
                        isDarkMode = isDarkMode,
                        onToggleTheme = { viewModel.toggleTheme(isDarkMode) },
                        onRefresh = { viewModel.checkApprovalStatus() },
                        onChangeAccount = { viewModel.deleteBuyerAccountAndReset() }
                    )
                }

                // Buyer Screens
                is AppScreen.BuyerHome -> {
                    BuyerHomeScreen(
                        products = products,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onCategorySelect = { viewModel.selectCategory(it) },
                        onProductClick = { viewModel.navigateTo(AppScreen.ProductDetail(it)) },
                        onAddToCart = { viewModel.addToCart(it) }
                    )
                }

                is AppScreen.ProductDetail -> {
                    ProductDetailScreen(
                        product = screen.product,
                        onAddToCart = { prod, qty -> viewModel.addToCart(prod, qty) },
                        onBuyNow = { prod, qty ->
                            viewModel.addToCart(prod, qty)
                            viewModel.navigateTo(AppScreen.Cart)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.Cart -> {
                    CartScreen(
                        items = cartItems,
                        onUpdateQuantity = { item, qty -> viewModel.updateCartQuantity(item, qty) },
                        onRemove = { viewModel.removeCartItem(it) },
                        onCheckout = { viewModel.navigateTo(AppScreen.Checkout) }
                    )
                }

                is AppScreen.Checkout -> {
                    CheckoutScreen(
                        items = cartItems,
                        onPlaceOrder = { name, phone, address, payment ->
                            viewModel.placeOrder(name, phone, address, payment)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.DeliveryTracking -> {
                    val activeOrder = orders.find { it.orderId == screen.orderId } ?: orders.firstOrNull()
                    DeliveryTrackingScreen(
                        order = activeOrder,
                        onCompleteDemo = {
                            viewModel.navigateTo(AppScreen.OrderComplete(screen.orderId))
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.OrderComplete -> {
                    OrderCompleteScreen(
                        orderId = screen.orderId,
                        onViewDetails = { viewModel.navigateTo(AppScreen.DeliveryTracking(screen.orderId)) },
                        onRateReview = { viewModel.navigateTo(AppScreen.BuyerHome) },
                        onBackHome = { viewModel.navigateTo(AppScreen.BuyerHome) }
                    )
                }

                // Seller Screens
                is AppScreen.SellerDashboard -> {
                    SellerDashboardScreen(
                        stats = sellerStats,
                        orders = orders,
                        storeName = currentUserAccount?.name?.ifBlank { "Fresh Farm" } ?: "Fresh Farm",
                        onAddProductClick = { viewModel.navigateTo(AppScreen.AddProduct) },
                        onOrdersClick = { viewModel.navigateTo(AppScreen.SellerOrders) },
                        onOrderClick = { orderId -> viewModel.navigateTo(AppScreen.OrderDetail(orderId)) },
                        onInventoryClick = { viewModel.navigateTo(AppScreen.SellerInventory) },
                        onStoreSettingsClick = { viewModel.navigateTo(AppScreen.StoreSettings) }
                    )
                }

                is AppScreen.OrderDetail -> {
                    val targetOrder = orders.find { it.orderId == screen.orderId }
                    OrderDetailScreen(
                        order = targetOrder,
                        onUpdateStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.AddProduct -> {
                    AddProductScreen(
                        onSaveProduct = { name, category, price, stock, unit, imageUrl, desc ->
                            viewModel.addProduct(name, category, price, stock, unit, imageUrl, desc)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.SellerOrders -> {
                    SellerOrdersScreen(
                        orders = orders,
                        onUpdateStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.SellerInventory -> {
                    SellerInventoryScreen(
                        products = products,
                        onUpdateStock = { id, stock -> viewModel.updateProductStock(id, stock) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.StoreSettings -> {
                    StoreSettingsScreen(
                        currentUser = currentUserAccount,
                        themeMode = themeMode,
                        onSelectThemeMode = { viewModel.setThemeMode(it) },
                        onUpdatePaymentMethod = { viewModel.updatePaymentMethod(it) },
                        onUpdateStoreAddress = { viewModel.updateDeliveryAddress(it) },
                        onResetAccount = { viewModel.deleteBuyerAccountAndReset() },
                        onLogout = { viewModel.logoutUser() },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                // Rider Screens
                is AppScreen.RiderDashboard -> {
                    RiderDashboardScreen(
                        stats = riderStats,
                        riderUser = currentUserAccount,
                        orders = orders,
                        onToggleOnline = { viewModel.toggleRiderOnline() },
                        onAvailableJobsClick = { viewModel.navigateTo(AppScreen.AvailableJobs) },
                        onActiveDeliveryClick = {
                            val activeJob = orders.find { it.orderStatus == "PICKED_UP" || it.orderStatus == "ON_THE_WAY" || it.orderStatus == "RIDER_ASSIGNED" }
                            if (activeJob != null) {
                                viewModel.navigateTo(AppScreen.CurrentDelivery(activeJob.orderId))
                            } else {
                                viewModel.navigateTo(AppScreen.CurrentDelivery(""))
                            }
                        },
                        onWalletClick = { viewModel.navigateTo(AppScreen.Account) },
                        onProfileClick = { viewModel.navigateTo(AppScreen.Account) },
                        onLogout = { viewModel.logoutUser() }
                    )
                }

                is AppScreen.AvailableJobs -> {
                    AvailableJobsScreen(
                        jobs = availableJobs,
                        onAcceptJob = { job -> viewModel.acceptDeliveryJob(job) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.CurrentDelivery -> {
                    val activeOrder = orders.find { it.orderId == screen.orderId }
                    RiderCurrentDeliveryScreen(
                        order = activeOrder,
                        onProceedToPin = { viewModel.navigateTo(AppScreen.PinConfirmation(screen.orderId)) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.PinConfirmation -> {
                    val order = orders.find { it.orderId == screen.orderId }
                    val actualPin = order?.deliveryPin ?: "4829"
                    RiderPinConfirmationScreen(
                        orderId = screen.orderId,
                        onConfirmPin = { pin ->
                            viewModel.verifyDeliveryPin(screen.orderId, pin, actualPin)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.RiderDeliveryComplete -> {
                    RiderDeliveryCompleteScreen(
                        orderId = screen.orderId,
                        payoutUgx = screen.payoutUgx,
                        onBackHome = { viewModel.navigateTo(AppScreen.RiderDashboard) }
                    )
                }

                // Common Screens
                is AppScreen.Account -> {
                    AccountScreen(
                        currentUser = currentUserAccount,
                        currentRole = currentRole,
                        themeMode = themeMode,
                        onSelectThemeMode = { viewModel.setThemeMode(it) },
                        onUpdatePaymentMethod = { viewModel.updatePaymentMethod(it) },
                        onUpdateDeliveryAddress = { viewModel.updateDeliveryAddress(it) },
                        onUpdateLanguage = { viewModel.updateLanguage(it) },
                        onUpdateProfile = { name, phone -> viewModel.updateProfile(name, phone) },
                        onDeleteBuyerAccount = { viewModel.deleteBuyerAccountAndReset() },
                        onLogout = { viewModel.logoutUser() }
                    )
                }

                is AppScreen.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onClearAll = { viewModel.clearAllNotifications() },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.Support -> {
                    AccountScreen(
                        currentUser = currentUserAccount,
                        currentRole = currentRole,
                        themeMode = themeMode,
                        onSelectThemeMode = { viewModel.setThemeMode(it) },
                        onUpdatePaymentMethod = { viewModel.updatePaymentMethod(it) },
                        onUpdateDeliveryAddress = { viewModel.updateDeliveryAddress(it) },
                        onUpdateLanguage = { viewModel.updateLanguage(it) },
                        onUpdateProfile = { name, phone -> viewModel.updateProfile(name, phone) },
                        onDeleteBuyerAccount = { viewModel.deleteBuyerAccountAndReset() },
                        onLogout = { viewModel.logoutUser() }
                    )
                }

                is AppScreen.Settings -> {
                    StoreSettingsScreen(
                        currentUser = currentUserAccount,
                        themeMode = themeMode,
                        onSelectThemeMode = { viewModel.setThemeMode(it) },
                        onUpdatePaymentMethod = { viewModel.updatePaymentMethod(it) },
                        onUpdateStoreAddress = { viewModel.updateDeliveryAddress(it) },
                        onResetAccount = { viewModel.deleteBuyerAccountAndReset() },
                        onLogout = { viewModel.logoutUser() },
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }
}

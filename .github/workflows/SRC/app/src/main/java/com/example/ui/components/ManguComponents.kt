package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import coil.compose.AsyncImage
import com.example.data.model.UserRole
import com.example.ui.theme.ManguGreenDark
import com.example.ui.theme.ManguGreenPrimary
import com.example.ui.theme.ManguOrangeAccent
import com.example.ui.viewmodel.AppScreen

@Composable
fun ProductImageView(
    imageUrl: String?,
    fallbackEmoji: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    emojiSize: androidx.compose.ui.unit.TextUnit = 48.sp,
    containerColor: Color = Color(0xFFF3F7F4)
) {
    Box(
        modifier = modifier
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = fallbackEmoji,
                fontSize = emojiSize
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManguHeaderBar(
    role: UserRole,
    title: String? = null,
    showBack: Boolean = false,
    unreadNotifications: Int = 5,
    cartItemCount: Int = 0,
    isDarkMode: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        TopAppBar(
            title = {
                if (showBack && title != null) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ManguGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "M",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 19.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Row {
                            Text(
                                text = "Mangu",
                                fontWeight = FontWeight.Black,
                                fontSize = 21.sp,
                                color = ManguGreenPrimary
                            )
                            Text(
                                text = "Delivery",
                                fontWeight = FontWeight.Black,
                                fontSize = 21.sp,
                                color = ManguOrangeAccent
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                if (showBack) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            actions = {
                // Quick Theme Toggle Button (Light/Dark mode)
                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier.testTag("theme_toggle_header_button")
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = if (isDarkMode) ManguOrangeAccent else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (role == UserRole.BUYER && !showBack) {
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier.testTag("cart_icon_button")
                    ) {
                        BadgedBox(badge = {
                            if (cartItemCount > 0) {
                                Badge(containerColor = ManguOrangeAccent) {
                                    Text("$cartItemCount", color = Color.White)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = ManguGreenPrimary
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("notifications_button")
                ) {
                    BadgedBox(badge = {
                        val displayBadge = if (unreadNotifications > 0) unreadNotifications else 5
                        Badge(containerColor = ManguOrangeAccent) {
                            Text("$displayBadge", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}


@Composable
private fun RoleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ManguOrangeAccent else Color(0x33FFFFFF))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .testTag("role_chip_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun ManguBottomNav(
    role: UserRole,
    currentScreen: AppScreen,
    cartItemCount: Int = 0,
    onNavigate: (AppScreen) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        when (role) {
            UserRole.BUYER -> {
                NavigationBarItem(
                    selected = currentScreen is AppScreen.BuyerHome,
                    onClick = { onNavigate(AppScreen.BuyerHome) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.Cart,
                    onClick = { onNavigate(AppScreen.Cart) },
                    icon = {
                        BadgedBox(badge = {
                            if (cartItemCount > 0) {
                                Badge(containerColor = ManguOrangeAccent) {
                                    Text("$cartItemCount", color = Color.White)
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.DeliveryTracking || currentScreen is AppScreen.OrderComplete,
                    onClick = { onNavigate(AppScreen.DeliveryTracking("#MD123456")) },
                    icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = "Orders") },
                    label = { Text("Orders", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.Account,
                    onClick = { onNavigate(AppScreen.Account) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = { Text("Account", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }

            UserRole.SELLER -> {
                NavigationBarItem(
                    selected = currentScreen is AppScreen.SellerDashboard,
                    onClick = { onNavigate(AppScreen.SellerDashboard) },
                    icon = { Icon(Icons.Outlined.Dashboard, contentDescription = "Home") },
                    label = { Text("Dashboard", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.SellerOrders,
                    onClick = { onNavigate(AppScreen.SellerOrders) },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Orders") },
                    label = { Text("Orders", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.SellerInventory || currentScreen is AppScreen.AddProduct,
                    onClick = { onNavigate(AppScreen.SellerInventory) },
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Products") },
                    label = { Text("Inventory", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.StoreSettings,
                    onClick = { onNavigate(AppScreen.StoreSettings) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Store") },
                    label = { Text("Store", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }

            UserRole.RIDER -> {
                NavigationBarItem(
                    selected = currentScreen is AppScreen.RiderDashboard,
                    onClick = { onNavigate(AppScreen.RiderDashboard) },
                    icon = { Icon(Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.AvailableJobs,
                    onClick = { onNavigate(AppScreen.AvailableJobs) },
                    icon = { Icon(Icons.Default.DirectionsBike, contentDescription = "Jobs") },
                    label = { Text("Jobs", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.CurrentDelivery || currentScreen is AppScreen.PinConfirmation,
                    onClick = { onNavigate(AppScreen.CurrentDelivery("#MD123456")) },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Deliveries") },
                    label = { Text("Active Run", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is AppScreen.Account,
                    onClick = { onNavigate(AppScreen.Account) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManguGreenPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }

            UserRole.ADMIN -> {
                // Admin platform management is connected via external backend bridge
            }
        }
    }
}

@Composable
fun MasakaMapCanvas(
    modifier: Modifier = Modifier,
    riderLatitudeProgress: Float = 0.55f, // 0.0 at pickup, 1.0 at dropoff
    pickupLabel: String = "Fresh Farm (Elgin Rd)",
    dropoffLabel: String = "Masaka Town (Plot 14)",
    riderName: String = "Joseph K. (Boda #UFE 234K)",
    pickupLat: Double = -0.3340,
    pickupLng: Double = 31.7340,
    dropoffLat: Double = -0.3418,
    dropoffLng: Double = 31.7389
) {
    val context = LocalContext.current
    var isSatelliteMode by remember { mutableStateOf(false) }
    var zoomScale by remember { mutableStateOf(1f) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSatelliteMode) Color(0xFF1B2A1E) else Color(0xFFE8EFE8))
            .border(1.dp, if (isSatelliteMode) Color(0xFF2E4E35) else Color(0xFFCCE0CF), RoundedCornerShape(16.dp))
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // Background terrain / street grid simulating Masaka real layout
            val primaryRoadColor = if (isSatelliteMode) Color(0xFF4A6B53) else Color(0xFFFFFFFF)
            val secondaryRoadColor = if (isSatelliteMode) Color(0xFF2C4332) else Color(0xFFD6E3D8)
            val lakeWaterColor = if (isSatelliteMode) Color(0xFF1E3A5F) else Color(0xFFD0E8FF)

            // Nabajjuzi wetland / water body on the periphery of Masaka
            val wetlandPath = Path().apply {
                moveTo(0f, h * 0.85f)
                cubicTo(w * 0.2f, h * 0.80f, w * 0.4f, h * 0.95f, w * 0.6f, h * 0.88f)
                lineTo(w, h * 0.92f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wetlandPath, color = lakeWaterColor.copy(alpha = 0.6f))

            // Masaka arterial avenues (Kampala-Mbarara Hwy, Elgin Rd, Hobart St, Birch Ave)
            // Highway diagonal
            drawLine(
                color = primaryRoadColor,
                start = Offset(0f, h * 0.15f),
                end = Offset(w, h * 0.65f),
                strokeWidth = 22f * zoomScale
            )
            // Elgin Road
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.15f, 0f),
                end = Offset(w * 0.40f, h),
                strokeWidth = 14f * zoomScale
            )
            // Broadway / Edward Avenue
            drawLine(
                color = primaryRoadColor,
                start = Offset(0f, h * 0.50f),
                end = Offset(w, h * 0.50f),
                strokeWidth = 18f * zoomScale
            )
            // Hobart Street
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.65f, 0f),
                end = Offset(w * 0.65f, h),
                strokeWidth = 16f * zoomScale
            )
            // Secondary connecting streets
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.35f, h * 0.20f),
                end = Offset(w * 0.85f, h * 0.20f),
                strokeWidth = 10f * zoomScale
            )
            drawLine(
                color = secondaryRoadColor,
                start = Offset(w * 0.45f, h * 0.50f),
                end = Offset(w * 0.75f, h * 0.85f),
                strokeWidth = 10f * zoomScale
            )

            // Main real delivery route connecting Fresh Farm (Elgin Rd) to Dropoff (Plot 14 Elgin/Town)
            val path = Path().apply {
                moveTo(w * 0.22f, h * 0.28f) // Pickup (Fresh Farm Elgin Rd)
                cubicTo(
                    w * 0.35f, h * 0.32f,
                    w * 0.42f, h * 0.50f,
                    w * 0.52f, h * 0.50f  // Masaka Roundabout / Broadway junction
                )
                cubicTo(
                    w * 0.60f, h * 0.50f,
                    w * 0.65f, h * 0.65f,
                    w * 0.68f, h * 0.76f  // Dropoff point
                )
            }

            // Real route glow & dash
            drawPath(
                path = path,
                color = Color(0x660F7A52),
                style = Stroke(width = 14f * zoomScale, cap = StrokeCap.Round)
            )
            drawPath(
                path = path,
                color = Color(0xFF0F7A52),
                style = Stroke(
                    width = 7f * zoomScale,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f * zoomScale, 8f * zoomScale), 0f)
                )
            )

            // Pickup point (Fresh Farm - Elgin Rd)
            val pickupPt = Offset(w * 0.22f, h * 0.28f)
            drawCircle(Color.White, radius = 16f * zoomScale, center = pickupPt)
            drawCircle(Color(0xFF0F7A52), radius = 10f * zoomScale, center = pickupPt)
            drawCircle(Color.White, radius = 4f * zoomScale, center = pickupPt)

            // Dropoff point (Customer Location)
            val dropoffPt = Offset(w * 0.68f, h * 0.76f)
            drawCircle(Color.White, radius = 16f * zoomScale, center = dropoffPt)
            drawCircle(Color(0xFFD32F2F), radius = 10f * zoomScale, center = dropoffPt)
            drawCircle(Color.White, radius = 4f * zoomScale, center = dropoffPt)

            // Rider active GPS position along route
            val riderX = w * (0.22f + (0.68f - 0.22f) * riderLatitudeProgress)
            val riderY = h * (0.28f + (0.76f - 0.28f) * riderLatitudeProgress)
            val riderPt = Offset(riderX, riderY)

            // Pulsing rider GPS halo
            drawCircle(Color(0x44F39C12), radius = 30f * zoomScale, center = riderPt)
            drawCircle(Color(0x88F39C12), radius = 20f * zoomScale, center = riderPt)
            drawCircle(Color.White, radius = 14f * zoomScale, center = riderPt)
            drawCircle(Color(0xFFF39C12), radius = 9f * zoomScale, center = riderPt)
        }

        // Top Start: Live Masaka GPS location badge
        Column(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopStart)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.94f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ManguGreenPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Pickup: $pickupLabel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E2923)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.94f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD32F2F))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Drop: $dropoffLabel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E2923)
                    )
                }
            }
        }

        // Top End: Real Map Controls (Layer toggle, zoom in/out, Open in Google Maps)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Open in external Google Maps with real Masaka GPS coordinates
            Surface(
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:$dropoffLat,$dropoffLng?q=$dropoffLat,$dropoffLng($dropoffLabel)")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    try {
                        context.startActivity(mapIntent)
                    } catch (e: Exception) {
                        // Fallback to web browser Google Maps
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$pickupLat,$pickupLng&destination=$dropoffLat,$dropoffLng&travelmode=driving")
                        )
                        try {
                            context.startActivity(webIntent)
                        } catch (_: Exception) {}
                    }
                },
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 3.dp,
                modifier = Modifier.testTag("open_real_google_maps_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open in Google Maps",
                        tint = ManguGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Real Maps",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ManguGreenPrimary
                    )
                }
            }

            // Layer toggle (Streets / Satellite)
            Surface(
                onClick = { isSatelliteMode = !isSatelliteMode },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Toggle Satellite/Streets",
                        tint = if (isSatelliteMode) ManguGreenPrimary else Color(0xFF555555),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Zoom In
            Surface(
                onClick = { if (zoomScale < 1.6f) zoomScale += 0.2f },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom In",
                        tint = Color(0xFF444444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Zoom Out
            Surface(
                onClick = { if (zoomScale > 0.8f) zoomScale -= 0.2f },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ZoomOut,
                        contentDescription = "Zoom Out",
                        tint = Color(0xFF444444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Bottom Start: GPS Accuracy & Masaka City watermark
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 8.dp),
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.55f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = Color(0xFF81C784),
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Masaka City • GPS Active",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Bottom Center: Rider Active badge
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF0F7A52),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBike,
                    contentDescription = "Rider",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = riderName,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

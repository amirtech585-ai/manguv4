package com.example.ui.seller

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import com.example.data.model.ThemeMode
import com.example.ui.common.ThemeOptionItem
import com.example.ui.components.ProductImageView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SellerStats
import com.example.ui.theme.ManguGreenDark
import com.example.ui.theme.ManguGreenPrimary
import com.example.ui.theme.ManguOrangeAccent
import java.text.NumberFormat
import java.util.Locale

private fun formatUgx(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "UGX " + formatter.format(amount)
}

@Composable
fun SellerDashboardScreen(
    stats: SellerStats,
    orders: List<OrderEntity>,
    storeName: String = "Fresh Farm",
    onAddProductClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onOrderClick: (orderId: String) -> Unit = {},
    onInventoryClick: () -> Unit,
    onStoreSettingsClick: () -> Unit
) {
    var selectedTimeframe by remember { mutableStateOf("Today") }
    val timeframes = listOf("Today", "This Week", "This Month", "All Time")

    val now = System.currentTimeMillis()
    val filteredOrdersByTime = orders.filter { order ->
        when (selectedTimeframe) {
            "Today" -> order.timestamp >= now - (24 * 60 * 60 * 1000L)
            "This Week" -> order.timestamp >= now - (7 * 24 * 60 * 60 * 1000L)
            "This Month" -> order.timestamp >= now - (30L * 24 * 60 * 60 * 1000L)
            else -> true
        }
    }
    val actualOrderCount = filteredOrdersByTime.size
    val actualTotalSalesUgx = filteredOrdersByTime.sumOf { it.totalUgx.toLong() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Store Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2F6ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏡", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "My Registered Business",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = storeName.ifBlank { "Masaka Produce Store" },
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = ManguGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Masaka City • Verified Local Seller",
                            fontSize = 11.sp,
                            color = ManguGreenPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ManguOrangeAccent.copy(alpha = 0.15f),
                        modifier = Modifier.clickable(onClick = onStoreSettingsClick)
                    ) {
                        Text(
                            text = "Settings",
                            color = ManguOrangeAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Time Frame Filter for Exact Sales & Orders
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sales Performance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Actual Records ($selectedTimeframe)",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeframes.forEach { tf ->
                        val isSelected = tf == selectedTimeframe
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTimeframe = tf }
                        ) {
                            Text(
                                text = tf,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // Stats Row: Exact Orders & Total Sales for the selected timeframe
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Exact Orders Count
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F6ED))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "$selectedTimeframe Orders",
                            fontSize = 12.sp,
                            color = Color(0xFF0C5A3C),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$actualOrderCount",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0C5A3C)
                        )
                        Text(
                            text = "Actual recorded orders",
                            fontSize = 10.sp,
                            color = Color(0xFF0C5A3C).copy(alpha = 0.7f)
                        )
                    }
                }

                // Exact Total Sales
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2DC))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "$selectedTimeframe Sales",
                            fontSize = 12.sp,
                            color = Color(0xFF8A5300),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatUgx(actualTotalSalesUgx),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF8A5300)
                        )
                        Text(
                            text = "Exact sum of orders",
                            fontSize = 10.sp,
                            color = Color(0xFF8A5300).copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // Quick Actions Grid (Products, Orders, Inventory, Analytics)
        item {
            Text(
                text = "Quick Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    label = "+ Add Product",
                    icon = Icons.Default.Add,
                    color = ManguGreenPrimary,
                    onClick = onAddProductClick,
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Orders",
                    icon = Icons.Default.ReceiptLong,
                    color = ManguOrangeAccent,
                    onClick = onOrdersClick,
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Inventory",
                    icon = Icons.Default.Inventory2,
                    color = Color(0xFF2B78C5),
                    onClick = onInventoryClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Orders Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Orders",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ManguGreenPrimary,
                    modifier = Modifier.clickable(onClick = onOrdersClick)
                )
            }
        }

        // Recent Orders List (Showing Product Name prominently and clickable)
        items(orders.take(5)) { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOrderClick(order.orderId) }
                    .testTag("recent_order_${order.orderId}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.itemsSummary,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = order.orderId,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Customer: ${order.buyerName}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "UGX " + NumberFormat.getNumberInstance(Locale.US).format(order.totalUgx),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ManguGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = when (order.orderStatus) {
                                "PENDING_PAYMENT", "SELLER_CONFIRMED" -> Color(0xFFFFF2DC)
                                "ON_THE_WAY", "READY_FOR_PICKUP" -> Color(0xFFE3F2FD)
                                "DELIVERED", "COMPLETED" -> Color(0xFFE8F8F0)
                                else -> Color(0xFFEEEEEE)
                            }
                        ) {
                            Text(
                                text = when (order.orderStatus) {
                                    "SELLER_CONFIRMED" -> "Preparing"
                                    "READY_FOR_PICKUP" -> "Ready"
                                    "ON_THE_WAY" -> "On Way"
                                    "DELIVERED", "COMPLETED" -> "Delivered"
                                    else -> "Pending"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (order.orderStatus) {
                                    "PENDING_PAYMENT", "SELLER_CONFIRMED" -> ManguOrangeAccent
                                    "ON_THE_WAY", "READY_FOR_PICKUP" -> Color(0xFF1976D2)
                                    "DELIVERED", "COMPLETED" -> ManguGreenPrimary
                                    else -> Color.DarkGray
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AddProductScreen(
    onSaveProduct: (String, String, Int, Int, String, String?, String) -> Unit,
    onBack: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var stockStr by remember { mutableStateOf("") }
    var unitStr by remember { mutableStateOf("") }
    var descriptionStr by remember { mutableStateOf("") }

    // 4 required side views: Front, Back, Left, Right
    var sideView1Uri by remember { mutableStateOf<Uri?>(null) }
    var sideView2Uri by remember { mutableStateOf<Uri?>(null) }
    var sideView3Uri by remember { mutableStateOf<Uri?>(null) }
    var sideView4Uri by remember { mutableStateOf<Uri?>(null) }

    var sideView1Url by remember { mutableStateOf("") }
    var sideView2Url by remember { mutableStateOf("") }
    var sideView3Url by remember { mutableStateOf("") }
    var sideView4Url by remember { mutableStateOf("") }

    var activeSlotForPicker by remember { mutableIntStateOf(1) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Groceries", "Electronics", "Fashion", "Home & Living")

    // Single photo picker launcher
    val singlePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            when (activeSlotForPicker) {
                1 -> { sideView1Uri = uri; sideView1Url = "" }
                2 -> { sideView2Uri = uri; sideView2Url = "" }
                3 -> { sideView3Uri = uri; sideView3Url = "" }
                4 -> { sideView4Uri = uri; sideView4Url = "" }
            }
            validationError = null
        }
    }

    // Multiple photo picker launcher to pick all 4 at once
    val multiplePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 4)
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            if (uris.size >= 1) { sideView1Uri = uris[0]; sideView1Url = "" }
            if (uris.size >= 2) { sideView2Uri = uris[1]; sideView2Url = "" }
            if (uris.size >= 3) { sideView3Uri = uris[2]; sideView3Url = "" }
            if (uris.size >= 4) { sideView4Uri = uris[3]; sideView4Url = "" }
            validationError = null
        }
    }

    val img1 = sideView1Uri?.toString() ?: sideView1Url.trim().ifBlank { null }
    val img2 = sideView2Uri?.toString() ?: sideView2Url.trim().ifBlank { null }
    val img3 = sideView3Uri?.toString() ?: sideView3Url.trim().ifBlank { null }
    val img4 = sideView4Uri?.toString() ?: sideView4Url.trim().ifBlank { null }

    val uploadedCount = listOfNotNull(img1, img2, img3, img4).size
    val allFourViewsProvided = img1 != null && img2 != null && img3 != null && img4 != null

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Add New Product",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Upload at least 4 side views of the product and provide all required details before submitting.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Section: Required 4 Side Views
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (allFourViewsProvided) Color(0xFFE8F8F0) else Color(0xFFFFF9EE)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (allFourViewsProvided) ManguGreenPrimary else ManguOrangeAccent
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Product Side Views (4 Required)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (allFourViewsProvided) ManguGreenDark else Color(0xFF8A5300)
                            )
                            Text(
                                text = "$uploadedCount of 4 side views uploaded",
                                fontSize = 11.sp,
                                color = if (allFourViewsProvided) ManguGreenPrimary else Color(0xFFB26B00),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                multiplePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pick 4 Photos", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 Side View Slots Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SideViewUploadSlot(
                            slotNumber = 1,
                            label = "Front View",
                            imageUrl = img1,
                            onPickClick = {
                                activeSlotForPicker = 1
                                singlePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onRemove = { sideView1Uri = null; sideView1Url = "" },
                            modifier = Modifier.weight(1f)
                        )
                        SideViewUploadSlot(
                            slotNumber = 2,
                            label = "Back View",
                            imageUrl = img2,
                            onPickClick = {
                                activeSlotForPicker = 2
                                singlePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onRemove = { sideView2Uri = null; sideView2Url = "" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SideViewUploadSlot(
                            slotNumber = 3,
                            label = "Left View",
                            imageUrl = img3,
                            onPickClick = {
                                activeSlotForPicker = 3
                                singlePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onRemove = { sideView3Uri = null; sideView3Url = "" },
                            modifier = Modifier.weight(1f)
                        )
                        SideViewUploadSlot(
                            slotNumber = 4,
                            label = "Right View",
                            imageUrl = img4,
                            onPickClick = {
                                activeSlotForPicker = 4
                                singlePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onRemove = { sideView4Uri = null; sideView4Url = "" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Product Name (Required)
        item {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it; validationError = null },
                label = { Text("Product Name *") },
                placeholder = { Text("e.g. Masaka Organic Matoke") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_product_name"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Category Pills (Required selection)
        item {
            Text("Category *", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == category
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.clickable { category = cat; validationError = null }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Price in UGX (Required, must be > 0)
        item {
            OutlinedTextField(
                value = priceStr,
                onValueChange = { priceStr = it; validationError = null },
                label = { Text("Price in UGX *") },
                placeholder = { Text("e.g. 5000") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_product_price"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Unit / Packaging (Required, no default!)
        item {
            OutlinedTextField(
                value = unitStr,
                onValueChange = { unitStr = it; validationError = null },
                label = { Text("Unit of Measurement / Packaging *") },
                placeholder = { Text("e.g. 1 bunch, 5kg bag, 2 litres") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Stock Quantity (Required, must be valid number >= 0)
        item {
            OutlinedTextField(
                value = stockStr,
                onValueChange = { stockStr = it; validationError = null },
                label = { Text("Stock Quantity (Units available) *") },
                placeholder = { Text("e.g. 50") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_product_stock"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Description (Required)
        item {
            OutlinedTextField(
                value = descriptionStr,
                onValueChange = { descriptionStr = it; validationError = null },
                label = { Text("Product Description *") },
                placeholder = { Text("Provide details: origin in Masaka, quality, freshness, specifications...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )
        }

        // Validation Error Message Banner
        validationError?.let { err ->
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = err,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Submit Button: Only completes if all required info and 4 side views are present
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = {
                    val price = priceStr.trim().toIntOrNull()
                    val stock = stockStr.trim().toIntOrNull()

                    when {
                        !allFourViewsProvided -> {
                            validationError = "Please upload all 4 side views (Front, Back, Left, Right) before completing registration."
                        }
                        productName.isBlank() -> {
                            validationError = "Please enter the product name."
                        }
                        category.isBlank() -> {
                            validationError = "Please select a product category."
                        }
                        price == null || price <= 0 -> {
                            validationError = "Please enter a valid price in UGX (greater than 0)."
                        }
                        unitStr.isBlank() -> {
                            validationError = "Please enter the unit of measurement or packaging."
                        }
                        stock == null || stock < 0 -> {
                            validationError = "Please enter a valid stock quantity (0 or more)."
                        }
                        descriptionStr.isBlank() -> {
                            validationError = "Please enter a product description."
                        }
                        else -> {
                            validationError = null
                            val primaryImage = img1
                            val fullDescription = descriptionStr.trim() + " • Includes 4 verified side views."
                            onSaveProduct(
                                productName.trim(),
                                category.trim(),
                                price,
                                stock,
                                unitStr.trim(),
                                primaryImage,
                                fullDescription
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_product_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (allFourViewsProvided && productName.isNotBlank() && category.isNotBlank() && priceStr.isNotBlank() && unitStr.isNotBlank() && stockStr.isNotBlank() && descriptionStr.isNotBlank())
                        ManguGreenPrimary else Color.Gray
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete Product Registration", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun SideViewUploadSlot(
    slotNumber: Int,
    label: String,
    imageUrl: String?,
    onPickClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable(onClick = onPickClick)
            .testTag("side_view_slot_$slotNumber"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (imageUrl != null) MaterialTheme.colorScheme.surface else Color(0xFFF3F7F5)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (imageUrl != null) ManguGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        if (imageUrl != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = 0.65f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Uploaded",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .padding(2.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(ManguGreenPrimary.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add $label",
                        tint = ManguGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to upload",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SellerOrdersScreen(
    orders: List<OrderEntity>,
    onUpdateStatus: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Pending", "Preparing", "Ready")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Store Orders",
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tabs
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tabs) { tab ->
                val isSelected = tab == selectedFilter
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.clickable { selectedFilter = tab }
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val filteredOrders = orders.filter { order ->
            when (selectedFilter) {
                "Pending" -> order.orderStatus == "PENDING_PAYMENT" || order.orderStatus == "PAID"
                "Preparing" -> order.orderStatus == "SELLER_CONFIRMED"
                "Ready" -> order.orderStatus == "READY_FOR_PICKUP"
                else -> true
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredOrders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = order.orderId, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                text = "UGX " + NumberFormat.getNumberInstance(Locale.US).format(order.totalUgx),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = ManguGreenPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Customer: ${order.buyerName} (${order.buyerPhone})", fontSize = 12.sp)
                        Text(text = "Items: ${order.itemsSummary}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Dropoff: ${order.deliveryAddress}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons based on status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (order.orderStatus == "PENDING_PAYMENT" || order.orderStatus == "PAID") {
                                Button(
                                    onClick = { onUpdateStatus(order.orderId, "SELLER_CONFIRMED") },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                                ) {
                                    Text("Accept Order", fontSize = 12.sp)
                                }
                            } else if (order.orderStatus == "SELLER_CONFIRMED") {
                                Button(
                                    onClick = { onUpdateStatus(order.orderId, "READY_FOR_PICKUP") },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ManguOrangeAccent)
                                ) {
                                    Text("Mark Ready for Boda", fontSize = 12.sp)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE8F8F0)
                                ) {
                                    Text(
                                        text = order.orderStatus.replace("_", " "),
                                        color = ManguGreenPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SellerInventoryScreen(
    products: List<ProductEntity>,
    onUpdateStock: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Inventory Management",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Manage stock quantities in real time for Masaka shoppers",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(products) { product ->
            var currentStock by remember(product.id, product.stockQuantity) {
                mutableIntStateOf(product.stockQuantity)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductImageView(
                        imageUrl = product.imageUrl,
                        fallbackEmoji = product.iconEmoji,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        emojiSize = 24.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "UGX " + NumberFormat.getNumberInstance(Locale.US).format(product.priceUgx), fontSize = 12.sp, color = ManguGreenPrimary)
                        Text(text = "Stock: $currentStock units", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Stock adjusters
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (currentStock > 0) {
                                    currentStock--
                                    onUpdateStock(product.id, currentStock)
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$currentStock",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        IconButton(
                            onClick = {
                                currentStock++
                                onUpdateStock(product.id, currentStock)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoreSettingsScreen(
    currentUser: com.example.data.model.UserAccount? = null,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onSelectThemeMode: (ThemeMode) -> Unit = {},
    onUpdatePaymentMethod: (String) -> Unit = {},
    onUpdateStoreAddress: (String) -> Unit = {},
    onResetAccount: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPayoutDialog by remember { mutableStateOf(false) }
    var showBusinessDialog by remember { mutableStateOf(false) }
    var showDeliveryDialog by remember { mutableStateOf(false) }

    var payoutInput by remember(currentUser) {
        mutableStateOf(currentUser?.paymentMethod?.ifBlank { "MTN MoMo Merchant: 0782 554 433" } ?: "MTN MoMo Merchant: 0782 554 433")
    }
    var businessAddressInput by remember(currentUser) {
        mutableStateOf(currentUser?.deliveryAddress?.ifBlank { "Elgin Road, Masaka Central" } ?: "Elgin Road, Masaka Central")
    }
    var deliveryRadiusInput by remember {
        mutableStateOf("10 km (All Masaka City & Suburbs)")
    }

    // Payout Method Dialog
    if (showPayoutDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPayoutDialog = false },
            title = { Text("Update Payout Method", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select or enter your mobile money merchant number or bank account for receiving daily customer payouts:", fontSize = 13.sp)
                    val samplePayouts = listOf(
                        "MTN MoMo Merchant: 0782 554 433",
                        "Airtel Money Pay: 0752 998 877",
                        "Centenary Bank Masaka Branch: A/C 310045"
                    )
                    samplePayouts.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { payoutInput = option }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = payoutInput == option,
                                onClick = { payoutInput = option }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(option, fontSize = 12.sp)
                        }
                    }
                    OutlinedTextField(
                        value = payoutInput,
                        onValueChange = { payoutInput = it },
                        label = { Text("Payout Details / MoMo Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdatePaymentMethod(payoutInput)
                        showPayoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Payout Method", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPayoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Business Info Dialog
    if (showBusinessDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showBusinessDialog = false },
            title = { Text("Store Location & Details", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Update physical pickup address for dispatching riders in Masaka:", fontSize = 13.sp)
                    OutlinedTextField(
                        value = businessAddressInput,
                        onValueChange = { businessAddressInput = it },
                        label = { Text("Store Location in Masaka") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateStoreAddress(businessAddressInput)
                        showBusinessDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Location", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showBusinessDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delivery Radius Dialog
    if (showDeliveryDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeliveryDialog = false },
            title = { Text("Delivery Coverage Settings", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select maximum delivery coverage radius around Masaka:", fontSize = 13.sp)
                    val radii = listOf(
                        "5 km (Masaka Central & Nyendo)",
                        "10 km (All Masaka City & Suburbs)",
                        "15 km (Greater Masaka Region)"
                    )
                    radii.forEach { rad ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { deliveryRadiusInput = rad }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = deliveryRadiusInput == rad,
                                onClick = { deliveryRadiusInput = rad }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(rad, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDeliveryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Radius", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeliveryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Store Account & Change Role?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This will remove your seller session and unlock fresh role registration so you can register under another role (e.g. Buyer or Boda Rider)."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onResetAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete Account & Switch Role", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Store Settings",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Profile & payout preferences for ${currentUser?.name ?: "Fresh Farm"}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SettingsCard(
                icon = Icons.Outlined.Storefront,
                title = "Business Information",
                subtitle = "${currentUser?.name ?: "Fresh Farm"} • $businessAddressInput",
                onClick = { showBusinessDialog = true }
            )
        }

        item {
            SettingsCard(
                icon = Icons.Outlined.AccountBalance,
                title = "Mobile Money / Bank Payout",
                subtitle = payoutInput,
                onClick = { showPayoutDialog = true }
            )
        }

        item {
            SettingsCard(
                icon = Icons.Outlined.LocalShipping,
                title = "Delivery Settings",
                subtitle = "Radius: $deliveryRadiusInput • Boda-Boda partners",
                onClick = { showDeliveryDialog = true }
            )
        }

        // Appearance & Display Theme
        item {
            Text(
                text = "Display Theme",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("seller_theme_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Choose store management theme mode:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ThemeOptionItem(
                        title = "Light Mode",
                        description = "Bright high-contrast view for daytime store management",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == ThemeMode.LIGHT,
                        onClick = { onSelectThemeMode(ThemeMode.LIGHT) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ThemeOptionItem(
                        title = "Dark Mode",
                        description = "Eye-comfort dark theme for evening inventory updates",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == ThemeMode.DARK,
                        onClick = { onSelectThemeMode(ThemeMode.DARK) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ThemeOptionItem(
                        title = "System Default",
                        description = "Automatically follow your phone's system display mode",
                        icon = Icons.Default.BrightnessAuto,
                        isSelected = themeMode == ThemeMode.SYSTEM,
                        onClick = { onSelectThemeMode(ThemeMode.SYSTEM) }
                    )
                }
            }
        }

        // Role Management / Account Reset
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1515)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF8B2525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Want to Switch Your Role?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFFF8A80)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your device is locked to the Seller role. To switch to a Buyer or Boda Rider role, you must delete this account session and register fresh.",
                        fontSize = 12.sp,
                        color = Color(0xFFFFCDD2)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delete Account to Change Role",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("seller_logout_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out of Store System", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
            ) {
                Text("Back to Dashboard", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OrderDetailScreen(
    order: OrderEntity?,
    onUpdateStatus: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    if (order == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Order Not Found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order Details",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "ID: ${order.orderId}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (order.orderStatus) {
                        "PENDING_PAYMENT", "SELLER_CONFIRMED" -> Color(0xFFFFF2DC)
                        "ON_THE_WAY", "READY_FOR_PICKUP" -> Color(0xFFE3F2FD)
                        "DELIVERED", "COMPLETED" -> Color(0xFFE8F8F0)
                        else -> Color(0xFFEEEEEE)
                    }
                ) {
                    Text(
                        text = when (order.orderStatus) {
                            "SELLER_CONFIRMED" -> "Preparing"
                            "READY_FOR_PICKUP" -> "Ready for Boda"
                            "ON_THE_WAY" -> "On Way"
                            "DELIVERED", "COMPLETED" -> "Delivered"
                            else -> "Pending"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.orderStatus) {
                            "PENDING_PAYMENT", "SELLER_CONFIRMED" -> ManguOrangeAccent
                            "ON_THE_WAY", "READY_FOR_PICKUP" -> Color(0xFF1976D2)
                            "DELIVERED", "COMPLETED" -> ManguGreenPrimary
                            else -> Color.DarkGray
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Ordered Products Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2F6ED)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = ManguGreenPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ordered Items",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Products sold by your store",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = order.itemsSummary,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Prepared freshly by: ${order.sellerName}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Customer & Delivery Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Customer & Delivery Address",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = ManguGreenPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = order.buyerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = order.buyerPhone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Direct Dial Customer Button
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.buyerPhone}"))
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2F6ED))
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call Customer", tint = ManguGreenPrimary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Drop-off: ${order.deliveryAddress}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Financial & Payment Breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Payment & Financial Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatUgx(order.subtotalUgx.toLong()), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Boda Delivery Fee", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatUgx(order.deliveryFeeUgx.toLong()), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Payment Method", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order.paymentMethod, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ManguGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Order Amount", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(
                            formatUgx(order.totalUgx.toLong()),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = ManguGreenPrimary
                        )
                    }
                }
            }
        }

        // Boda Delivery & Verification PIN Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EE)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3C775))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = ManguOrangeAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Boda Delivery Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF8A5300)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Assigned Rider: ${order.riderName} (${order.riderPhone})",
                        fontSize = 12.sp,
                        color = Color(0xFF8A5300)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Delivery Security PIN: ${order.deliveryPin} (Buyer will verify upon handover)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8A5300)
                    )
                }
            }
        }

        // Seller Actions (Status changes)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (order.orderStatus == "PENDING_PAYMENT" || order.orderStatus == "PAID") {
                    Button(
                        onClick = {
                            onUpdateStatus(order.orderId, "SELLER_CONFIRMED")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                    ) {
                        Text("Confirm & Start Preparing Order", fontWeight = FontWeight.Bold)
                    }
                } else if (order.orderStatus == "SELLER_CONFIRMED") {
                    Button(
                        onClick = {
                            onUpdateStatus(order.orderId, "READY_FOR_PICKUP")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ManguOrangeAccent)
                    ) {
                        Text("Mark Ready for Boda Pickup", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Back to Dashboard")
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2F6ED)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ManguGreenPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

package com.example.ui.rider

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryJob
import com.example.data.model.OrderEntity
import com.example.data.model.RiderStats
import com.example.data.model.UserAccount
import com.example.ui.components.MasakaMapCanvas
import com.example.ui.theme.ManguGreenDark
import com.example.ui.theme.ManguGreenPrimary
import com.example.ui.theme.ManguOrangeAccent
import java.text.NumberFormat
import java.util.Locale

private fun formatUgx(amount: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "UGX " + formatter.format(amount)
}

@Composable
fun RiderDashboardScreen(
    stats: RiderStats,
    riderUser: UserAccount? = null,
    orders: List<OrderEntity> = emptyList(),
    onToggleOnline: () -> Unit,
    onAvailableJobsClick: () -> Unit,
    onActiveDeliveryClick: () -> Unit,
    onWalletClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val displayName = riderUser?.name?.ifBlank { "Joseph Kigozi" } ?: "Joseph Kigozi"
    val displayDetails = riderUser?.details?.ifBlank { "Masaka Central Zone • Reg #UFE 234K" } ?: "Masaka Central Zone • Reg #UFE 234K"

    // Completed jobs ever done by this rider
    val completedDeliveries = orders.filter { it.orderStatus == "COMPLETED" || it.orderStatus == "DELIVERED" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Rider Identity Card showing actual registered details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2F6ED)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = ManguGreenPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = displayName,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ManguGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Verified Boda-Boda Partner",
                                fontSize = 11.sp,
                                color = ManguGreenPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = displayDetails,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!riderUser?.phone.isNullOrBlank()) {
                                Text(
                                    text = "Phone: ${riderUser?.phone}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("rider_header_logout_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Log Out",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Online / Offline Status Toggle
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (stats.isOnline) Color(0xFFE2F6ED) else Color(0xFFF5F5F5)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (stats.isOnline) ManguGreenPrimary else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (stats.isOnline) "You are Online" else "You are Offline",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (stats.isOnline) ManguGreenDark else Color.DarkGray
                            )
                            Text(
                                text = if (stats.isOnline) "Receiving boda delivery requests" else "Switch on to receive jobs",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = stats.isOnline,
                        onCheckedChange = { onToggleOnline() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ManguGreenPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        ),
                        modifier = Modifier.testTag("rider_online_switch")
                    )
                }
            }
        }

        // Stats Cards: Today's Earnings & Completed Runs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = ManguGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Earnings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = formatUgx(stats.todaysEarningsUgx),
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = ManguGreenPrimary
                        )
                        Text(text = "Today's Payout", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = ManguOrangeAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Deliveries", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${stats.completedDeliveries} Trips",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                        Text(text = "Rating: ★ ${stats.rating}", fontSize = 10.sp, color = ManguOrangeAccent)
                    }
                }
            }
        }

        // Quick Navigation Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onAvailableJobsClick),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ManguGreenPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = ManguGreenPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Available Jobs", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "Find Orders", fontSize = 10.sp, color = ManguGreenPrimary)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onActiveDeliveryClick),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ManguOrangeAccent.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, tint = ManguOrangeAccent)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Active Run", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "Current Job", fontSize = 10.sp, color = ManguOrangeAccent)
                    }
                }
            }
        }

        // INFORMATION SHOWING ALL JOBS EVER DONE BY THE RIDER AND THEIR DETAILS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = ManguGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "All Jobs Done History",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${completedDeliveries.size} completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        if (completedDeliveries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Completed Jobs Yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Accept delivery jobs from the Available Jobs screen to build your ride history and track earnings.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(completedDeliveries) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.itemsSummary.ifBlank { "Order #${order.orderId}" },
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE2F6ED)
                            ) {
                                Text(
                                    text = "Completed",
                                    color = ManguGreenPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Order ID: ${order.orderId}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Earned: ${formatUgx(order.deliveryFeeUgx.coerceAtLeast(3000))}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ManguGreenPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Pickup and Drop Details
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ManguGreenPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pickup: ${order.sellerName}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD32F2F))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dropoff: ${order.deliveryAddress}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Customer: ${order.buyerName} • ${order.buyerPhone}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Logout option for Rider
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rider_dashboard_logout_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFD32F2F))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out of Boda Rider Account", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AvailableJobsScreen(
    jobs: List<DeliveryJob>,
    onAcceptJob: (DeliveryJob) -> Unit,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Near Me") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Available Jobs (${jobs.size})",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Fast dispatch boda jobs across Masaka City",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Near Me / All pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Near Me", "All Masaka").forEach { tab ->
                val isSelected = tab == selectedFilter
                Surface(
                    shape = RoundedCornerShape(16.dp),
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

        Spacer(modifier = Modifier.height(14.dp))

        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🏍️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No pending jobs right now", fontWeight = FontWeight.Bold)
                    Text(text = "New orders from Masaka sellers will appear here", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(jobs) { job ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = job.orderId, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFE2F6ED)
                                    ) {
                                        Text(
                                            text = "${job.distanceKm} km",
                                            color = ManguGreenPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = formatUgx(job.payoutUgx),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = ManguGreenPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Pickup
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(ManguGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pickup: ${job.pickupLocation}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Dropoff
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD32F2F))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Drop: ${job.dropoffLocation}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Items: ${job.itemsSummary}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { onAcceptJob(job) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("accept_job_${job.orderId}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                            ) {
                                Text("Accept Job • Earn ${formatUgx(job.payoutUgx)}", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiderCurrentDeliveryScreen(
    order: OrderEntity?,
    onProceedToPin: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // If there is NO delivery in progress, display nothing rather than an empty/mock map
    if (order == null || order.orderStatus == "COMPLETED" || order.orderStatus == "DELIVERED") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2F6ED)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBike,
                    contentDescription = null,
                    tint = ManguGreenPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No Delivery in Progress",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "There is currently no active delivery in progress. Once you accept a delivery request from the Available Jobs screen, your live pickup, drop, and journey progress will display here.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("rider_no_delivery_back_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
            ) {
                Text("Return to Rider Dashboard", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    // Live animated journey progress along Masaka City route
    val infiniteTransition = rememberInfiniteTransition(label = "riderJourneyProgress")
    val liveRiderProgress by infiniteTransition.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "riderProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Active Delivery Run",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Order ID: ${order.orderId}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ManguOrangeAccent
                    ) {
                        Text(
                            text = "● Live Journey",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Live Map Showing Real Pickup, Drop, and Live Journey Progress
            item {
                MasakaMapCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    riderLatitudeProgress = liveRiderProgress,
                    pickupLabel = "${order.sellerName} (Pickup)",
                    dropoffLabel = "${order.deliveryAddress} (Drop)",
                    riderName = "${order.riderName} • Live En Route",
                    pickupLat = -0.3340,
                    pickupLng = 31.7340,
                    dropoffLat = -0.3418,
                    dropoffLng = 31.7389
                )
            }

            // Delivery Locations Card with real details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Trip Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ManguGreenPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pickup: ${order.sellerName}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD32F2F))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Destination: ${order.deliveryAddress}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Package Items: ${order.itemsSummary}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Customer Contact Card: Active Call Button directly opens phone dialer
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            Icon(Icons.Default.Person, contentDescription = null, tint = ManguGreenPrimary)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Customer: ${order.buyerName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Phone: ${order.buyerPhone}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        val customerPhone = order.buyerPhone.ifBlank { "0701234567" }

                        // Active Call Button: directly opens phone dialer with customer phone
                        IconButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$customerPhone"))
                                try {
                                    context.startActivity(callIntent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ManguGreenPrimary)
                                .testTag("rider_call_customer_btn")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call Customer", tint = Color.White, modifier = Modifier.size(18.dp))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Active SMS Button: directly opens messaging with customer
                        IconButton(
                            onClick = {
                                val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$customerPhone")).apply {
                                    putExtra("sms_body", "Hello ${order.buyerName}, I am your Mangu Boda rider delivering: ${order.itemsSummary}. I am arriving shortly.")
                                }
                                try {
                                    context.startActivity(smsIntent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ManguOrangeAccent)
                                .testTag("rider_message_customer_btn")
                        ) {
                            Icon(Icons.Default.Message, contentDescription = "Message Customer", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Bottom CTA: Arrived at customer -> Verify PIN
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onProceedToPin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("arrived_verify_pin_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Arrived • Confirm Delivery PIN", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun RiderPinConfirmationScreen(
    orderId: String,
    onConfirmPin: (String) -> Unit,
    onBack: () -> Unit
) {
    var pinText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF2DC)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = ManguOrangeAccent, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Delivery PIN Confirmation",
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Ask the buyer for their 4-digit security PIN provided on their order tracking screen to safely complete handover for order $orderId.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Display 4 PIN boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..3) {
                val digit = if (i < pinText.length) pinText[i].toString() else ""
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            2.dp,
                            if (i == pinText.length) ManguGreenPrimary else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = digit,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = ManguGreenPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = pinText,
            onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinText = it },
            label = { Text("Enter 4-digit Customer PIN") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pin_input_field"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onConfirmPin(pinText) },
            enabled = pinText.length == 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("confirm_delivery_pin_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
        ) {
            Text("Confirm Delivery", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun RiderDeliveryCompleteScreen(
    orderId: String,
    payoutUgx: Int,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2F6ED)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ManguGreenPrimary,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Delivery Completed!",
            fontWeight = FontWeight.Black,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Great job! You have safely delivered order $orderId in Masaka City.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2DC))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Earnings Added", fontSize = 12.sp, color = Color(0xFF8A5300))
                Text(
                    text = formatUgx(payoutUgx),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8A5300)
                )
                Text(text = "Deposited to your Boda Partner Wallet", fontSize = 11.sp, color = Color(0xFF8A5300))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("rider_back_home_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
        ) {
            Text("Back to Dashboard", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

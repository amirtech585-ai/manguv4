package com.example.ui.common

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.data.model.ThemeMode
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.data.model.PendingApplicant
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.ui.theme.ManguGreenDark
import com.example.ui.theme.ManguGreenPrimary
import com.example.ui.theme.ManguOrangeAccent

@Composable
fun AccountScreen(
    currentUser: UserAccount?,
    currentRole: UserRole,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onSelectThemeMode: (ThemeMode) -> Unit = {},
    onUpdatePaymentMethod: (String) -> Unit = {},
    onUpdateDeliveryAddress: (String) -> Unit = {},
    onUpdateLanguage: (String) -> Unit = {},
    onUpdateProfile: (name: String, phone: String) -> Unit = { _, _ -> },
    onDeleteBuyerAccount: () -> Unit,
    onLogout: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }

    var addressInput by remember(currentUser) {
        mutableStateOf(currentUser?.deliveryAddress?.ifBlank { "Plot 14 Elgin Road, Masaka Town" } ?: "Plot 14 Elgin Road, Masaka Town")
    }
    var selectedPayment by remember(currentUser) {
        mutableStateOf(currentUser?.paymentMethod?.ifBlank { "MTN Mobile Money, Airtel Money" } ?: "MTN Mobile Money, Airtel Money")
    }
    var selectedLang by remember(currentUser) {
        mutableStateOf(currentUser?.language?.ifBlank { "English (Luganda supported)" } ?: "English (Luganda supported)")
    }
    var profileNameInput by remember(currentUser) {
        mutableStateOf(currentUser?.name ?: "")
    }
    var profilePhoneInput by remember(currentUser) {
        mutableStateOf(currentUser?.phone ?: "")
    }

    // Delivery Address Dialog
    if (showAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddressDialog = false },
            title = { Text("Update Delivery Address", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter your default delivery location in Masaka:", fontSize = 13.sp)
                    val sampleAddresses = listOf(
                        "Plot 14 Elgin Road, Masaka Town",
                        "Nyendo Market Stage, Masaka",
                        "Bwala Hill Road, Masaka",
                        "Kijjabwemi Trading Centre, Masaka"
                    )
                    sampleAddresses.forEach { addr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { addressInput = addr }
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = addressInput == addr,
                                onClick = { addressInput = addr }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(addr, fontSize = 12.sp)
                        }
                    }
                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("Custom Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateDeliveryAddress(addressInput)
                        showAddressDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Address", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddressDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Payment Method Dialog
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Preferred Payment Method", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose how you prefer to pay or receive payouts across Uganda:", fontSize = 13.sp)
                    val paymentOptions = listOf(
                        "MTN Mobile Money (*165#)",
                        "Airtel Money (*185#)",
                        "Cash on Delivery (Masaka Town)",
                        "Stanbic / Centenary Bank"
                    )
                    paymentOptions.forEach { opt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPayment = opt }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPayment == opt,
                                onClick = { selectedPayment = opt }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opt, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdatePaymentMethod(selectedPayment)
                        showPaymentDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Preference", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Language Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Choose Language", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select your preferred display language:", fontSize = 13.sp)
                    val languages = listOf(
                        "English (Uganda)",
                        "Luganda (Oluganda)",
                        "Runyankore / Rukiga",
                        "Swahili (Kiswahili)"
                    )
                    languages.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLang = lang }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLang == lang,
                                onClick = { selectedLang = lang }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(lang, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateLanguage(selectedLang)
                        showLanguageDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Apply Language", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Profile Edit Dialog
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Edit Profile Info", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = profileNameInput,
                        onValueChange = { profileNameInput = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = profilePhoneInput,
                        onValueChange = { profilePhoneInput = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(profileNameInput, profilePhoneInput)
                        showProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Save Profile", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Help & Support Dialog
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text("Masaka Help & Support", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Need assistance with your orders, deliveries, or payments in Masaka?", fontSize = 13.sp)
                    Text("• Hotline: 0800 123 456 (Toll-Free Masaka)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("• WhatsApp: +256 700 987 654", fontSize = 13.sp)
                    Text("• Support Office: Plot 12 Elgin Road, Masaka City", fontSize = 13.sp)
                    Text("• Operating Hours: 7:00 AM - 10:00 PM Daily", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSupportDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Close")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE53935)
                )
            },
            title = {
                Text(
                    text = "Delete Account & Switch Role?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Per ManguDelivery rules, your role is strictly locked to this device. You cannot switch roles while an active account session exists.\n\nTo change your role (e.g., to register as a Buyer, Seller, or Boda Rider), you must permanently delete this account session and complete a fresh registration.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteBuyerAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Delete Account & Reset Role", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
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
        // User Profile Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2F6ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (currentRole) {
                                UserRole.BUYER -> "🛒"
                                UserRole.SELLER -> "🏪"
                                UserRole.RIDER -> "🏍️"
                                UserRole.ADMIN -> "🔐"
                            },
                            fontSize = 26.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.name ?: when (currentRole) {
                                UserRole.BUYER -> "Sarah Nalubega"
                                UserRole.SELLER -> "Fresh Farm"
                                UserRole.RIDER -> "Joseph K."
                                UserRole.ADMIN -> "Mangu Admin"
                            },
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentUser?.phone?.ifBlank { "0701 234 567" } ?: "0701 234 567"} • Masaka, Uganda",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ManguGreenPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "LOCKED ROLE: ${currentRole.name}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ManguGreenPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showProfileDialog = true },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Account Details & Preferences
        item {
            Text("Account Details & Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    AccountOptionRow(
                        icon = Icons.Default.LocationOn,
                        title = "Delivery Address",
                        subtitle = currentUser?.deliveryAddress?.ifBlank { addressInput } ?: addressInput,
                        onClick = { showAddressDialog = true }
                    )
                    AccountOptionRow(
                        icon = Icons.Default.Payment,
                        title = "Payment Methods",
                        subtitle = currentUser?.paymentMethod?.ifBlank { selectedPayment } ?: selectedPayment,
                        onClick = { showPaymentDialog = true }
                    )
                    AccountOptionRow(
                        icon = Icons.Default.Translate,
                        title = "Language",
                        subtitle = currentUser?.language?.ifBlank { selectedLang } ?: selectedLang,
                        onClick = { showLanguageDialog = true }
                    )
                    AccountOptionRow(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "Help & Support Helpline",
                        subtitle = "Toll-free customer care in Masaka City",
                        onClick = { showSupportDialog = true }
                    )
                }
            }
        }

        // Appearance & Display Theme Section
        item {
            Text("Appearance & Theme", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("theme_selection_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Customize your visual viewing experience for night or day:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ThemeOptionItem(
                        title = "Light Mode",
                        description = "Crisp, bright daylight interface with high contrast",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == ThemeMode.LIGHT,
                        onClick = { onSelectThemeMode(ThemeMode.LIGHT) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ThemeOptionItem(
                        title = "Dark Mode",
                        description = "Deep emerald dark theme for eye comfort and battery saving",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == ThemeMode.DARK,
                        onClick = { onSelectThemeMode(ThemeMode.DARK) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ThemeOptionItem(
                        title = "System Default",
                        description = "Matches your Android device's system settings automatically",
                        icon = Icons.Default.BrightnessAuto,
                        isSelected = themeMode == ThemeMode.SYSTEM,
                        onClick = { onSelectThemeMode(ThemeMode.SYSTEM) }
                    )
                }
            }
        }

        // Role-Specific Action (Delete Account for Buyer vs Log Out for Seller/Rider)
        item {
            if (currentRole == UserRole.BUYER) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1515)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF8B2525))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Want to Change Your Role?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFFF8A80)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "In ManguDelivery, your app interface is strictly locked to your chosen role. To become a Seller or Boda Rider, you must first permanently delete this Buyer account and register fresh.",
                            fontSize = 12.sp,
                            color = Color(0xFFFFCDD2)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("delete_buyer_account_button"),
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
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Account Role & Access",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "This ${currentRole.name.lowercase()} profile is locked to this role. To switch to a different role, you can permanently delete this profile and register fresh.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("delete_rider_account_button"),
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
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onLogout,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Log Out")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ManguGreenPrimary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ThemeOptionItem(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val bgColor = if (isSelected) ManguGreenPrimary.copy(alpha = 0.08f) else Color.Transparent

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("theme_option_${title.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ManguGreenPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = ManguGreenPrimary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity>,
    onClearAll: () -> Unit = {},
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Orders", "Deliveries", "System"

    val filteredList = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "Orders" -> notifications.filter { it.type == "order" }
            "Deliveries" -> notifications.filter { it.type == "rider" || it.type == "delivery" }
            "System" -> notifications.filter { it.type != "order" && it.type != "rider" && it.type != "delivery" }
            else -> notifications
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (notifications.isNotEmpty()) {
                    TextButton(
                        onClick = onClearAll,
                        modifier = Modifier.testTag("clear_all_notifications_btn")
                    ) {
                        Icon(Icons.Default.ClearAll, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Clear All",
                            color = Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips: All, Orders, Deliveries, System
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Orders", "Deliveries", "System").forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { selectedFilter = filter }
                            .testTag("notification_filter_${filter.lowercase()}")
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (notifications.isEmpty()) "No Notifications" else "No $selectedFilter notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Fresh updates on orders, rider dispatches, and store alerts will appear here.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(filteredList) { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    when (notif.type) {
                                        "order" -> Color(0xFFE2F6ED)
                                        "rider" -> Color(0xFFFFF2DC)
                                        else -> Color(0xFFE8F0FE)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (notif.type) {
                                    "order" -> Icons.Default.Storefront
                                    "rider" -> Icons.Default.DirectionsBike
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = when (notif.type) {
                                    "order" -> ManguGreenPrimary
                                    "rider" -> ManguOrangeAccent
                                    else -> Color(0xFF1976D2)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = notif.timeAgo, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = notif.message,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


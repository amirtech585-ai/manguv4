package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.ManguGreenDark
import com.example.ui.theme.ManguGreenPrimary
import com.example.ui.theme.ManguOrangeAccent

@Composable
fun OnboardingRegisterScreen(
    isDarkMode: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onRegisterBuyer: (name: String, phone: String, address: String) -> Unit,
    onSubmitSellerApplication: (storeName: String, ownerName: String, phone: String, location: String) -> Unit,
    onLoginSeller: (storeNameOrPhone: String) -> Unit,
    onSubmitRiderApplication: (riderName: String, phone: String, bodaReg: String, stage: String) -> Unit,
    onLoginRider: (phoneOrReg: String) -> Unit
) {
    var selectedRoleTab by remember { mutableStateOf(UserRole.BUYER) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, end = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = onToggleTheme,
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.testTag("theme_toggle_onboarding")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                            tint = if (isDarkMode) ManguOrangeAccent else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isDarkMode) "Light Mode" else "Dark Mode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("app_logo_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ManguGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row {
                    Text(
                        text = "Mangu",
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        color = ManguGreenPrimary
                    )
                    Text(
                        text = "Delivery",
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        color = ManguOrangeAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Masaka City Marketplace & Fast Boda Delivery",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Register or Select Your Role",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Each account is locked to its specific role. Choose how you want to use the platform:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Role Selector Chips (Public user roles)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RoleSelectionPill(
                    title = "Buyer",
                    icon = "🛒",
                    isSelected = selectedRoleTab == UserRole.BUYER,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedRoleTab = UserRole.BUYER
                    }
                )
                RoleSelectionPill(
                    title = "Seller",
                    icon = "🏪",
                    isSelected = selectedRoleTab == UserRole.SELLER,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedRoleTab = UserRole.SELLER
                    }
                )
                RoleSelectionPill(
                    title = "Rider",
                    icon = "🏍️",
                    isSelected = selectedRoleTab == UserRole.RIDER,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedRoleTab = UserRole.RIDER
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Form Section per Role
        item {
            when (selectedRoleTab) {
                UserRole.BUYER -> {
                    BuyerRegistrationCard(onRegister = onRegisterBuyer)
                }
                UserRole.SELLER -> {
                    SellerRegistrationCard(
                        onSubmitApplication = onSubmitSellerApplication,
                        onLoginApproved = onLoginSeller
                    )
                }
                UserRole.RIDER -> {
                    RiderRegistrationCard(
                        onSubmitApplication = onSubmitRiderApplication,
                        onLoginApproved = onLoginRider
                    )
                }
                UserRole.ADMIN -> {
                    BuyerRegistrationCard(onRegister = onRegisterBuyer)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun RoleSelectionPill(
    title: String,
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("role_select_$title"),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A3A31))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BuyerRegistrationCard(
    onRegister: (name: String, phone: String, address: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2F6ED)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛒", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Buyer Registration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Instant registration for shopping in Masaka",
                        fontSize = 11.sp,
                        color = ManguGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Sarah Nalubega") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ManguGreenPrimary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_name_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; errorMessage = null },
                label = { Text("Phone Number (MTN / Airtel)") },
                placeholder = { Text("0701 234 567") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ManguGreenPrimary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_phone_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it; errorMessage = null },
                label = { Text("Delivery Address / Landmark") },
                placeholder = { Text("e.g. Plot 14 Elgin Road, Masaka") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = ManguGreenPrimary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_address_input"),
                singleLine = true
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name"
                    } else if (phone.isBlank()) {
                        errorMessage = "Please enter your phone number"
                    } else {
                        val finalAddress = address.ifBlank { "Masaka City, Uganda" }
                        onRegister(name, phone, finalAddress)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_buyer_register"),
                colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Create Buyer Account & Start Shopping",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun SellerRegistrationCard(
    onSubmitApplication: (storeName: String, ownerName: String, phone: String, location: String) -> Unit,
    onLoginApproved: (storeNameOrPhone: String) -> Unit
) {
    var mode by remember { mutableIntStateOf(0) } // 0 = Apply, 1 = Login
    var storeName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var loginInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF2DC)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏪", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Seller / Merchant Portal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Sellers must be registered & verified by Platform",
                        fontSize = 11.sp,
                        color = ManguOrangeAccent,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub tabs: Apply vs Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { mode = 0 },
                    color = if (mode == 0) ManguOrangeAccent else Color(0xFF223028),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "New Store Application",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (mode == 0) Color.Black else Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { mode = 1 },
                    color = if (mode == 1) ManguOrangeAccent else Color(0xFF223028),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Log In Approved Store",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (mode == 1) Color.Black else Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (mode == 0) {
                // Application Form
                Text(
                    text = "Submit your store application for verification and approval:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it; errorMessage = null },
                    label = { Text("Store / Farm Name") },
                    placeholder = { Text("e.g. Masaka Fresh Produce") },
                    leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null, tint = ManguOrangeAccent) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it; errorMessage = null },
                    label = { Text("Proprietor Name") },
                    placeholder = { Text("e.g. Kasule David") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ManguOrangeAccent) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text("Business Phone Number") },
                    placeholder = { Text("0782 554 433") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ManguOrangeAccent) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it; errorMessage = null },
                    label = { Text("Market / Street Location") },
                    placeholder = { Text("e.g. Elgin Road Central Market") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = ManguOrangeAccent) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (storeName.isBlank() || ownerName.isBlank() || phone.isBlank() || location.isBlank()) {
                            errorMessage = "Please provide all required details: store name, proprietor, phone, and location"
                        } else {
                            onSubmitApplication(storeName.trim(), ownerName.trim(), phone.trim(), location.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguOrangeAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Submit Application for Verification",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            } else {
                // Log In Form for pre-approved seller
                Text(
                    text = "If your store is already approved and active, log in with your store name or registered phone:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginInput,
                    onValueChange = { loginInput = it; errorMessage = null },
                    label = { Text("Store Name or Phone") },
                    placeholder = { Text("e.g. Fresh Farm") },
                    leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null, tint = ManguOrangeAccent) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (loginInput.isBlank()) {
                            errorMessage = "Please enter store name or phone"
                        } else {
                            onLoginApproved(loginInput)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguOrangeAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Log In to Store Dashboard",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        loginInput = "Fresh Farm"
                        onLoginApproved("Fresh Farm")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Instant Demo Login: Fresh Farm (Approved)", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun RiderRegistrationCard(
    onSubmitApplication: (riderName: String, phone: String, bodaReg: String, stage: String) -> Unit,
    onLoginApproved: (phoneOrReg: String) -> Unit
) {
    var mode by remember { mutableIntStateOf(0) } // 0 = Apply, 1 = Login
    var riderName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bodaReg by remember { mutableStateOf("") }
    var stage by remember { mutableStateOf("") }
    var loginInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2F6ED)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏍️", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Boda-Boda Rider Partner",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Riders must be verified & approved by Platform",
                        fontSize = 11.sp,
                        color = ManguGreenPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub tabs: Apply vs Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { mode = 0 },
                    color = if (mode == 0) ManguGreenPrimary else Color(0xFF223028),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "New Rider Application",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { mode = 1 },
                    color = if (mode == 1) ManguGreenPrimary else Color(0xFF223028),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Log In Approved Rider",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (mode == 0) {
                // Application Form
                Text(
                    text = "Submit your boda rider application for verification:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = riderName,
                    onValueChange = { riderName = it; errorMessage = null },
                    label = { Text("Rider Full Name") },
                    placeholder = { Text("e.g. Joseph Kigozi") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ManguGreenPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text("Phone Number") },
                    placeholder = { Text("0701 234 567") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ManguGreenPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bodaReg,
                    onValueChange = { bodaReg = it; errorMessage = null },
                    label = { Text("Motorcycle Registration No.") },
                    placeholder = { Text("e.g. UFE 234K") },
                    leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = ManguGreenPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = stage,
                    onValueChange = { stage = it; errorMessage = null },
                    label = { Text("Boda Stage in Masaka") },
                    placeholder = { Text("e.g. Nyendo Stage / Total Elgin Rd") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = ManguGreenPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (riderName.isBlank() || phone.isBlank() || bodaReg.isBlank() || stage.isBlank()) {
                            errorMessage = "Please enter all required rider details: full name, phone, motorcycle registration number, and boda stage"
                        } else {
                            onSubmitApplication(
                                riderName.trim(),
                                phone.trim(),
                                bodaReg.trim(),
                                stage.trim()
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Submit Rider Application for Verification",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            } else {
                // Log In Form
                Text(
                    text = "If your boda account is already verified and active, log in with your phone or boda reg:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginInput,
                    onValueChange = { loginInput = it; errorMessage = null },
                    label = { Text("Phone Number or Boda Reg") },
                    placeholder = { Text("e.g. 0701 234 567 or UFE 234K") },
                    leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = ManguGreenPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (loginInput.isBlank()) {
                            errorMessage = "Please enter phone or boda registration number"
                        } else {
                            onLoginApproved(loginInput)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Log In to Rider Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PendingApprovalScreen(
    role: UserRole,
    name: String,
    details: String,
    isDarkMode: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onRefresh: () -> Unit,
    onChangeAccount: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Top theme toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                onClick = onToggleTheme,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.testTag("theme_toggle_pending")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = if (isDarkMode) ManguOrangeAccent else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isDarkMode) "Light Mode" else "Dark Mode",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF2DC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = "Pending",
                        tint = ManguOrangeAccent,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Application Pending Approval",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF663600),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "STATUS: AWAITING PLATFORM APPROVAL",
                        color = ManguOrangeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Hello $name,\nyour application as a ${if (role == UserRole.SELLER) "Seller" else "Boda-Boda Rider"} has been registered.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = details,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ManguGreenPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Per platform policy, the Central Operations Team verifies and approves all store merchants and boda delivery partners through the standalone management platform before unlocking active dashboards.",
                    fontSize = 11.sp,
                    color = Color(0xFF8F9E97),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRefresh,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Check / Refresh Approval Status", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onChangeAccount,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Register Different Role / Back", fontSize = 12.sp)
                }
            }
        }
    }
}

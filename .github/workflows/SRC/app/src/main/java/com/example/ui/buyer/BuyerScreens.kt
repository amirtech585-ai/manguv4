package com.example.ui.buyer

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.ui.components.MasakaMapCanvas
import com.example.ui.components.ProductImageView
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
fun BuyerHomeScreen(
    products: List<ProductEntity>,
    searchQuery: String,
    selectedCategory: String,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit
) {
    val categories = listOf("All", "Groceries", "Electronics", "Fashion", "Home & Living")
    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.category.contains(searchQuery, ignoreCase = true) ||
                product.sellerName.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                placeholder = { Text("Search for products, stores...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = ManguGreenPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Hero Banner: Fresh & Quality Products
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .testTag("hero_banner"),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "Fresh & Quality Products in Masaka",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(0x990A452B)
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = ManguOrangeAccent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Masaka Direct",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Fresh & Quality\nProducts",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.clickable { onCategorySelect("Groceries") }
                        ) {
                            Text(
                                text = "Shop Now",
                                color = ManguGreenDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Category Pills
        item {
            Text(
                text = "Categories",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ManguGreenPrimary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .clickable { onCategorySelect(cat) }
                            .testTag("category_$cat")
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Popular Products Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Products",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "View All (${filteredProducts.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ManguGreenPrimary,
                    modifier = Modifier.clickable { onCategorySelect("All") }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Products Grid / List
        if (filteredProducts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🛒", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No products found",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Try another search or category",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredProducts.chunked(2)) { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowProducts.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductItemCard(
                                product = product,
                                onClick = { onProductClick(product) },
                                onAddToCart = { onAddToCart(product) }
                            )
                        }
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProductItemCard(
    product: ProductEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Product visual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F7F4)),
                contentAlignment = Alignment.Center
            ) {
                ProductImageView(
                    imageUrl = product.imageUrl,
                    fallbackEmoji = product.iconEmoji,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = product.name,
                    emojiSize = 48.sp
                )
                // Rating pill
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = ManguOrangeAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${product.rating}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = product.sellerName,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formatUgx(product.priceUgx),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = ManguGreenPrimary
                    )
                    Text(
                        text = product.unit,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ManguGreenPrimary)
                        .clickable(onClick = onAddToCart)
                        .testTag("add_to_cart_${product.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    product: ProductEntity,
    onAddToCart: (ProductEntity, Int) -> Unit,
    onBuyNow: (ProductEntity, Int) -> Unit,
    onBack: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }

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
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Product Hero Graphic
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8F2EC)),
                    contentAlignment = Alignment.Center
                ) {
                    ProductImageView(
                        imageUrl = product.imageUrl,
                        fallbackEmoji = product.iconEmoji,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = product.name,
                        emojiSize = 96.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Title and rating
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = ManguOrangeAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${product.rating} (${product.reviewCount} reviews)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (product.stockQuantity > 0) Color(0xFFE8F8F0) else Color(0xFFFFEBEE)
                    ) {
                        Text(
                            text = if (product.stockQuantity > 0) "In stock" else "Out of stock",
                            color = if (product.stockQuantity > 0) ManguGreenPrimary else Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price and unit
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatUgx(product.priceUgx),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = ManguGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "/ ${product.unit}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quantity selector
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Seller Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2F3EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏡", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Seller: ${product.sellerName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
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
                                text = "Masaka City • Dispatches with Boda in 15 mins",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Description
            item {
                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Bottom CTA action bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onAddToCart(product, quantity) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("add_to_cart_detail_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ManguGreenPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ManguGreenPrimary)
                ) {
                    Text("Add to Cart", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onBuyNow(product, quantity) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("buy_now_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Buy Now", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    items: List<CartItemEntity>,
    onUpdateQuantity: (CartItemEntity, Int) -> Unit,
    onRemove: (CartItemEntity) -> Unit,
    onCheckout: () -> Unit
) {
    val subtotal = items.sumOf { it.priceUgx * it.quantity }
    val deliveryFee = if (items.isNotEmpty()) 2000 else 0
    val total = subtotal + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🛒", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your Cart is Empty",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Explore fresh Masaka products and add items",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "My Cart (${items.size} items)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(items) { item ->
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
                                imageUrl = item.imageUrl,
                                fallbackEmoji = item.iconEmoji,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                emojiSize = 28.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = formatUgx(item.priceUgx),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = ManguGreenPrimary
                                )
                                Text(
                                    text = item.unit,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Stepper
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { onUpdateQuantity(item, item.quantity - 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = if (item.quantity == 1) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = "${item.quantity}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )

                                IconButton(
                                    onClick = { onUpdateQuantity(item, item.quantity + 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Summary card and Proceed button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatUgx(subtotal), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee (Boda-Boda)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatUgx(deliveryFee), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            formatUgx(total),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = ManguGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("proceed_to_checkout_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                    ) {
                        Text("Proceed to Checkout", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen(
    items: List<CartItemEntity>,
    onPlaceOrder: (String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var address by remember { mutableStateOf("Masaka, Uganda • Plot 14 Elgin Road") }
    var selectedPayment by remember { mutableStateOf("Mobile Money (MTN/Airtel)") }
    var mobileMoneyNumber by remember { mutableStateOf("0701 234 567") }
    var customerName by remember { mutableStateOf("Sarah N.") }

    val subtotal = items.sumOf { it.priceUgx * it.quantity }
    val deliveryFee = 2000
    val total = subtotal + deliveryFee

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
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Delivery Address card
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delivery Address",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Change",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ManguGreenPrimary,
                                modifier = Modifier.clickable {
                                    address = if (address.contains("Elgin")) "Nyendo Market Rd, Masaka" else "Masaka, Uganda • Plot 14 Elgin Road"
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = ManguGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = address,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Payment Methods
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Payment Method",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val methods = listOf(
                            "Mobile Money (MTN/Airtel)",
                            "Card Payment",
                            "Cash on Delivery"
                        )

                        methods.forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPayment = method }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPayment == method,
                                    onClick = { selectedPayment = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = ManguGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = method,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedPayment == method) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        if (selectedPayment.contains("Mobile Money")) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = mobileMoneyNumber,
                                onValueChange = { mobileMoneyNumber = it },
                                label = { Text("Mobile Money Phone (MTN/Airtel)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }

            // Order Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Order Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Items (${items.size})", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatUgx(subtotal), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Delivery Fee", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatUgx(deliveryFee), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(
                                formatUgx(total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = ManguGreenPrimary
                            )
                        }
                    }
                }
            }
        }

        // Place Order button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        onPlaceOrder(customerName, mobileMoneyNumber, address, selectedPayment)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("place_order_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
                ) {
                    Text("Place Order (${formatUgx(total)})", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DeliveryTrackingScreen(
    order: OrderEntity?,
    onCompleteDemo: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val displayOrder = order ?: OrderEntity(
        orderId = "#MD123456",
        buyerName = "Sarah N.",
        buyerPhone = "0701 234 567",
        deliveryAddress = "Masaka Town, Elgin Road",
        sellerName = "Fresh Farm",
        itemsSummary = "Matoke (2 kg), Rice 5kg",
        subtotalUgx = 21000,
        deliveryFeeUgx = 2000,
        totalUgx = 23000,
        paymentMethod = "Mobile Money (MTN)",
        orderStatus = "ON_THE_WAY",
        riderName = "Joseph K.",
        riderPhone = "0701 234 567",
        deliveryPin = "4829",
        etaMinutes = 12
    )

    // Smooth continuous live movement animation of the delivery boda guy along the Masaka route
    val infiniteTransition = rememberInfiniteTransition(label = "bodaMovement")
    val animatedBodaProgress by infiniteTransition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bodaProgress"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Product Name prominently displayed instead of "Order #MD123456"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayOrder.itemsSummary,
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = displayOrder.orderId,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Estimated ETA: ${displayOrder.etaMinutes} mins",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ManguOrangeAccent
                ) {
                    Text(
                        text = "Live On Way",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live Masaka Map Canvas with continuous moving boda guy and real pickup & drop details
        item {
            MasakaMapCanvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                riderLatitudeProgress = animatedBodaProgress,
                pickupLabel = "${displayOrder.sellerName} (Masaka)",
                dropoffLabel = displayOrder.deliveryAddress,
                riderName = "${displayOrder.riderName} • Live Moving",
                pickupLat = -0.3340,
                pickupLng = 31.7340,
                dropoffLat = -0.3418,
                dropoffLng = 31.7389
            )
        }

        // Rider card with active Call & Message buttons opening device apps
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2F6ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBike,
                            contentDescription = null,
                            tint = ManguGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = displayOrder.riderName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Boda-Boda Partner • ${displayOrder.riderPhone}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val riderPhoneContact = displayOrder.riderPhone.ifBlank { "0701234567" }

                    // Calling Button: automatically opens phone dialer with boda guy contact
                    IconButton(
                        onClick = {
                            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$riderPhoneContact"))
                            try {
                                context.startActivity(callIntent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEBF7F0))
                            .testTag("buyer_call_boda_button")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call Boda Guy", tint = ManguGreenPrimary, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Messaging Button: automatically opens SMS app with boda guy contact
                    IconButton(
                        onClick = {
                            val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$riderPhoneContact")).apply {
                                putExtra("sms_body", "Hello ${displayOrder.riderName}, this is regarding my Mangu delivery: ${displayOrder.itemsSummary}")
                            }
                            try {
                                context.startActivity(smsIntent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF4E5))
                            .testTag("buyer_message_boda_button")
                    ) {
                        Icon(Icons.Default.Message, contentDescription = "Message Boda Guy", tint = ManguOrangeAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Delivery PIN Card: unique generated pin for every delivery
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EE)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3C775))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ManguOrangeAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Your Delivery PIN: ${displayOrder.deliveryPin}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color(0xFF804D00)
                        )
                        Text(
                            text = "Each delivery generates a unique PIN. Provide this code to the boda guy upon arrival to confirm package handover.",
                            fontSize = 11.sp,
                            color = Color(0xFF6B4810),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Live Delivery Milestones Tracking (Automatically tracked live)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Live Delivery Progress",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE2F6ED)
                        ) {
                            Text(
                                text = "● Live Tracking",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ManguGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    TimelineStep(title = "Order Confirmed", subtitle = "${displayOrder.sellerName} prepared items", isDone = true)
                    TimelineStep(title = "Dispatched with Boda", subtitle = "${displayOrder.riderName} picked up package", isDone = true)
                    TimelineStep(title = "Boda Guy Approaching", subtitle = "Moving actively along Masaka streets", isDone = false, isActive = true)
                    TimelineStep(title = "Arrived & Handover", subtitle = "Verify using PIN: ${displayOrder.deliveryPin}", isDone = false, isLast = true)
                }
            }
        }

        // Clean Back / Close button (no simulate button)
        item {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("back_to_home_from_tracking"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
            ) {
                Text("Return to Marketplace", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TimelineStep(
    title: String,
    subtitle: String,
    isDone: Boolean,
    isActive: Boolean = false,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isDone -> ManguGreenPrimary
                            isActive -> ManguOrangeAccent
                            else -> Color(0xFFE0E0E0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                } else if (isActive) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(30.dp)
                        .background(if (isDone) ManguGreenPrimary else Color(0xFFE0E0E0))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)) {
            Text(
                text = title,
                fontWeight = if (isDone || isActive) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp,
                color = if (isDone || isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun OrderCompleteScreen(
    orderId: String,
    onViewDetails: () -> Unit,
    onRateReview: () -> Unit,
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
                .size(84.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2F6ED)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = ManguGreenPrimary,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Order Completed!",
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your order $orderId has been delivered successfully to your location in Masaka.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRateReview,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("rate_review_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ManguGreenPrimary)
        ) {
            Text("Rate & Review", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("back_to_home_btn"),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ManguGreenPrimary)
        ) {
            Text("Back to Home", fontWeight = FontWeight.Bold, color = ManguGreenPrimary)
        }
    }
}

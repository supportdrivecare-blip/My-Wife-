package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.UserProfile
import com.example.ui.theme.RosePrimary

@Composable
fun FavoritesScreen(
    profile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${profile.wifeName}'s Pocket Guide",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Never second-guess her favorite drink, food, or sizes again!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.testTag("edit_favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit All",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Section 1: Food & Drinks
        item {
            PreferenceSectionCard(
                sectionTitle = "Food & Drink Favorites",
                sectionIcon = Icons.Default.LocalCafe,
                items = listOf(
                    PreferenceItemData("Favorite Beverage", profile.favoriteDrink, "Her go-to chai, coffee, or juice"),
                    PreferenceItemData("Favorite Food / Cuisine", profile.favoriteFood, "The dish that always brings a smile")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section 2: Flowers & Aesthetics
        item {
            PreferenceSectionCard(
                sectionTitle = "Flowers & Aesthetics",
                sectionIcon = Icons.Default.LocalFlorist,
                items = listOf(
                    PreferenceItemData("Favorite Flowers", profile.favoriteFlower, "Perfect for surprise deliveries or romantic dates"),
                    PreferenceItemData("Favorite Color", profile.favoriteColor.ifBlank { "Soft Pink / Pastel" }, "For gifts, clothes, and wrapping")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section 3: Shopping & Sizes Cheat-Sheet
        item {
            PreferenceSectionCard(
                sectionTitle = "Shopping & Sizes Cheat-Sheet",
                sectionIcon = Icons.Default.ShoppingBag,
                items = listOf(
                    PreferenceItemData("Ring Size", profile.ringSize.ifBlank { "Not set yet" }, "Finger ring size for jewelry gifts"),
                    PreferenceItemData("Shoe Size", profile.shoeSize.ifBlank { "Not set yet" }, "Shoe measurement"),
                    PreferenceItemData("Dress / Kurti Size", profile.dressSize.ifBlank { "Not set yet" }, "Outfit & clothes fit")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        item {
            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("update_all_preferences_button"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit All Preferences & Dates")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class PreferenceItemData(
    val title: String,
    val value: String,
    val description: String
)

@Composable
fun PreferenceSectionCard(
    sectionTitle: String,
    sectionIcon: ImageVector,
    items: List<PreferenceItemData>,
    onCopy: (String, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = RosePrimary.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = sectionIcon,
                            contentDescription = null,
                            tint = RosePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.value.ifBlank { "Tap Edit to set" },
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (item.value.isNotBlank() && item.value != "Not set yet") {
                        IconButton(
                            onClick = { onCopy(item.title, item.value) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy ${item.title}",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                if (index < items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

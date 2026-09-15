package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Park
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
import com.example.data.WifeProfile
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.RoseSecondary

@Composable
fun FavoritesScreen(
    profile: WifeProfile,
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
                        BoxContentCenter {
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
                            text = "${profile.name}'s Pocket Guide",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Never second-guess her coffee order or shopping sizes again!",
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

        // Section 1: Shopping & Sizes Cheat-Sheet
        item {
            PreferenceSectionCard(
                sectionTitle = "Shopping & Sizes Cheat-Sheet",
                sectionIcon = Icons.Default.ShoppingBag,
                items = listOf(
                    PreferenceItemData("Ring Size", profile.ringSize, "US 6 / Indian 12"),
                    PreferenceItemData("Shoe Size", profile.shoeSize, "UK 5 / EU 38"),
                    PreferenceItemData("Dress / Kurti Size", profile.dressSize, "Medium / 38")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section 2: Beverages & Food
        item {
            PreferenceSectionCard(
                sectionTitle = "Food, Chai & Sweet Cravings",
                sectionIcon = Icons.Default.LocalCafe,
                items = listOf(
                    PreferenceItemData("Chai / Coffee Preference", profile.chaiCoffee, "How she takes her cup"),
                    PreferenceItemData("Comfort Food", profile.comfortFood, "What makes her instantly happy"),
                    PreferenceItemData("Favorite Dessert", profile.favoriteDessert, "Sweet tooth craving")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section 3: Aesthetics & Scents
        item {
            PreferenceSectionCard(
                sectionTitle = "Flowers, Colors & Scents",
                sectionIcon = Icons.Default.Park,
                items = listOf(
                    PreferenceItemData("Favorite Flowers", profile.favoriteFlowers, "Great for surprise deliveries"),
                    PreferenceItemData("Favorite Color", profile.favoriteColor, "When picking clothes or gift wrap"),
                    PreferenceItemData("Perfume & Scents", profile.favoritePerfume, "Fragrance notes she loves")
                ),
                onCopy = { label, value ->
                    clipboardManager.setText(AnnotatedString("$label: $value"))
                    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section 4: Mood Fixers & Entertainment
        item {
            PreferenceSectionCard(
                sectionTitle = "Mood Fixer & Entertainment",
                sectionIcon = Icons.Default.Mood,
                items = listOf(
                    PreferenceItemData("When She's Stressed / Upset", profile.moodFixer, "Emergency care instructions"),
                    PreferenceItemData("Favorite Song / Movie", profile.songOrMovie, "For car rides and cozy evenings")
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
                Text("Edit All Preferences")
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
                    BoxContentCenter {
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

                    if (item.value.isNotBlank()) {
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

@Composable
fun BoxContentCenter(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

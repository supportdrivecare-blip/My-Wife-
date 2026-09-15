package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.DatePlannerScreen
import com.example.ui.EditProfileDialog
import com.example.ui.FavoritesScreen
import com.example.ui.HomeScreen
import com.example.ui.LoveNotesScreen
import com.example.ui.WifeViewModel
import com.example.ui.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RosePrimary

enum class AppTab(val label: String, val icon: ImageVector, val title: String) {
    HOME("Home", Icons.Default.Favorite, "Meri Wife ❤️"),
    FAVORITES("Preferences", Icons.Default.LocalCafe, "Her Favorites & Sizes"),
    WISHLIST("Gifts", Icons.Default.CardGiftcard, "Secret Gift Wishlist"),
    NOTES("Love Notes", Icons.Default.FavoriteBorder, "Pyar Bhari Baatein"),
    DATES("Dates", Icons.Default.Nightlife, "Romantic Date Planner")
}

class MainActivity : ComponentActivity() {
    private val viewModel: WifeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var currentTab by remember { mutableStateOf(AppTab.HOME) }
                var showEditDialog by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = RosePrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = currentTab.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("bottom_navigation_bar")
                        ) {
                            AppTab.entries.forEach { tab ->
                                val isSelected = currentTab == tab
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.label
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
                            when (tab) {
                                AppTab.HOME -> HomeScreen(
                                    uiState = uiState,
                                    onEditProfileClick = { showEditDialog = true },
                                    onToggleCareItem = { id, completed ->
                                        viewModel.toggleCareItem(id, completed)
                                    },
                                    onNavigateToFavorites = { currentTab = AppTab.FAVORITES }
                                )

                                AppTab.FAVORITES -> FavoritesScreen(
                                    profile = uiState.profile,
                                    onEditClick = { showEditDialog = true }
                                )

                                AppTab.WISHLIST -> WishlistScreen(
                                    gifts = uiState.gifts,
                                    onAddGift = { title, cat, price, occasion, notes ->
                                        viewModel.addGift(title, cat, price, occasion, notes)
                                    },
                                    onToggleFulfilled = { gift ->
                                        viewModel.toggleGiftFulfilled(gift)
                                    },
                                    onDeleteGift = { gift ->
                                        viewModel.deleteGift(gift)
                                    }
                                )

                                AppTab.NOTES -> LoveNotesScreen(
                                    notes = uiState.loveNotes,
                                    wifeNickname = uiState.profile.nickname,
                                    wifePhone = uiState.profile.phoneNumber,
                                    onAddNote = { title, msg, hindi, cat ->
                                        viewModel.addLoveNote(title, msg, hindi, cat)
                                    },
                                    onToggleFavorite = { note ->
                                        viewModel.toggleNoteFavorite(note)
                                    },
                                    onDeleteNote = { note ->
                                        viewModel.deleteLoveNote(note)
                                    }
                                )

                                AppTab.DATES -> DatePlannerScreen(
                                    dateIdeas = uiState.dateIdeas,
                                    onAddIdea = { title, desc, loc ->
                                        viewModel.addDateIdea(title, desc, loc)
                                    },
                                    onToggleCompleted = { idea ->
                                        viewModel.toggleDateIdeaCompleted(idea)
                                    },
                                    onDeleteIdea = { idea ->
                                        viewModel.deleteDateIdea(idea)
                                    }
                                )
                            }
                        }

                        if (showEditDialog) {
                            EditProfileDialog(
                                profile = uiState.profile,
                                onDismiss = { showEditDialog = false },
                                onSave = { updatedProfile ->
                                    viewModel.updateProfile(updatedProfile)
                                    showEditDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compatible Greeting composable for Robolectric and Roborazzi test compatibility.
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

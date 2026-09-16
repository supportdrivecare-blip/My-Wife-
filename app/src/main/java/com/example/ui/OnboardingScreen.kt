package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.RoseSecondary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onSaveProfile: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var husbandName by remember { mutableStateOf("") }
    var wifeName by remember { mutableStateOf("") }
    var anniversaryDate by remember { mutableStateOf<LocalDate?>(null) }
    var birthdayDate by remember { mutableStateOf<LocalDate?>(null) }
    var favoriteDrink by remember { mutableStateOf("") }
    var favoriteFlower by remember { mutableStateOf("") }
    var favoriteFood by remember { mutableStateOf("") }

    var showAnniversaryPicker by remember { mutableStateOf(false) }
    var showBirthdayPicker by remember { mutableStateOf(false) }

    val displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val popularDrinks = listOf("Masala Chai", "Adrak Chai", "Cappuccino", "Cold Coffee", "Fresh Juice", "Green Tea")
    val popularFlowers = listOf("Red Roses", "Jasmine / Mogra", "White Lilies", "Tulips", "Orchids")
    val popularFoods = listOf("Biryani", "Pizza", "Butter Chicken & Naan", "Pasta", "Pani Puri / Chaat", "Karahi")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Romantic Welcome Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_welcome_card"),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_couple),
                    contentDescription = "Welcome Couple",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFFF4081),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Meri Wife App",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Text(
                        text = "Har pyare husband ki companion app ❤️",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Intro message
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Khush Aamdeed!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Apni biwi ki zaroori tareekhein aur pasandeeda cheezein darj karein taake app aapke liye customize ho sake.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "1. Bunyadi Maloomat (Basic Info)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Husband Name
                OutlinedTextField(
                    value = husbandName,
                    onValueChange = { husbandName = it },
                    label = { Text("Aapka Naam (Husband's Name) *") },
                    placeholder = { Text("e.g. Ali / Farhan") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = RosePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_husband_name"),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Wife Name
                OutlinedTextField(
                    value = wifeName,
                    onValueChange = { wifeName = it },
                    label = { Text("Biwi Ka Naam (Wife's Name) *") },
                    placeholder = { Text("e.g. Ayesha / Sara") },
                    leadingIcon = {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = RosePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_wife_name"),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "2. Yaadgar Tareekhein (Special Dates)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Anniversary Date Picker Trigger
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { showAnniversaryPicker = true }
                        .testTag("btn_pick_anniversary"),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = RosePrimary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Shaadi / Anniversary Ki Date *",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = anniversaryDate?.format(displayFormatter) ?: "Date chunein (Tap to pick)",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (anniversaryDate != null) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (anniversaryDate != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Birthday Date Picker Trigger
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { showBirthdayPicker = true }
                        .testTag("btn_pick_birthday"),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cake,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Biwi Ki Birthday Ki Date *",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = birthdayDate?.format(displayFormatter) ?: "Date chunein (Tap to pick)",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (birthdayDate != null) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (birthdayDate != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "3. Uski Pasand (Her Favorites)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Favorite Drink
                OutlinedTextField(
                    value = favoriteDrink,
                    onValueChange = { favoriteDrink = it },
                    label = { Text("Biwi Ka Pasandeeda Mashroob *") },
                    placeholder = { Text("e.g. Masala Chai, Cold Coffee") },
                    leadingIcon = {
                        Icon(Icons.Default.LocalCafe, contentDescription = null, tint = RosePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_favorite_drink"),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    popularDrinks.forEach { drink ->
                        FilterChip(
                            selected = favoriteDrink.equals(drink, ignoreCase = true),
                            onClick = { favoriteDrink = drink },
                            label = { Text(drink, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Favorite Flower
                OutlinedTextField(
                    value = favoriteFlower,
                    onValueChange = { favoriteFlower = it },
                    label = { Text("Biwi Ka Pasandeeda Phool *") },
                    placeholder = { Text("e.g. Red Roses, Jasmine/Mogra") },
                    leadingIcon = {
                        Icon(Icons.Default.LocalFlorist, contentDescription = null, tint = RosePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_favorite_flower"),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    popularFlowers.forEach { flower ->
                        FilterChip(
                            selected = favoriteFlower.equals(flower, ignoreCase = true),
                            onClick = { favoriteFlower = flower },
                            label = { Text(flower, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Favorite Food
                OutlinedTextField(
                    value = favoriteFood,
                    onValueChange = { favoriteFood = it },
                    label = { Text("Biwi Ka Pasandeeda Khana *") },
                    placeholder = { Text("e.g. Biryani, Pizza, Pasta") },
                    leadingIcon = {
                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = RosePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_favorite_food"),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    popularFoods.forEach { food ->
                        FilterChip(
                            selected = favoriteFood.equals(food, ignoreCase = true),
                            onClick = { favoriteFood = food },
                            label = { Text(food, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                if (husbandName.isBlank()) {
                    Toast.makeText(context, "Meherbani farma kar apna naam darj karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (wifeName.isBlank()) {
                    Toast.makeText(context, "Meherbani farma kar biwi ka naam darj karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (anniversaryDate == null) {
                    Toast.makeText(context, "Shaadi ki date select karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (birthdayDate == null) {
                    Toast.makeText(context, "Birthday date select karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (favoriteDrink.isBlank()) {
                    Toast.makeText(context, "Pasandeeda mashroob darj karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (favoriteFlower.isBlank()) {
                    Toast.makeText(context, "Pasandeeda phool darj karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (favoriteFood.isBlank()) {
                    Toast.makeText(context, "Pasandeeda khana darj karein", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val profile = UserProfile(
                    id = 1,
                    husbandName = husbandName.trim(),
                    wifeName = wifeName.trim(),
                    anniversaryDate = anniversaryDate!!.format(DateTimeFormatter.ISO_DATE),
                    birthdayDate = birthdayDate!!.format(DateTimeFormatter.ISO_DATE),
                    favoriteDrink = favoriteDrink.trim(),
                    favoriteFlower = favoriteFlower.trim(),
                    favoriteFood = favoriteFood.trim(),
                    nickname = "Jaanu / Meri Begum"
                )
                onSaveProfile(profile)
                Toast.makeText(context, "Welcome! Profile kamiyabi se save ho gayi ❤️", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("save_onboarding_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Save Aur App Shuru Karein ❤️",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Anniversary Date Picker Dialog
    if (showAnniversaryPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = anniversaryDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
                ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showAnniversaryPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            anniversaryDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        showAnniversaryPicker = false
                    }
                ) {
                    Text("Select", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAnniversaryPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Birthday Date Picker Dialog
    if (showBirthdayPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = birthdayDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
                ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showBirthdayPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            birthdayDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        showBirthdayPicker = false
                    }
                ) {
                    Text("Select", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBirthdayPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

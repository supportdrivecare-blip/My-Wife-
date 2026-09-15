package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.WifeProfile

@Composable
fun EditProfileDialog(
    profile: WifeProfile,
    onDismiss: () -> Unit,
    onSave: (WifeProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var nickname by remember { mutableStateOf(profile.nickname) }
    var phone by remember { mutableStateOf(profile.phoneNumber) }
    var weddingYear by remember { mutableStateOf(profile.weddingYear.toString()) }
    var weddingMonth by remember { mutableStateOf(profile.weddingMonth.toString()) }
    var weddingDay by remember { mutableStateOf(profile.weddingDay.toString()) }
    var birthMonth by remember { mutableStateOf(profile.birthMonth.toString()) }
    var birthDay by remember { mutableStateOf(profile.birthDay.toString()) }
    var chaiCoffee by remember { mutableStateOf(profile.chaiCoffee) }
    var ringSize by remember { mutableStateOf(profile.ringSize) }
    var shoeSize by remember { mutableStateOf(profile.shoeSize) }
    var dressSize by remember { mutableStateOf(profile.dressSize) }
    var flowers by remember { mutableStateOf(profile.favoriteFlowers) }
    var moodFixer by remember { mutableStateOf(profile.moodFixer) }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Wife's Profile",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Wife's Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_name_input")
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Sweet Nickname (Begum, Jaanu, Rani)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_nickname_input")
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone / WhatsApp (Optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_phone_input")
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Wedding Date (Anniversary)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = weddingDay,
                        onValueChange = { weddingDay = it.filter { c -> c.isDigit() } },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = weddingMonth,
                        onValueChange = { weddingMonth = it.filter { c -> c.isDigit() } },
                        label = { Text("Month (1-12)") },
                        modifier = Modifier.weight(1.2f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = weddingYear,
                        onValueChange = { weddingYear = it.filter { c -> c.isDigit() } },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1.3f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Her Birthday",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = birthDay,
                        onValueChange = { birthDay = it.filter { c -> c.isDigit() } },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = birthMonth,
                        onValueChange = { birthMonth = it.filter { c -> c.isDigit() } },
                        label = { Text("Month (1-12)") },
                        modifier = Modifier.weight(1.2f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Favorites Quick Notes",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = chaiCoffee,
                    onValueChange = { chaiCoffee = it },
                    label = { Text("Chai / Coffee Preference") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = ringSize,
                        onValueChange = { ringSize = it },
                        label = { Text("Ring Size") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = shoeSize,
                        onValueChange = { shoeSize = it },
                        label = { Text("Shoe Size") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dressSize,
                    onValueChange = { dressSize = it },
                    label = { Text("Dress / Kurti Size") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = flowers,
                    onValueChange = { flowers = it },
                    label = { Text("Favorite Flowers") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = moodFixer,
                    onValueChange = { moodFixer = it },
                    label = { Text("Best Mood Fixer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val wYear = weddingYear.toIntOrNull() ?: profile.weddingYear
                    val wMonth = (weddingMonth.toIntOrNull() ?: profile.weddingMonth).coerceIn(1, 12)
                    val wDay = (weddingDay.toIntOrNull() ?: profile.weddingDay).coerceIn(1, 31)
                    val bMonth = (birthMonth.toIntOrNull() ?: profile.birthMonth).coerceIn(1, 12)
                    val bDay = (birthDay.toIntOrNull() ?: profile.birthDay).coerceIn(1, 31)

                    onSave(
                        profile.copy(
                            name = name.ifBlank { "Priya" },
                            nickname = nickname.ifBlank { "Begum" },
                            phoneNumber = phone.trim(),
                            weddingYear = wYear,
                            weddingMonth = wMonth,
                            weddingDay = wDay,
                            birthMonth = bMonth,
                            birthDay = bDay,
                            chaiCoffee = chaiCoffee,
                            ringSize = ringSize,
                            shoeSize = shoeSize,
                            dressSize = dressSize,
                            favoriteFlowers = flowers,
                            moodFixer = moodFixer
                        )
                    )
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Save Details")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

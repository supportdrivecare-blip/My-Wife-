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
import com.example.data.UserProfile

@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var husbandName by remember { mutableStateOf(profile.husbandName) }
    var wifeName by remember { mutableStateOf(profile.wifeName) }
    var nickname by remember { mutableStateOf(profile.nickname) }
    var phone by remember { mutableStateOf(profile.phoneNumber) }
    var favoriteDrink by remember { mutableStateOf(profile.favoriteDrink) }
    var favoriteFlower by remember { mutableStateOf(profile.favoriteFlower) }
    var favoriteFood by remember { mutableStateOf(profile.favoriteFood) }
    var ringSize by remember { mutableStateOf(profile.ringSize) }
    var shoeSize by remember { mutableStateOf(profile.shoeSize) }
    var dressSize by remember { mutableStateOf(profile.dressSize) }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Maloomat Tabdeel Karein",
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
                    value = husbandName,
                    onValueChange = { husbandName = it },
                    label = { Text("Aapka Naam (Husband)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = wifeName,
                    onValueChange = { wifeName = it },
                    label = { Text("Biwi Ka Naam") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_name_input")
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Pyara Naam (Misaal: Begum, Jaanu, Rani)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_nickname_input")
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone / WhatsApp (Ikhtiyari)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wife_phone_input")
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pasandeeda Cheezein",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = favoriteDrink,
                    onValueChange = { favoriteDrink = it },
                    label = { Text("Pasandeeda Mashroob") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = favoriteFlower,
                    onValueChange = { favoriteFlower = it },
                    label = { Text("Pasandeeda Phool") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = favoriteFood,
                    onValueChange = { favoriteFood = it },
                    label = { Text("Pasandeeda Khana") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = ringSize,
                        onValueChange = { ringSize = it },
                        label = { Text("Ungli Ka Naap (Ring)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = shoeSize,
                        onValueChange = { shoeSize = it },
                        label = { Text("Jootay Ka Naap (Shoe)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dressSize,
                    onValueChange = { dressSize = it },
                    label = { Text("Kapron Ka Naap (Dress)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        profile.copy(
                            husbandName = husbandName.trim(),
                            wifeName = wifeName.trim(),
                            nickname = nickname.trim(),
                            phoneNumber = phone.trim(),
                            favoriteDrink = favoriteDrink.trim(),
                            favoriteFlower = favoriteFlower.trim(),
                            favoriteFood = favoriteFood.trim(),
                            ringSize = ringSize.trim(),
                            shoeSize = shoeSize.trim(),
                            dressSize = dressSize.trim()
                        )
                    )
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Mehfooz Karein")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Mansookh Karein")
            }
        }
    )
}

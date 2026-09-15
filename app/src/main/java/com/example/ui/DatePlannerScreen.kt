package com.example.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.DateIdea
import com.example.ui.theme.RosePrimary

@Composable
fun DatePlannerScreen(
    dateIdeas: List<DateIdea>,
    onAddIdea: (String, String, String) -> Unit,
    onToggleCompleted: (DateIdea) -> Unit,
    onDeleteIdea: (DateIdea) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("All") }

    val locations = listOf("All", "Home", "Outdoor", "Dining")
    val filteredIdeas = if (selectedLocation == "All") dateIdeas else dateIdeas.filter { it.locationType.equals(selectedLocation, ignoreCase = true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = RosePrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_date_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Date Idea")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                locations.forEach { loc ->
                    FilterChip(
                        selected = selectedLocation == loc,
                        onClick = { selectedLocation = loc },
                        label = { Text(loc, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("filter_date_${loc.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("date_ideas_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredIdeas, key = { it.id }) { idea ->
                    DateIdeaCard(
                        idea = idea,
                        onToggle = { onToggleCompleted(idea) },
                        onDelete = { onDeleteIdea(idea) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if (showAddDialog) {
        AddDateIdeaDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, desc, loc ->
                onAddIdea(title, desc, loc)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DateIdeaCard(
    idea: DateIdea,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("date_card_${idea.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (idea.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (idea.isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (idea.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = if (idea.isCompleted) "Mark not completed" else "Mark completed",
                    tint = if (idea.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = idea.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (idea.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = if (idea.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = idea.locationType,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = idea.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                )
            }

            if (idea.isUserCreated) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete date idea",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddDateIdeaDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var locationType by remember { mutableStateOf("Outdoor") }

    val locations = listOf("Home", "Outdoor", "Dining")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Plan a Romantic Date", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Date Idea Title") },
                    placeholder = { Text("e.g. Moonlight Rooftop Dinner") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("date_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Setting / Style", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    locations.forEach { loc ->
                        FilterChip(
                            selected = locationType == loc,
                            onClick = { locationType = loc },
                            label = { Text(loc, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Details / How to make it special") },
                    placeholder = { Text("e.g. Surprise her after work with flowers...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title.trim(), description.trim(), locationType)
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.testTag("save_date_button")
            ) {
                Text("Add Date")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

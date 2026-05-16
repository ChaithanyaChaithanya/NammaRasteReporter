package com.example.nammarastereporter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusTrackerScreen(
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var ticketIdInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Track Your Report",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Enter your ticket ID to check the current status of your report.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = ticketIdInput,
            onValueChange = { ticketIdInput = it },
            label = { Text("Ticket ID") },
            placeholder = { Text("e.g. NR-A1B2C3D4") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { 
                if (ticketIdInput.isNotBlank()) {
                    onSearchClick(ticketIdInput.trim())
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = ticketIdInput.isNotBlank()
        ) {
            Text("Search Status")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onBackClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

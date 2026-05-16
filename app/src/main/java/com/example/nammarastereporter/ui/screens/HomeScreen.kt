package com.example.nammarastereporter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onReportClick: () -> Unit,
    onTrackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to NammaRaste", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onReportClick,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Report Issue")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onTrackClick,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Track Status")
        }
    }
}

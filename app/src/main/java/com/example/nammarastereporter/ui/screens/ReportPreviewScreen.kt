package com.example.nammarastereporter.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.nammarastereporter.viewmodel.ReportViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@SuppressLint("MissingPermission")
@Composable
fun ReportPreviewScreen(
    imageUri: Uri,
    viewModel: ReportViewModel,
    onReportSubmitted: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedIssueType by remember { mutableStateOf("Pothole") }
    val issueTypes = listOf("Pothole", "Broken Streetlight")
    var isLocating by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Report Preview", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Captured Issue",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Select Issue Type:", style = MaterialTheme.typography.bodyLarge)
        
        issueTypes.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = (type == selectedIssueType),
                    onClick = { selectedIssueType = type }
                )
                Text(text = type, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (viewModel.isSubmitting.value || isLocating) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLocating = true
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).addOnSuccessListener { location ->
                        isLocating = false
                        if (location != null) {
                            val ticketId = viewModel.submitReport(
                                issueType = selectedIssueType,
                                latitude = location.latitude,
                                longitude = location.longitude,
                                imageUri = imageUri.toString()
                            )
                            onReportSubmitted(ticketId)
                        } else {
                            Toast.makeText(context, "Could not get location. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        isLocating = false
                        Toast.makeText(context, "Location error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Report")
            }
        }
    }
}

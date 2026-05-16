package com.example.nammarastereporter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nammarastereporter.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatusResultScreen(
    ticketId: String?,
    onBackClick: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val report by viewModel.reportStatus.collectAsState()

    LaunchedEffect(ticketId) {
        ticketId?.let { viewModel.trackReport(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Report Status",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (report != null) {
            val r = report!!
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ticket ID: ${r.ticketId}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Image(
                        painter = rememberAsyncImagePainter(r.imageUri),
                        contentDescription = "Issue Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailItem(label = "Issue Type", value = r.issueType)
                    DetailItem(label = "Location", value = "${r.latitude}, ${r.longitude}")
                    DetailItem(label = "Status", value = r.status, isStatus = true)
                    
                    val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(r.timestamp))
                    DetailItem(label = "Reported On", value = date)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (ticketId == null) {
                    Text("Invalid Ticket ID")
                } else {
                    CircularProgressIndicator()
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onBackClick() },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, isStatus: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isStatus) FontWeight.Bold else FontWeight.Normal,
            color = if (isStatus) {
                when (value.lowercase()) {
                    "pending" -> MaterialTheme.colorScheme.error
                    "in progress" -> MaterialTheme.colorScheme.primary
                    "resolved" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                    else -> MaterialTheme.colorScheme.onSurface
                }
            } else MaterialTheme.colorScheme.onSurface
        )
    }
}

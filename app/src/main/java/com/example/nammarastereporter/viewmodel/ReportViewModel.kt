package com.example.nammarastereporter.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammarastereporter.data.Report
import com.example.nammarastereporter.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _capturedImageUri = mutableStateOf<Uri?>(null)
    val capturedImageUri: State<Uri?> = _capturedImageUri

    private val _reportStatus = MutableStateFlow<Report?>(null)
    val reportStatus: StateFlow<Report?> = _reportStatus

    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    fun setCapturedImage(uri: Uri) {
        _capturedImageUri.value = uri
    }

    fun submitReport(
        issueType: String,
        latitude: Double,
        longitude: Double,
        imageUri: String
    ): String {
        val ticketId = "NR-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val report = Report(
            ticketId = ticketId,
            imageUri = imageUri,
            issueType = issueType,
            latitude = latitude,
            longitude = longitude,
            timestamp = System.currentTimeMillis(),
            status = "Pending"
        )

        viewModelScope.launch {
            _isSubmitting.value = true
            repository.insertReport(report)
            _isSubmitting.value = false
        }
        return ticketId
    }

    fun trackReport(ticketId: String) {
        viewModelScope.launch {
            _reportStatus.value = repository.getReportByTicketId(ticketId)
        }
    }
}

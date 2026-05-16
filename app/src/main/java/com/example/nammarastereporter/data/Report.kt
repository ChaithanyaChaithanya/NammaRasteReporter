package com.example.nammarastereporter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: String,
    val imageUri: String,
    val issueType: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val status: String
)

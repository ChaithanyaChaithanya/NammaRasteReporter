package com.example.nammarastereporter.repository

import com.example.nammarastereporter.data.Report
import com.example.nammarastereporter.data.ReportDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepository @Inject constructor(
    private val reportDao: ReportDao
) {
    suspend fun insertReport(report: Report) {
        reportDao.insertReport(report)
    }

    suspend fun getReportByTicketId(ticketId: String): Report? {
        return reportDao.getReportByTicketId(ticketId)
    }

    fun getAllReports(): Flow<List<Report>> {
        return reportDao.getAllReports()
    }
}

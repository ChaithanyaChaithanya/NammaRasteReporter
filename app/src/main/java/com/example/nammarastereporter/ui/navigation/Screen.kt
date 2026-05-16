package com.example.nammarastereporter.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object ReportPreview : Screen("preview/{imageUri}") {
        fun createRoute(imageUri: String) = "preview/$imageUri"
    }
    object StatusTracker : Screen("status_tracker")
    object TicketConfirmation : Screen("ticket_confirmation/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket_confirmation/$ticketId"
    }
    object StatusResult : Screen("status_result/{ticketId}") {
        fun createRoute(ticketId: String) = "status_result/$ticketId"
    }
}

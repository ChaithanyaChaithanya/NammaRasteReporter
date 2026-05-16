package com.example.nammarastereporter

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nammarastereporter.ui.navigation.Screen
import com.example.nammarastereporter.ui.screens.*
import com.example.nammarastereporter.ui.theme.NammaRasteReporterTheme
import com.example.nammarastereporter.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NammaRasteReporterTheme {
                val navController = rememberNavController()
                val viewModel: ReportViewModel = hiltViewModel()

                var hasPermissions by remember { mutableStateOf(false) }
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    hasPermissions = permissions.values.all { it }
                    if (!hasPermissions) {
                        Toast.makeText(this, "Permissions required to use the app", Toast.LENGTH_LONG).show()
                    }
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onReportClick = { navController.navigate(Screen.Camera.route) },
                                onTrackClick = { navController.navigate(Screen.StatusTracker.route) }
                            )
                        }
                        composable(Screen.Camera.route) {
                            CameraScreen(
                                onImageCaptured = { uri ->
                                    val encodedUri = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())
                                    navController.navigate(Screen.ReportPreview.createRoute(encodedUri))
                                },
                                onError = {
                                    Toast.makeText(this@MainActivity, "Camera Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        composable(
                            route = Screen.ReportPreview.route,
                            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val encodedUri = backStackEntry.arguments?.getString("imageUri") ?: ""
                            ReportPreviewScreen(
                                imageUri = Uri.parse(encodedUri),
                                viewModel = viewModel,
                                onReportSubmitted = { ticketId ->
                                    navController.navigate(Screen.TicketConfirmation.createRoute(ticketId)) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }
                        composable(
                            route = Screen.TicketConfirmation.route,
                            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
                            TicketConfirmationScreen(
                                ticketId = ticketId,
                                onBackToHome = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Screen.StatusTracker.route) {
                            StatusTrackerScreen(
                                onSearchClick = { ticketId ->
                                    navController.navigate(Screen.StatusResult.createRoute(ticketId))
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.StatusResult.route,
                            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
                            StatusResultScreen(
                                ticketId = ticketId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

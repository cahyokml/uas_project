package com.example.uas_project.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas_project.viewmodel.EventViewModel
import com.example.uas_project.ui.screens.EventDetailScreen
import com.example.uas_project.ui.screens.EventFormScreen
import com.example.uas_project.ui.screens.EventListScreen

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{id}"
    const val DETAIL_BASE = "detail"
    const val CREATE = "create?eventId={eventId}"
    const val CREATE_BASE = "create"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(eventViewModel: EventViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            EventListScreen(
                viewModel = eventViewModel,
                onOpenDetail = { id -> navController.navigate("${Routes.DETAIL_BASE}/$id") },
                onCreate = { navController.navigate("${Routes.CREATE_BASE}?eventId=-1") }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            EventDetailScreen(
                id = id,
                viewModel = eventViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { eid -> navController.navigate("${Routes.CREATE_BASE}?eventId=$eid") }
            )
        }

        composable(
            route = Routes.CREATE,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val eid = backStackEntry.arguments?.getInt("eventId") ?: -1
            val eventId = if (eid > 0) eid else null
            EventFormScreen(eventId = eventId, onSaved = { navController.popBackStack() }, viewModel = eventViewModel)
        }
    }
}

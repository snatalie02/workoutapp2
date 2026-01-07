package com.example.workoutapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.ui.auth.LoginScreen
import com.example.workoutapp.ui.auth.RegisterScreen
import com.example.workoutapp.ui.exercise.AddExerciseScreen
import com.example.workoutapp.ui.exercise.ExerciseScreen
import com.example.workoutapp.ui.friends.AddFriendsScreen
import com.example.workoutapp.ui.home.HomeScreen
import com.example.workoutapp.ui.streak.StreakScreen
import com.example.workoutapp.viewmodel.*
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    authStore: AuthStore,
    token: String,
    username: String,
    authViewModel: AuthViewModel,
    friendsViewModel: FriendsViewModel,
    streakViewModel: StreakViewModel,
    workoutViewModel: WorkoutViewModel
) {
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = startDestination) {

        // Auth Screens
        composable("login") {
            LoginScreen(
                vm = authViewModel,
                navigateHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                navigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                vm = authViewModel,
                navigateHome = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                navigateToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                username = username,
                navigateToExercise = { navController.navigate("exercise") },
                navigateToAddFriends = { navController.navigate("friends") },
                onLogout = {
                   authViewModel.logout {
                       navController.navigate("login"){
                           popUpTo(0) {inclusive = true}
                       }
                   }
                }
            )
        }

        composable("exercise") {
            ExerciseScreen(
                token = token,
                username = username,
                navigateHome = { navController.navigate("home") },
                navigateToAddFriends = { navController.navigate("friends") },
                naviateToPickExercise = { navController.navigate("pick_exercise") },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                viewModel = workoutViewModel
            )
        }

        composable("pick_exercise") {
            AddExerciseScreen(
                navigateBack = { navController.popBackStack() },
                viewModel = workoutViewModel,
                token = token,
                onAddExercise = { workoutItem, time, reps ->
                    workoutViewModel.addExercise(token, workoutItem, time, reps)
                    navController.popBackStack()
                }
            )
        }

        composable("friends") {
            AddFriendsScreen(
                vm = friendsViewModel,
                token = token,
                navigateHome = { navController.navigate("home") },
                navigateToExercise = { navController.navigate("exercise") },
                navigateToStreak = { friendId, friendUsername ->
                    navController.navigate("streak/$friendId/$friendUsername")
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(
            route = "streak/{friendId}/{friendUsername}",
            arguments = listOf(
                navArgument("friendId") { type = NavType.IntType },
                navArgument("friendUsername") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getInt("friendId") ?: 0
            val friendUsername = backStackEntry.arguments?.getString("friendUsername") ?: ""

            StreakScreen(
                friendId = friendId,
                friendUsername = friendUsername,
                token = token,
                viewModel = streakViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

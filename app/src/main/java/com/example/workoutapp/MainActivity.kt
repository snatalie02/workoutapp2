package com.example.workoutapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.network.ApiClient
import com.example.workoutapp.repository.AuthRepository
import com.example.workoutapp.repository.FriendsRepository
import com.example.workoutapp.repository.StreakRepository
import com.example.workoutapp.ui.auth.LoginScreen
import com.example.workoutapp.ui.auth.RegisterScreen
import com.example.workoutapp.ui.friends.AddFriendsScreen
import com.example.workoutapp.ui.home.HomeScreen
import com.example.workoutapp.ui.streak.StreakScreen
import com.example.workoutapp.viewmodel.AuthViewModel
import com.example.workoutapp.viewmodel.FriendsViewModel
import com.example.workoutapp.viewmodel.StreakViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authStore = AuthStore(this)

        setContent {
            // Simple theme wrapper
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutApp(authStore)
                }
            }
        }
    }
}

@Composable
fun WorkoutApp(authStore: AuthStore) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // Get token from AuthStore
    val token by authStore.getToken.collectAsState(initial = "")
    val username by authStore.getUsername.collectAsState(initial = "User")

    // Setup API and Repositories
    val apiService = ApiClient.instance
    val authRepository = AuthRepository()
    val friendsRepository = FriendsRepository()
    val streakRepository = StreakRepository(apiService)

    // Initialize ViewModels with Factories
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, authStore)
    )

    val friendsViewModel: FriendsViewModel = viewModel(
        factory = FriendsViewModelFactory(friendsRepository)
    )

    val streakViewModel: StreakViewModel = viewModel(
        factory = StreakViewModelFactory(streakRepository)
    )

    LaunchedEffect(token) {
        Log.d("AUTH_DEBUG", "Token sekarang = '$token'")
        if (token.isNotEmpty()) {
            Log.d("AUTH_DEBUG", "Navigasi ke Home")
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            Log.d("AUTH_DEBUG", "Navigasi ke Login")
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }



    // Determine start destination based on token
//    val startDestination = if (token.isNotEmpty()) "home" else "login"

//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ==================== AUTH SCREENS ====================
        composable("login") {
            LoginScreen(
                vm = authViewModel,
                navigateHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                navigateToRegister = {
                    navController.navigate("register")
                }
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
                navigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== HOME SCREEN ====================
//        composable("home") {
//            HomeScreen(
//                token = token,
//                navigateToExercise = { navController.navigate("exercise") },
//                navigateToAddFriends = { navController.navigate("friends") },
//                onLogout = {
//                    scope.launch {
//                        authStore.clearToken()
//                        navController.navigate("login") {
//                            popUpTo(0) { inclusive = true }
//                        }
//                    }
//                }
//            )
//        }
//        composable("home") {
//            HomeScreen(
//                username = username, // ⬅️ ganti token → username
//                navigateToExercise = { navController.navigate("exercise") },
//                navigateToAddFriends = { navController.navigate("friends") },
//                onLogout = {
//                    scope.launch {
//                        authStore.clearToken()
//                        navController.navigate("login") {
//                            popUpTo(0) { inclusive = true }
//                        }
//                    }
//                }
//            )
//        }
        composable("home") {
            HomeScreen(
                username = username,
                navigateToExercise = { navController.navigate("exercise") },
                navigateToAddFriends = { navController.navigate("friends") },
                onLogout = {
//                    authViewModel.logout()
                }
            )
        }



        // ==================== FRIENDS SCREEN ====================
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
                    scope.launch {
                        authStore.clearToken()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ==================== STREAK SCREEN (MICHELLE) ====================
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

        // ==================== EXERCISE SCREEN (Optional) ====================
        composable("exercise") {
            // ExerciseScreen(...) - Punya teman lain
            // Sementara redirect ke home kalau belum ada
//            HomeScreen(
//                token = token,
//                navigateToExercise = { },
//                navigateToAddFriends = { navController.navigate("friends") },
//                onLogout = {
//                    scope.launch {
//                        authStore.clearToken()
//                        navController.navigate("login") {
//                            popUpTo(0) { inclusive = true }
//                        }
//                    }
//                }
//            )
                HomeScreen(
                    username = username, // ⬅️ ganti token → username
                    navigateToExercise = { navController.navigate("exercise") },
                    navigateToAddFriends = { navController.navigate("friends") },
                    onLogout = {
                        scope.launch {
                            authStore.clearToken()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

        }
}

// ==================== VIEW MODEL FACTORIES ====================

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val authStore: AuthStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, authStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class FriendsViewModelFactory(
    private val repository: FriendsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class StreakViewModelFactory(
    private val repository: StreakRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreakViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StreakViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
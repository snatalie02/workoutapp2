package com.example.workoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.navigation.NavigationGraph
import com.example.workoutapp.network.ApiClient
import com.example.workoutapp.repository.AuthRepository
import com.example.workoutapp.repository.FriendsRepository
import com.example.workoutapp.repository.StreakRepository
import com.example.workoutapp.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authStore = AuthStore(this)

        setContent {
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

    // Collect token and username from AuthStore
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

    val workoutViewModel: WorkoutViewModel = viewModel()

    // Determine start destination based on token
    val startDestination = if (token.isNotEmpty()) "home" else "login"

    // Navigation Graph handles all routing
    NavigationGraph(
        navController = navController,
        startDestination = startDestination,
        authStore = authStore,
        token = token,
        username = username,
        authViewModel = authViewModel,
        friendsViewModel = friendsViewModel,
        streakViewModel = streakViewModel,
        workoutViewModel = workoutViewModel
    )
}


//package com.example.workoutapp
//
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.*
//
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//
//import androidx.navigation.compose.*
//
//import com.example.workoutapp.datastore.AuthStore
//import com.example.workoutapp.network.ApiClient
//import com.example.workoutapp.repository.AuthRepository
//import com.example.workoutapp.repository.FriendsRepository
//import com.example.workoutapp.repository.StreakRepository
//import com.example.workoutapp.ui.auth.LoginScreen
//import com.example.workoutapp.ui.auth.RegisterScreen
//
//import com.example.workoutapp.ui.exercise.AddExerciseScreen
//import com.example.workoutapp.ui.exercise.ExerciseScreen
//
//import com.example.workoutapp.ui.friends.AddFriendsScreen
//import com.example.workoutapp.ui.home.HomeScreen
//import com.example.workoutapp.ui.streak.StreakScreen
//import com.example.workoutapp.viewmodel.AuthViewModel
//import com.example.workoutapp.viewmodel.FriendsViewModel
//
//import com.example.workoutapp.viewmodel.StreakViewModel
//import com.example.workoutapp.viewmodel.WorkoutViewModel
//import kotlinx.coroutines.launch
//
//
//class MainActivity : ComponentActivity() {
//
//    private lateinit var authStore: AuthStore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        authStore = AuthStore(this)
//
//        setContent {
////            <<<<<<< HEAD
//            // Simple theme wrapper
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    WorkoutApp(authStore)
////                    ====== =
//                    val nav = rememberNavController()
//                    // Create a navigation controller, Save it in memory so it doesn't get recreated , Let you use it to navigate between screens
//
//                    val authVM = AuthViewModel(AuthRepository(), store)
//
//                    val friendsVM = FriendsViewModel(FriendsRepository())
//
//                    val token by store.getToken.collectAsState(initial = "")
//                    // store.getToken  : go to AuthStore file and returns a Flow<String>
//                    // collectAsState : turns that Flow into a State<String>
//                    // (initial = "") : Before DataStore finishes loading (takes a long time), token = empty string
//                    // output : token is now a String not a Flow, (just open : empty string "", if alrd login/reg haven't logout token contains)
//
//                    val username by store.getUsername.collectAsState(initial = "User")
//
//                    val startDestination = if (token.isBlank()) "login" else "home"
//                    // jika token "" empty masuk view login, jika (masih login) tidak langsung view home
//
//                    val workoutVM: WorkoutViewModel = viewModel()
//
//                    NavHost(navController = nav, startDestination = startDestination) {
//
//                        composable("register") {
//                            RegisterScreen(
//                                vm = authVM,
//                                navigateToLogin = { nav.navigate("login") },
//                                navigateHome = { nav.navigate("home") }
//                            )
//                        }
//
//                        // composable (route naming to screen) diff with @composable (define func inside as UI)
//                        composable("login") {
//                            LoginScreen(
//                                vm = authVM,
//                                navigateHome = { nav.navigate("home") },
//                                navigateToRegister = { nav.navigate("register") }
//                            )
//                        }
//
//                        composable("home") {
//                            HomeScreen(
//                                username = username,
//                                navigateToExercise = { nav.navigate("exercise") },
//                                navigateToAddFriends = { nav.navigate("add_friends") },
//                                onLogout = {
//                                    authVM.logout {
//                                        nav.navigate("login") {
//                                            popUpTo(nav.graph.startDestinationId) {
//                                                inclusive = true
//                                            }
//                                            // When a user logs out, you don’t want them to press “Back” and go back to the home screen.
//                                            //This popUpTo(...) ensures: HomeScreen → LoginScreen, press back → app exits
//                                        }
//                                    }
//                                }
//                            )
//                        }
//
//                        composable("exercise") {
//                            ExerciseScreen(
//                                token = token,
//                                username = username,
//                                navigateHome = { nav.navigate("home") },
//                                navigateToAddFriends = { nav.navigate("add_friends") },
//                                onLogout = {
//                                    authVM.logout {
//                                        nav.navigate("login") {
//                                            popUpTo(nav.graph.startDestinationId) {
//                                                inclusive = true
//                                            }
//                                        }
//                                    }
//                                },
//                                naviateToPickExercise = { nav.navigate("pick_exercise") },
//                                viewModel = workoutVM
//                            )
//                        }
//
//                        composable("pick_exercise") {
//                            AddExerciseScreen(
//                                navigateBack = { nav.popBackStack() },
//                                viewModel = workoutVM,
//                                token = token,
//                                onAddExercise = { workoutItem, time, reps ->
//                                    workoutVM.addExercise(token, workoutItem, time, reps)
//                                    nav.popBackStack()
//                                }
//                            )
//                        }
//
//                        // BAGIAN : SHARON
//                        composable("add_friends") {
//                            AddFriendsScreen(
//                                vm = friendsVM,
//                                token = token,
//                                navigateHome = { nav.navigate("home") },
//                                navigateToExercise = { nav.navigate("exercise") },
//                                onLogout = {
//                                    authVM.logout {
//                                        nav.navigate("login") {
//                                            popUpTo(nav.graph.startDestinationId) {
//                                                inclusive = true
//                                            }
//                                        }
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        @Composable
//        fun WorkoutApp(authStore: AuthStore) {
//            val navController = rememberNavController()
//            val scope = rememberCoroutineScope()
//
//            // Get token from AuthStore
//            val token by authStore.getToken.collectAsState(initial = "")
//            val username by authStore.getUsername.collectAsState(initial = "User")
//
//            // Setup API and Repositories
//            val apiService = ApiClient.instance
//            val authRepository = AuthRepository()
//            val friendsRepository = FriendsRepository()
//            val streakRepository = StreakRepository(apiService)
//
//            // Initialize ViewModels with Factories
//            val authViewModel: AuthViewModel = viewModel(
//                factory = AuthViewModelFactory(authRepository, authStore)
//            )
//
//            val friendsViewModel: FriendsViewModel = viewModel(
//                factory = FriendsViewModelFactory(friendsRepository)
//            )
//
//            val streakViewModel: StreakViewModel = viewModel(
//                factory = StreakViewModelFactory(streakRepository)
//            )
//
//            LaunchedEffect(token) {
//                Log.d("AUTH_DEBUG", "Token sekarang = '$token'")
//                if (token.isNotEmpty()) {
//                    Log.d("AUTH_DEBUG", "Navigasi ke Home")
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                } else {
//                    Log.d("AUTH_DEBUG", "Navigasi ke Login")
//                    navController.navigate("login") {
//                        popUpTo(0) { inclusive = true }
//                    }
//                }
//            }
//
//
//            // Determine start destination based on token
////    val startDestination = if (token.isNotEmpty()) "home" else "login"
//
////    NavHost(
////        navController = navController,
////        startDestination = startDestination
////    ) {
//            NavHost(
//                navController = navController,
//                startDestination = "login"
//            ) {
//
//                // ==================== AUTH SCREENS ====================
//                composable("login") {
//                    LoginScreen(
//                        vm = authViewModel,
//                        navigateHome = {
//                            navController.navigate("home") {
//                                popUpTo("login") { inclusive = true }
//                            }
//                        },
//                        navigateToRegister = {
//                            navController.navigate("register")
//                        }
//                    )
//                }
//
//                composable("register") {
//                    RegisterScreen(
//                        vm = authViewModel,
//                        navigateHome = {
//                            navController.navigate("home") {
//                                popUpTo("register") { inclusive = true }
//                            }
//                        },
//                        navigateToLogin = {
//                            navController.popBackStack()
//                        }
//                    )
//                }
//
//                // ==================== HOME SCREEN ====================
////        composable("home") {
////            HomeScreen(
////                token = token,
////                navigateToExercise = { navController.navigate("exercise") },
////                navigateToAddFriends = { navController.navigate("friends") },
////                onLogout = {
////                    scope.launch {
////                        authStore.clearToken()
////                        navController.navigate("login") {
////                            popUpTo(0) { inclusive = true }
////                        }
////                    }
////                }
////            )
////        }
////        composable("home") {
////            HomeScreen(
////                username = username, // ⬅️ ganti token → username
////                navigateToExercise = { navController.navigate("exercise") },
////                navigateToAddFriends = { navController.navigate("friends") },
////                onLogout = {
////                    scope.launch {
////                        authStore.clearToken()
////                        navController.navigate("login") {
////                            popUpTo(0) { inclusive = true }
////                        }
////                    }
////                }
////            )
////        }
//                composable("home") {
//                    HomeScreen(
//                        username = username,
//                        navigateToExercise = { navController.navigate("exercise") },
//                        navigateToAddFriends = { navController.navigate("friends") },
//                        onLogout = {
////                    authViewModel.logout()
//                        }
//                    )
//                }
//
//
//                // ==================== FRIENDS SCREEN ====================
//                composable("friends") {
//                    AddFriendsScreen(
//                        vm = friendsViewModel,
//                        token = token,
//                        navigateHome = { navController.navigate("home") },
//                        navigateToExercise = { navController.navigate("exercise") },
//                        navigateToStreak = { friendId, friendUsername ->
//                            navController.navigate("streak/$friendId/$friendUsername")
//                        },
//                        onLogout = {
//                            scope.launch {
//                                authStore.clearToken()
//                                navController.navigate("login") {
//                                    popUpTo(0) { inclusive = true }
//                                }
//                            }
//                        }
//                    )
//                }
//
//                // ==================== STREAK SCREEN (MICHELLE) ====================
//                composable(
//                    route = "streak/{friendId}/{friendUsername}",
//                    arguments = listOf(
//                        navArgument("friendId") { type = NavType.IntType },
//                        navArgument("friendUsername") { type = NavType.StringType }
//                    )
//                ) { backStackEntry ->
//                    val friendId = backStackEntry.arguments?.getInt("friendId") ?: 0
//                    val friendUsername = backStackEntry.arguments?.getString("friendUsername") ?: ""
//
//                    StreakScreen(
//                        friendId = friendId,
//                        friendUsername = friendUsername,
//                        token = token,
//                        viewModel = streakViewModel,
//                        onBack = { navController.popBackStack() }
//                    )
//                }
//
//                // ==================== EXERCISE SCREEN (Optional) ====================
//                composable("exercise") {
//                    // ExerciseScreen(...) - Punya teman lain
//                    // Sementara redirect ke home kalau belum ada
////            HomeScreen(
////                token = token,
////                navigateToExercise = { },
////                navigateToAddFriends = { navController.navigate("friends") },
////                onLogout = {
////                    scope.launch {
////                        authStore.clearToken()
////                        navController.navigate("login") {
////                            popUpTo(0) { inclusive = true }
////                        }
////                    }
////                }
////            )
//                    HomeScreen(
//                        username = username, // ⬅️ ganti token → username
//                        navigateToExercise = { navController.navigate("exercise") },
//                        navigateToAddFriends = { navController.navigate("friends") },
//                        onLogout = {
//                            scope.launch {
//                                authStore.clearToken()
//                                navController.navigate("login") {
//                                    popUpTo(0) { inclusive = true }
//                                }
//                            }
//                        }
//                    )
//                }
//
//            }
//        }
//
//// ==================== VIEW MODEL FACTORIES ====================
//
//        class AuthViewModelFactory(
//            private val repository: AuthRepository,
//            private val authStore: AuthStore
//        ) : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
//                    @Suppress("UNCHECKED_CAST")
//                    return AuthViewModel(repository, authStore) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        }
//
//        class FriendsViewModelFactory(
//            private val repository: FriendsRepository
//        ) : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
//                    @Suppress("UNCHECKED_CAST")
//                    return FriendsViewModel(repository) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        }
//
//        class StreakViewModelFactory(
//            private val repository: StreakRepository
//        ) : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(StreakViewModel::class.java)) {
//                    @Suppress("UNCHECKED_CAST")
//                    return StreakViewModel(repository) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        }
//    }}
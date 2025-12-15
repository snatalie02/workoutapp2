package com.example.workoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.repository.AuthRepository
import com.example.workoutapp.repository.FriendsRepository
import com.example.workoutapp.ui.auth.LoginScreen
import com.example.workoutapp.ui.auth.RegisterScreen
import com.example.workoutapp.ui.exercise.ExerciseScreen
import com.example.workoutapp.ui.friends.AddFriendsScreen
import com.example.workoutapp.ui.home.HomeScreen
import com.example.workoutapp.viewmodel.AuthViewModel
import com.example.workoutapp.viewmodel.FriendsViewModel

class MainActivity : ComponentActivity() {
    // MainActivity : first screen that launches when app start
    // ComponentActivity() : Mainctivity inheriting the ComponentActivity class (ability to show UI, works with viewmodels etc)
    // Saat app dibuka, Android menjalankan class ini.
    override fun onCreate(savedInstanceState: Bundle?) {
        // onCreate : function in ComponentActivity  (first func android calls when your screen starts.)
        // override : overdiding the onCreate with my own code
        // savedInstanceState: Bundle? : remember ui (text input, scroll position, etc) when rotate screen, app goes to background and back, etc.
        super.onCreate(savedInstanceState)
        // super.onCreate : call the parent class of function onCreate (ComponentActivity) because need initial setups (look at ComponentActivity above)

        val store = AuthStore(this)
        // variable store can : save token, load token , clear token, load username
        // AuthStore(this) : go to file AuthStore (give MainActivity as context)
        // output : It creates a new AuthStore object (token & username)

        setContent {
            val nav = rememberNavController()
            // Create a navigation controller, Save it in memory so it doesn't get recreated , Let you use it to navigate between screens

            val authVM = AuthViewModel(AuthRepository(), store)

            val friendsVM = FriendsViewModel(FriendsRepository())

            val token by store.getToken.collectAsState(initial = "")
            // store.getToken  : go to AuthStore file and returns a Flow<String>
            // collectAsState : turns that Flow into a State<String>
            // (initial = "") : Before DataStore finishes loading (takes a long time), token = empty string
            // output : token is now a String not a Flow, (just open : empty string "", if alrd login/reg haven't logout token contains)

            val username by store.getUsername.collectAsState(initial = "User")

            val startDestination = if (token.isBlank()) "login" else "home"
            // jika token "" empty masuk view login, jika (masih login) tidak langsung view home

            NavHost(navController = nav, startDestination = startDestination) {

                composable("register") {
                    RegisterScreen(
                        vm = authVM,
                        navigateToLogin = { nav.navigate("login") },
                        navigateHome = { nav.navigate("home") }
                    )
                }

                // composable (route naming to screen) diff with @composable (define func inside as UI)
                composable("login") {
                    LoginScreen(
                        vm = authVM,
                        navigateHome = { nav.navigate("home") },
                        navigateToRegister = { nav.navigate("register") }
                    )
                }

                composable("home") {
                    HomeScreen(
                        username = username,
                        navigateToExercise = { nav.navigate("exercise") },
                        navigateToAddFriends = { nav.navigate("add_friends") },
                        onLogout = {
                            authVM.logout {
                                nav.navigate("login") {
                                    popUpTo(nav.graph.startDestinationId) { inclusive = true }
                                    // When a user logs out, you don’t want them to press “Back” and go back to the home screen.
                                    //This popUpTo(...) ensures: HomeScreen → LoginScreen, press back → app exits
                                }
                            }
                        }
                    )
                }

                // BAGIAN : SHARON
                composable("exercise") {
                    ExerciseScreen(
                        navigateHome = { nav.navigate("home") },
                        navigateToAddFriends = { nav.navigate("add_friends") }
                    )
                }

                // BAGIAN : SHARON
                composable("add_friends") {
                    AddFriendsScreen(
                        vm = friendsVM,
                        token = token,
                        navigateHome = { nav.navigate("home") },
                        navigateToExercise = { nav.navigate("exercise") },
                        onLogout = {
                            authVM.logout {
                                nav.navigate("login") {
                                    popUpTo(nav.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

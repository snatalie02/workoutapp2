package com.example.workoutapp.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.workoutapp.ui.home.BottomNavBar

@Composable
fun ExerciseScreen(
    navigateHome: () -> Unit,
    navigateToAddFriends: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {



        Spacer(Modifier.weight(1f))

        // composable ada di home ( shat tidak perlu buat navigation lagi lengkapi di file ini saja )
        BottomNavBar(
            current = "exercise",
            navigateHome = navigateHome,
            navigateToExercise = { /* already here */ },
            navigateToAddFriends = navigateToAddFriends
        )
    }
}



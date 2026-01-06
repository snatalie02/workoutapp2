package com.example.workoutapp.ui.exercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.ui.home.BottomNavBar
import com.example.workoutapp.viewmodel.UserExercise
import com.example.workoutapp.viewmodel.WorkoutViewModel

@Composable
fun ExerciseScreen(
    token: String,
    username: String,
    navigateHome: () -> Unit,
    navigateToAddFriends: () -> Unit,
    naviateToPickExercise: () -> Unit,
    onLogout: () -> Unit,
    viewModel: WorkoutViewModel
) {
    val exercises = viewModel.userExercises.collectAsState().value

    LaunchedEffect(token) {
        if (token.isNotEmpty()){
            viewModel.loadHistory(token)
        }else{
            viewModel.clearData()
        }
    }

    val groupedExercises = exercises.groupBy {
        viewModel.formatDateForHeader(it.timeStamp)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 15.dp, end = 15.dp)
        ) {
            Text(
                text = username.ifEmpty { "User" },
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterEnd),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_logout_24),
                    contentDescription = "Logout",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
        Text(
            "Exercise List",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                naviateToPickExercise()
            },
            modifier = Modifier
                .size(65.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_add_24),
                contentDescription = "Add",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(20.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (groupedExercises.isEmpty()) {
                item {
                    Text(
                        "No exercises yet. Click + to add.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                groupedExercises.forEach { (dateHeader, exercisesInDate)->
                    item {
                        Text(
                            text = dateHeader,
                            color = Color(0xFF7CFC00),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
                        )
                    }
                    itemsIndexed(exercisesInDate) { index, exercise ->
                        ExerciseCard(index = index + 1, exercise = exercise)
                        if (index < exercisesInDate.size - 1) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }
        }

        BottomNavBar(
            current = "exercise",
            navigateHome = navigateHome,
            navigateToExercise = { },
            navigateToAddFriends = navigateToAddFriends
        )
    }
}

@Composable
fun ExerciseCard(index: Int, exercise: UserExercise) {
    val caloriesBurned = exercise.calories

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF7CFC00), RoundedCornerShape(10.dp))
            .background(Color.Black, RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF333333), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = "$index. ${exercise.name}",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Time : ${exercise.time} min",
                color = Color.White,
                fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Rep : ${exercise.reps}x",
                color = Color.White,
                fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Calories : $caloriesBurned kcal",
                color = Color(0xFF7CFC00),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}
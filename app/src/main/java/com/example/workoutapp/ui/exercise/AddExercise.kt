package com.example.workoutapp.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.model.WorkoutItem
import com.example.workoutapp.viewmodel.WorkoutViewModel

@Composable
fun AddExerciseScreen(
    viewModel: WorkoutViewModel,
    token: String,
    navigateBack: () -> Unit,
    onAddExercise: (WorkoutItem, String, String) -> Unit
) {
    val workouts by viewModel.workoutList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var timeInput by remember { mutableStateOf("") }
    var repInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedWorkout by remember { mutableStateOf<WorkoutItem?>(null) }

    val filteredList = workouts.filter {
        it.workout_name.contains(searchQuery, ignoreCase = true)
    }

    val DarkBackground = Color(0xFF121212)
    val CardGray = Color(0xFF2C2C2C)
    val TextGray = Color(0xFFB0B0B0)
    val GreenAccent = Color(0xFF8BC34A)

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.getWorkouts(token)
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 15.dp, bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White)

                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "INPUT",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (selectedWorkout != null) {
                        onAddExercise(selectedWorkout!!, timeInput, repInput)
                    }
                },
                enabled = selectedWorkout != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenAccent,
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    "ADD",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            CustomInput(
                value = timeInput,
                onValueChange = { timeInput = it },
                label = "Time : (In Minute) 1 "
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomInput(
                value = repInput,
                onValueChange = { repInput = it },
                label = "Rep : (User Input) 3"
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text("Search Exercise...",
                        color = TextGray)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardGray.copy(alpha = 0.5f),
                    unfocusedContainerColor = CardGray.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = GreenAccent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenAccent)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { item ->
                        val isSelected = selectedWorkout?.id == item.id

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        color = if (isSelected) CardGray.copy(alpha = 0.8f) else CardGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = if (isSelected) 1.dp else 0.dp,
                                        color = if (isSelected) TextGray else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        selectedWorkout = if (isSelected) null else item
                                    }
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.workout_name,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = GreenAccent
                                    )
                                }
                            }

                            if (isSelected) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CardGray, shape = RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = item.description ?: "Tidak ada deskripsi",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomInput(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(label, color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF2C2C2C),
            unfocusedContainerColor = Color(0xFF2C2C2C),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}
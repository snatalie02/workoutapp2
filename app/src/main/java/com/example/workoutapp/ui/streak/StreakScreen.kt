package com.example.workoutapp.ui.streak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workoutapp.viewmodel.StreakViewModel
import com.example.workoutapp.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen(
    friendId: Int,
    friendUsername: String,
    token: String,
    viewModel: StreakViewModel,
    onBack: () -> Unit
) {
    val streakStatus by viewModel.streakStatus.collectAsStateWithLifecycle()
    val checkInResult by viewModel.checkInResult.collectAsStateWithLifecycle()

    LaunchedEffect(friendId) {
        viewModel.getStreakStatus(token, friendId)
    }

    LaunchedEffect(checkInResult) {
        if (checkInResult is UiState.Success) {
            viewModel.getStreakStatus(token, friendId)
            viewModel.resetCheckInResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = friendUsername,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1C1E)
                )
            )
        },
        containerColor = Color(0xFF1C1C1E)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (streakStatus) {
                is UiState.Loading -> {
                    CircularProgressIndicator(color = Color(0xFFFFC107))
                }

                is UiState.Error -> {
                    Text(
                        text = (streakStatus as UiState.Error).message,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }

                is UiState.Success -> {
                    val data = (streakStatus as UiState.Success).data

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Timestamp Card - CENTERED
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2C2C2E)
                            )
                        ) {
                            Text(
                                text = "Time : ${formatDate(data.lastUpdated)}",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Message Box
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFC107)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (!data.myCheckInStatus) {
                                        "Press the button to activate the streak"
                                    } else if (!data.friendCheckInStatus) {
                                        "Waiting for $friendUsername to check in..."
                                    } else {
                                        "Both checked in! Streak increased 🔥"
                                    },
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Duration Streak - CENTERED
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2C2C2E)
                            )
                        ) {
                            Text(
                                text = "Duration : ${data.currentStreak}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Fire Button - PERFECTLY CENTERED
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    if (!data.myCheckInStatus) {
                                        viewModel.checkInStreak(token, friendId)
                                    }
                                },
                                enabled = !data.myCheckInStatus && checkInResult !is UiState.Loading,
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFC107),
                                    disabledContainerColor = Color.Gray
                                ),
                                modifier = Modifier.size(100.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 48.sp,
                                    color = if (data.myCheckInStatus) Color.Red else Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Loading/Error Messages - CENTERED
                        if (checkInResult is UiState.Loading) {
                            CircularProgressIndicator(
                                color = Color(0xFFFFC107),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (checkInResult is UiState.Error) {
                            Text(
                                text = (checkInResult as UiState.Error).message,
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = parser.parse(isoDate)
        formatter.format(date ?: Date())
    } catch (e: Exception) {
        try {
            val parser2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            val date = parser2.parse(isoDate)
            formatter.format(date ?: Date())
        } catch (e2: Exception) {
            isoDate
        }
    }
}
package com.example.workoutapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.workoutapp.viewmodel.AuthViewModel

// REGISTER SEMUA BAGIAN : SHARON
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    navigateToLogin: () -> Unit,
    navigateHome: () -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state = vm.state.collectAsState().value
    val isError = state =="error_register"
    LaunchedEffect(state) {
        if (state == "success") navigateHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp)
    ) {

//        if (state.startsWith("error")) {
//            Text(
//                text = state,
//                color = Color.Red,
//                modifier = Modifier.padding(8.dp)
//            )
//        }

        // HEADER HITAM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {

            Text(
                "Get Started Now",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Create An Account or Log in To Explore About Our App",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // CARD PUTIH
        Surface(
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            color = Color.White,
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TAB LOGIN / REGISTER
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = navigateToLogin) {
                            Text(
                                text = "Log in",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = "Register",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )

                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // USERNAME FIELD
                Text(
                    "Username :",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        if (isError) vm.clearError()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // PASSWORD FIELD
                Text(
                    "Password",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (isError) {
                    Text(
                        text = "Username sudah digunakan atau data tidak valid",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // REGISTER BUTTON
                Button(
                    onClick = { vm.register(username, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E5AFF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("REGISTER", color = Color.White)
                }
            }
        }
    }
}


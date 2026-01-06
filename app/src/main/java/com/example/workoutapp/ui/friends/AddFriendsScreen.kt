package com.example.workoutapp.ui.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.workoutapp.viewmodel.FriendsViewModel

// ADD FRIENDS SEMUA BAGIAN : SHARON
// STREAK NAVIGATION BAGIAN : MICHELLE
@Composable
fun AddFriendsScreen(
    vm: FriendsViewModel,
    token: String,
    navigateHome: () -> Unit,
    navigateToExercise: () -> Unit,
    navigateToStreak: (friendId: Int, friendUsername: String) -> Unit, // NEW! MICHELLE
    onLogout: () -> Unit
) {
    var search by remember { mutableStateOf("") }

    val friends = vm.friends.collectAsState().value
    val suggest = vm.suggest.collectAsState().value

    LaunchedEffect(Unit) {
        vm.load(token)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = "Friends",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onLogout,
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(Color(0xFFE74C3C)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // SEARCH BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔍", fontSize = 18.sp)
                Spacer(Modifier.width(10.dp))

                TextField(
                    value = search,
                    onValueChange = {
                        search = it
                        vm.search(token, it)
                    },
                    placeholder = { Text("Search username", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        // FRIENDS LIST
        LazyColumn(modifier = Modifier.weight(1f)) {

            items(friends) { f ->
                // ✅ CLICKABLE BOX - Navigate to StreakScreen (MICHELLE)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToStreak(f.id, f.username) } // NEW!
                        .background(Color(0xFF2A2A2A), shape = MaterialTheme.shapes.medium)
                        .padding(vertical = 16.dp, horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = f.username,
                            color = Color.White,
                            fontSize = 18.sp
                        )

                        // ✅ SHOW STREAK INFO (MICHELLE)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🔥",
                                fontSize = 20.sp
                            )
                            Text(
                                text = "${f.duration_streak}",
                                color = Color(0xFFFFD700),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    "Suggested",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
            }

            // SUGGESTED USERS
            items(suggest) { s ->
                val isFriend = friends.any { it.username == s.username }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2A2A), shape = MaterialTheme.shapes.medium)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(s.username, color = Color.White, fontSize = 18.sp)

                    if (!isFriend) {
                        Button(
                            onClick = { vm.addFriend(token, s.username) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700)
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
                        ) {
                            Text("ADD", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(10.dp))

        BottomNavBar(
            current = "friends",
            navigateHome = navigateHome,
            navigateToExercise = navigateToExercise,
            navigateToAddFriends = { /* already here */ }
        )
    }
}

//package com.example.workoutapp.ui.friends
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.*
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.workoutapp.R
//import com.example.workoutapp.ui.home.BottomNavBar
//import com.example.workoutapp.viewmodel.FriendsViewModel
//
//// ADD FRIENDS SEMUA BAGIAN : SHARON
//@Composable
//fun AddFriendsScreen(
//    vm: FriendsViewModel,
//    token: String,
//    navigateHome: () -> Unit,
//    navigateToExercise: () -> Unit,
//    onLogout: () -> Unit
//) {
//    var search by remember { mutableStateOf("") }
//    // controlling the ui : making the UI auto update if the value change (value change by it)
//
//    val friends = vm.friends.collectAsState().value
//    // login : if authvm change state (<string> in authvm) "" to success the loginscreen auto read the state
//    // after func load in friendsvm will keep the list friends data
//
//    val suggest = vm.suggest.collectAsState().value
//
//    LaunchedEffect(Unit) { // unit : void (never change) run once if LaunchedEffect(token) run if the token change
//        vm.load(token)
//        // inserting the val friends & val suggest with the data.
//        // later when vm.search override the suggest data
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
//    ) {
//
//        // HEADER
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Spacer(modifier = Modifier.width(40.dp))
//
//            Text(
//                text = "Friends",
//                color = Color.White,
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Button(
//                onClick = onLogout,
//                modifier = Modifier.size(45.dp),
//                shape = CircleShape,
//                colors = ButtonDefaults.buttonColors(Color(0xFFE74C3C)),
//                contentPadding = PaddingValues(0.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_logout),
//                    contentDescription = null,
//                    colorFilter = ColorFilter.tint(Color.White),
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        // SEARCH BAR
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF1E1E1E), shape = CircleShape)
//                .padding(horizontal = 16.dp, vertical = 12.dp)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("🔍", fontSize = 18.sp)
//                Spacer(Modifier.width(10.dp))
//
//                TextField(
//                    value = search,
//                    onValueChange = {
//                        search = it // mutableState.value = it (Oh! State changed → recompose UI)
//                        // it : what the user type
//                        vm.search(token, it)
//
//                    },
//                    placeholder = { Text("Search username", color = Color.Gray) },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        cursorColor = Color.White,
//                        focusedTextColor = Color.White,
//                        unfocusedTextColor = Color.White
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//
//        Spacer(Modifier.height(25.dp))
//
//        // FRIENDS LIST
//        LazyColumn(modifier = Modifier.weight(1f)) {
//
//            items(friends) { f ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xFF2A2A2A), shape = MaterialTheme.shapes.medium)
//                        .padding(vertical = 16.dp)
//                        .padding(horizontal = 20.dp)
//                        .padding(bottom = 10.dp)
//                ) {
//                    Text(
//                        text = f.username,
//                        color = Color.White,
//                        fontSize = 18.sp,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                Spacer(Modifier.height(10.dp))
//            }
//
//            item {
//                Spacer(Modifier.height(20.dp))
//                Text(
//                    "Suggested",
//                    color = Color.White,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(Modifier.height(10.dp))
//            }
//
//            // SUGGESTED USERS
//            items(suggest) { s ->
//                val isFriend = friends.any { it.username == s.username }
//                // it.username : username of the friend list == suggest.username, example kimi = true
//                // if not friend : false
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xFF2A2A2A), shape = MaterialTheme.shapes.medium)
//                        .padding(horizontal = 20.dp, vertical = 14.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(s.username, color = Color.White, fontSize = 18.sp)
//
//                    // shown only if not friend
//                    if (!isFriend) {
//                        Button(
//                            onClick = { vm.addFriend(token, s.username) },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFFFFD700)
//                            ),
//                            shape = CircleShape,
//                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
//                        ) {
//                            Text("ADD", color = Color.Black, fontWeight = FontWeight.Bold)
//                        }
//                    }
//
//                }
//                Spacer(Modifier.height(10.dp))
//            }
//        }
//
//        Spacer(Modifier.height(10.dp))
//
//        // from home
//        BottomNavBar(
//            current = "friends",
//            navigateHome = navigateHome,
//            navigateToExercise = navigateToExercise,
//            navigateToAddFriends = { /* already here */ }
//        )
//
//    }
//}
//
//
//
//

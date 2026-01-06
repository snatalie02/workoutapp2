package com.example.workoutapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import com.example.workoutapp.R
import java.util.*
import androidx.activity.compose.BackHandler

@Composable
fun HomeScreen(
    username: String,
    navigateToExercise: () -> Unit,
    navigateToAddFriends: () -> Unit,
    onLogout: () -> Unit
) {
//    BackHandler {
//        onLogout()
//    }

    val today = remember {
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
            .format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp,start = 10.dp, end = 10.dp)
    ) {

        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Welcome $username!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start=20.dp, top = 10.dp)
            )

//            Spacer(modifier = Modifier.height(3.dp))

            Button(
                onClick = { onLogout() },
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Logout",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(20.dp)
                )
            }


        }

        Spacer(modifier = Modifier.height(15.dp))

        AutoScrollingTextRow()

        Spacer(modifier = Modifier.height(20.dp))

        // DATE
        Text(
            text = today,
            color = Color(0xFF7CFC00),
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // IMAGE CARDS
        AutoScrollingImageRow()

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM NAV
        BottomNavBar(
            current = "home",
            navigateHome = { /* already here */ },
            navigateToExercise = navigateToExercise,
            navigateToAddFriends = navigateToAddFriends
        )

    }
}

// AUTO SCROLLING MOTIVATION TEXT
@Composable
fun AutoScrollingTextRow() {
    val messages = listOf(
        "Your Consistency Is The Key!",
        "Wake up. Work out. Feel amazing.",
        "The only bad workout is the one that didn't happen.",
        "Your future self will thank you.",
        "A one-hour workout is 4% of your day. No excuses."
    )

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val nextIndex = (listState.firstVisibleItemIndex + 1) % messages.size
            // nextindex : save the index destination, current visible item + 1, reach end go back to 1st text.
            listState.animateScrollToItem(nextIndex)
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(messages.size) { index ->
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()   // tampil 1 item penuh
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = messages[index],
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// AUTO SCROLLING IMAGE CARDS
@Composable
fun AutoScrollingImageRow() {
    val images = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4
    )

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val next = (listState.firstVisibleItemIndex + 1) % images.size
            listState.animateScrollToItem(next)
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        items(images.size) { index ->
            Card(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
            ) {
                Image(
                    painter = painterResource(images[index]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

// BOTTOM NAV BAR
@Composable
fun BottomNavBar(
    current: String,
    navigateHome: () -> Unit,
    navigateToExercise: () -> Unit,
    navigateToAddFriends: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // HOME
        NavItem(
            icon = Icons.Default.Home,
            label = "Home",
            active = current == "home",
            onClick = navigateHome
        )

        // EXERCISES
        NavItem(
            icon = Icons.Default.FitnessCenter,
            label = "Exercises",
            active = current == "exercise",
            onClick = navigateToExercise
        )

        // FRIENDS
        NavItem(
            icon = Icons.Default.Group,
            label = "Add Friends",
            active = current == "friends",
            onClick = navigateToAddFriends
        )
    }
}

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (active) 1f else 0.5f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = alpha),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = alpha)
        )
    }
}

//package com.example.workoutapp.ui.home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.FitnessCenter
//import androidx.compose.material.icons.filled.Group
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//import java.text.SimpleDateFormat
//import com.example.workoutapp.R
//import java.util.*
//
//@Composable
//fun HomeScreen(
//    username: String,
//    navigateToExercise: () -> Unit,
//    navigateToAddFriends: () -> Unit,
//    onLogout: () -> Unit
//) {
//    val today = remember {
//        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
//            .format(Date())
//    }
//
//    Scaffold(
//        containerColor = Color(0xFF1C1C1E),
//        bottomBar = {
//            BottomNavBar(
//                current = "home",
//                navigateHome = { },
//                navigateToExercise = navigateToExercise,
//                navigateToAddFriends = navigateToAddFriends
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 20.dp)
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // TOP HEADER
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = "Welcome",
//                        color = Color.Gray,
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = username,
//                        color = Color.White,
//                        fontSize = 28.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                IconButton(
//                    onClick = onLogout,
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFE74C3C))
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_logout),
//                        contentDescription = "Logout",
//                        colorFilter = ColorFilter.tint(Color.White),
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // DATE CARD
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color(0xFF2C2C2E)
//                )
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "📅",
//                        fontSize = 20.sp
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = today,
//                        color = Color(0xFF7CFC00),
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // MOTIVATION TEXT
//            AutoScrollingTextRow()
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // QUICK ACTIONS
//            Text(
//                text = "Quick Actions",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                QuickActionCard(
//                    modifier = Modifier.weight(1f),
//                    title = "Exercises",
//                    emoji = "💪",
//                    color = Color(0xFFFFC107),
//                    onClick = navigateToExercise
//                )
//                QuickActionCard(
//                    modifier = Modifier.weight(1f),
//                    title = "Friends",
//                    emoji = "👥",
//                    color = Color(0xFF4CAF50),
//                    onClick = navigateToAddFriends
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // IMAGE CAROUSEL
//            Text(
//                text = "Workout Inspiration",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            AutoScrollingImageRow()
//
//            Spacer(modifier = Modifier.weight(1f))
//        }
//    }
//}
//
//@Composable
//fun QuickActionCard(
//    modifier: Modifier = Modifier,
//    title: String,
//    emoji: String,
//    color: Color,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = modifier
//            .height(120.dp)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = color.copy(alpha = 0.15f)
//        ),
//        elevation = CardDefaults.cardElevation(0.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = emoji,
//                    fontSize = 40.sp
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = title,
//                    color = Color.White,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun AutoScrollingTextRow() {
//    val messages = listOf(
//        "Your Consistency Is The Key!",
//        "Wake up. Work out. Feel amazing.",
//        "The only bad workout is the one that didn't happen.",
//        "Your future self will thank you.",
//        "A one-hour workout is 4% of your day. No excuses."
//    )
//
//    val listState = rememberLazyListState()
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(5000)
//            val nextIndex = (listState.firstVisibleItemIndex + 1) % messages.size
//            listState.animateScrollToItem(nextIndex)
//        }
//    }
//
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF2C2C2E)
//        )
//    ) {
//        LazyRow(
//            state = listState,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(messages.size) { index ->
//                Box(
//                    modifier = Modifier
//                        .fillParentMaxWidth()
//                        .padding(20.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = "✨",
//                            fontSize = 16.sp
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = messages[index],
//                            color = Color.White,
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.Medium,
//                            textAlign = TextAlign.Center
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "✨",
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AutoScrollingImageRow() {
//    val images = listOf(
//        R.drawable.img1,
//        R.drawable.img2,
//        R.drawable.img3,
//        R.drawable.img4
//    )
//
//    val listState = rememberLazyListState()
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(5000)
//            val next = (listState.firstVisibleItemIndex + 1) % images.size
//            listState.animateScrollToItem(next)
//        }
//    }
//
//    LazyRow(
//        state = listState,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(220.dp)
//    ) {
//        items(images.size) { index ->
//            Card(
//                modifier = Modifier
//                    .fillParentMaxWidth()
//                    .padding(horizontal = 8.dp),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E)),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Box(modifier = Modifier.fillMaxSize()) {
//                    Image(
//                        painter = painterResource(images[index]),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    // Gradient overlay for better text readability
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(
//                                Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color.Transparent,
//                                        Color.Black.copy(alpha = 0.3f)
//                                    )
//                                )
//                            )
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun BottomNavBar(
//    current: String,
//    navigateHome: () -> Unit,
//    navigateToExercise: () -> Unit,
//    navigateToAddFriends: () -> Unit
//) {
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        color = Color(0xFF1C1C1E),
//        shadowElevation = 8.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp, horizontal = 8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            NavItem(
//                icon = Icons.Default.Home,
//                label = "Home",
//                active = current == "home",
//                onClick = navigateHome
//            )
//
//            NavItem(
//                icon = Icons.Default.FitnessCenter,
//                label = "Exercises",
//                active = current == "exercise",
//                onClick = navigateToExercise
//            )
//
//            NavItem(
//                icon = Icons.Default.Group,
//                label = "Friends",
//                active = current == "friends",
//                onClick = navigateToAddFriends
//            )
//        }
//    }
//}
//
//@Composable
//fun NavItem(
//    icon: ImageVector,
//    label: String,
//    active: Boolean,
//    onClick: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { onClick() }
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = label,
//            tint = if (active) Color(0xFFFFC107) else Color.Gray,
//            modifier = Modifier.size(26.dp)
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(
//            text = label,
//            fontSize = 12.sp,
//            color = if (active) Color.White else Color.Gray,
//            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
//        )
//    }
//}
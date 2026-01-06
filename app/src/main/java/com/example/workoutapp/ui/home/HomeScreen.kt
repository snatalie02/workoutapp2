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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

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




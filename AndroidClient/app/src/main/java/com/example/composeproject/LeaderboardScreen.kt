package com.example.composeproject


import android.provider.ContactsContract.Data
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.data.DataProvider
import com.example.composeproject.data.RouteInfo
import com.example.composeproject.data.UserInfo
import com.example.composeproject.ui.theme.*
import com.example.composeproject.viewmodel.HomeViewModel
import com.example.composeproject.viewmodel.LeaderboardViewModel
import com.example.composeproject.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun LeaderboardScreen(navController: NavController, sharedViewModel: SharedViewModel) {
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val viewModel: LeaderboardViewModel = viewModel()

    viewModel.loadLeaderboard(navController, sharedViewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to White1,
                        1f to WhiteBlue1,
                    ),
                )
            )
    ) {
        Column( modifier = Modifier.fillMaxSize() ) {

            if (viewModel.leadUsers.value.isNotEmpty()) {
                if (viewModel.leadUsers.value.size < 2)
                {
                    LeaderboardBar(navController, viewModel.leadUsers.value[0], UserInfo(0, 0, "", 0), UserInfo(0, 0, "", 0))
                }
                else if (viewModel.leadUsers.value.size < 3)
                {
                    LeaderboardBar(navController, viewModel.leadUsers.value[0], viewModel.leadUsers.value[1], UserInfo(0, 0, "", 0))
                }
                else
                {
                    LeaderboardBar(navController, viewModel.leadUsers.value[0], viewModel.leadUsers.value[1], viewModel.leadUsers.value[2])
                    UserCardsContent(viewModel.leadUsers.value.subList(3, viewModel.leadUsers.value.size))
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

            }
        }
    }

}


@Composable
fun LeaderboardBar(navController: NavController, first: UserInfo, second: UserInfo, third: UserInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .clip(
                RoundedCornerShape(
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )
            )
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Blue2,
                        1f to Blue1,
                    ),
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.left),
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                    )
                }
                Text(
                    text = "Leaderboard", color = colorResource(id = R.color.white),
                    fontFamily = ManropeFamily,
                    fontSize = 19.sp,
                )
                Text(text = "            ")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "2", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 17.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.man),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(
                                CircleShape
                            )
                            .background(Purple2)
                            .padding(3.dp)
                    )
                    Text(
                        text = "${second.points} pt", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 18.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Text(text = second.username,color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 14.sp,
                        modifier = Modifier.alpha(0.9f)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.manb),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(
                                    CircleShape
                                )
                                .background(Purple2)
                                .padding(9.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.46f),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.crown),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(95.dp)
                                    .offset((18).dp, (-64).dp)
                                    .rotate(20f)
                            )
                        }
                    }
                    Text(
                        text = "${first.points}pt", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 23.sp,
                        modifier = Modifier
                            .alpha(0.7f)
                    )
                    Text(text = first.username,color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 14.sp,
                        modifier = Modifier.alpha(0.9f)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "3", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 17.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.woman),
                        contentDescription = null,
                        modifier = Modifier
                            .size(86.dp)
                            .clip(
                                CircleShape
                            )
                            .background(Purple2)
                            .padding(3.dp)
                    )
                    Text(
                        text = "${third.points} pt", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 18.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Text(text = third.username,color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 14.sp,
                        modifier = Modifier.alpha(0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun UserCardsContent(restUsers: List<UserInfo>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp)
    ) {
        items(restUsers) { user ->
            UserCard(user)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: UserInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(bottom = 15.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${user.position}",
                fontFamily = ManropeFamily,
                fontSize = 19.sp,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(end = 15.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.woman),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
            )
            Text(text = user.username,
                fontFamily = ManropeFamily,
                fontSize = 18.sp,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(start = 15.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${user.points} pts",
                    fontFamily = ManropeFamily,
                    fontSize = 19.sp,
                    modifier = Modifier
                        .alpha(0.7f)
                        .padding(start = 20.dp)
                )
            }
        }
    }
}
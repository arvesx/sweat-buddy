package com.example.composeproject

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.data.UserSegmentInfo
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.Purple2
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1
import com.example.composeproject.viewmodel.SegmentLeaderboardViewModel
import com.example.composeproject.viewmodel.SharedViewModel

@Composable
fun SegmentLeaderboardScreen(navController: NavController, sharedViewModel: SharedViewModel)
{
    val viewModel: SegmentLeaderboardViewModel = viewModel()

    viewModel.loadSegmentLeaderboard(navController, sharedViewModel)

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

            if (viewModel.userLead.value.isNotEmpty()) {
                if (viewModel.userLead.value.size < 2)
                {
                    SegmentLeaderboardBar(navController, viewModel.userLead.value[0], UserSegmentInfo(0, 0, "", ""), UserSegmentInfo(0, 0, "", ""))
                }
                else if (viewModel.userLead.value.size < 3)
                {
                    SegmentLeaderboardBar(navController, viewModel.userLead.value[0], viewModel.userLead.value[1], UserSegmentInfo(0, 0, "", ""))
                }
                else
                {
                    SegmentLeaderboardBar(navController, viewModel.userLead.value[0], viewModel.userLead.value[1], viewModel.userLead.value[2])
                    UserCardsContentSegment(viewModel.userLead.value.subList(3, viewModel.userLead.value.size))
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
fun UserCardsContentSegment(restUsers: List<UserSegmentInfo>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp)
    ) {
        items(restUsers) { user ->
            UserCardSegemnts(user)
        }
    }
}

@Composable
fun UserCardSegemnts(user: UserSegmentInfo) {
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
                Text(text = user.time,
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


@Composable
fun SegmentLeaderboardBar(navController: NavController, first: UserSegmentInfo, second: UserSegmentInfo, third: UserSegmentInfo)
{
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
                    modifier = Modifier.clip(androidx.compose.foundation.shape.CircleShape)
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
                                androidx.compose.foundation.shape.CircleShape
                            )
                            .background(Purple2)
                            .padding(3.dp)
                    )
                    UserText(second.time)
                    Text(text = second.username, color = colorResource(id = R.color.white),
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
                                    androidx.compose.foundation.shape.CircleShape
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
                    UserText(first.time, 23.sp)
                    Text(text = first.username, color = colorResource(id = R.color.white),
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
                                androidx.compose.foundation.shape.CircleShape
                            )
                            .background(Purple2)
                            .padding(3.dp)
                    )
                    UserText(third.time)
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


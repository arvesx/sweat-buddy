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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.data.UserInfo
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1
import com.example.composeproject.viewmodel.HomeViewModel
import com.example.composeproject.viewmodel.NewSegmentViewModel
import com.example.composeproject.viewmodel.SharedViewModel

//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewSegmentCreatedScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val viewModel: NewSegmentViewModel = viewModel()

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
    )
    {
        ScreenUpperSection(SectionContent.SEGMENTS, 120.dp, navController, false, screen = Screen.AllSegmentsScreen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                SegmentsLeaderBoardCard(navController, sharedViewModel, viewModel)
                Spacer(modifier = Modifier.height(50.dp))
                CardSegmentInMiddle(
                    totalDistance = sharedViewModel.totalDistanceS.value,
                    totalElevation = sharedViewModel.totalElevationS.value,
                    avgSpeed = sharedViewModel.avgSpeedS.value,
                    totalTime = sharedViewModel.totalTimeS.value
                )
            }

        }
    }
}

@Composable
fun CardSegmentInMiddle(totalDistance: Float, totalElevation: Float, avgSpeed: Float, totalTime: String) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .offset(y = (-60).dp),
        shape = RoundedCornerShape(25.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    SegmentInfoElement(String.format("%.1f", totalDistance) + "km", "Total Distance")
                    SegmentInfoElement(totalTime, "Total Time")
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    SegmentInfoElement(String.format("%.1f", totalElevation) + "m", "Total Elevation")
                    SegmentInfoElement(String.format("%.1f", avgSpeed) + "km/h", "Average Speed")

                }
            }
        }

    }
}


@Composable
fun SegmentInfoElement(upperText: String, lowerText: String) {
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(size = 8.dp)
                    .clip(shape = RoundedCornerShape(percent = 50))
                    .background(color = Blue2)
            ) {
            }
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = upperText, color = Blue2,
                fontFamily = ManropeFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = lowerText, color = Color(0xff6e6e6e),
            fontFamily = ManropeFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(letterSpacing = 1.sp)
        )
    }
}

@Composable
fun SegmentsLeaderBoardCard(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: NewSegmentViewModel
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .offset(y = (-40).dp)
            .bounceClick {
                //viewModel.onLeaderboardClick(navController, sharedViewModel)
                navController.navigate(Screen.SegmentLeaderboardScreen.route)
            },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, hoveredElevation = 10.dp),
    )
    {
        Column(
            modifier = Modifier
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            //.padding(top = 4.dp)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            )
            {
                Text(
                    text = "Leaderboard",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp,
                )

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.leaderboard_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(bottom = 7.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
        }
    }
}

@Composable
fun SegmentsLeaderboardTextField(
    user: UserInfo,
    focused: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    )
    {
        val fontSize = if (focused) 22.sp else 16.sp
        val pointsSize = if (focused) 20.sp else 15.sp

        Text(
            text = "${user.position}",
            fontFamily = ManropeFamily,
            fontSize = fontSize,
            color = Color(0, 0, 0, 0xBF)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = user.username,
            fontFamily = ManropeFamily,
            fontSize = fontSize,
            color = Color(0, 0, 0, 0xBF)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "${user.points}",
            fontFamily = ManropeFamily,
            fontSize = pointsSize,
            color = Color(0, 0, 0, 0x66)
        )

    }

}



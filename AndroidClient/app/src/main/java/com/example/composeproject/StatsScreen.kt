package com.example.composeproject

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.ui.theme.Aqua2
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.Blue5
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.Purple1
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1
import com.example.composeproject.viewmodel.SharedViewModel
import com.example.composeproject.viewmodel.StatsScreenViewModel

@Composable
fun StatsScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val viewModel: StatsScreenViewModel = viewModel()
    viewModel.onPageLoad()
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
        Column {
            TopBar(navController)
            StatsCard(
                totalDistance = viewModel.totalDistance,
                totalElevation = viewModel.totalElevation,
                totalTime = viewModel.totalTime
            )
            Spacer(modifier = Modifier.height(20.dp))
            Legend()
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val distanced = listOf(
                    Bar(viewModel.totalDistance.value),
                    Bar(viewModel.avgTotalDistance.value)
                )

                val timed = listOf(
                    Bar(viewModel.totalTimeMillis.value.toFloat()),
                    Bar(viewModel.avgTotalTimeMillis.value.toFloat())
                )
                val elevationd = listOf(
                    Bar(viewModel.totalElevation.value),
                    Bar(viewModel.avgTotalElevation.value)
                )

                if (viewModel.totalDistance.value != 0f || viewModel.avgTotalDistance.value != 0f)
                {
                    Bars(
                        data = distanced,
                        dataText = listOf(
                            "${String.format("%.1f", viewModel.totalDistance.value)}km",
                            "${String.format("%.1f", viewModel.avgTotalDistance.value)}km"
                        ),
                        description = "Total Distance"
                    )
                }

                if (viewModel.totalTimeMillis.value.toFloat() != 0f || viewModel.avgTotalTimeMillis.value.toFloat() != 0f)
                {
                    Bars(
                        data = timed,
                        dataText = listOf(
                            viewModel.totalTime.value,
                            viewModel.avgTotalTime.value
                        ),
                        description = "Total Time"
                    )
                }

                if (viewModel.totalElevation.value != 0f || viewModel.avgTotalElevation.value != 0f)
                {
                    Bars(
                        data = elevationd,
                        dataText = listOf(
                            "${String.format("%.1f", viewModel.totalElevation.value)}m",
                            "${String.format("%.1f", viewModel.avgTotalElevation.value)}m"
                        ),
                        description = "Total Elevation"
                    )
                }


            }
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
//            .clip(RoundedCornerShape(bottomEnd = 45.dp, bottomStart = 45.dp))
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Blue2,
                        1f to Blue1,
                    ),
                )
            )

    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigate(Screen.HomeScreen.route) },
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
                    text = "Your  Stats", color = colorResource(id = R.color.white),
                    fontFamily = ManropeFamily,
                    fontSize = 18.sp,
                )
                Text(
                    text = "            "
                )
            }
        }
    }
}

@Composable
fun StatsCard(
    totalDistance: MutableState<Float>,
    totalElevation: MutableState<Float>,
    totalTime: MutableState<String>
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(start = 22.dp, end = 22.dp)
            .offset(x = 0.dp, y = -(20.dp)),
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
                RouteInfoElement("${String.format("%.1f", totalDistance.value)}km", "Total Distance")
                RouteInfoElement(totalTime.value, "Total Time")
                RouteInfoElement("${String.format("%.1f", totalElevation.value)}m", "Total Elevation")
            }
        }
    }
}

@Composable
fun Bars(
    maxBarSize: Dp = 300.dp,
    animDuration: Int = 1300,
    animDelay: Int = 20,
    data: List<Bar>,
    dataText: List<String> = emptyList(),
    description: String
) {

    // calculate percentages
    val dataMaxValue = data.maxOf { it.value }
    data.forEach {
        it.perc = it.value / dataMaxValue
    }

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val list = mutableListOf<State<Float>>()
    data.forEach {
        list.add(
            animateFloatAsState(
                targetValue = if (animationPlayed) it.perc else 0f,
                animationSpec = tween(
                    durationMillis = animDuration,
                    delayMillis = animDelay
                )
            )
        )
    }

    val states = remember {
        mutableStateListOf<State<Float>>().apply {
            addAll(list)
        }
    }

    LaunchedEffect(key1 = true)
    {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxHeight(0.7f),
        contentAlignment = Alignment.BottomCenter
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        )
        {
            var bar = 0
            states.forEach {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    if (bar == 1) {
                        Text(
                            text = dataText[bar],
                            fontFamily = ManropeFamily,
                            fontSize = 12.sp,
                            color = Color(0, 0, 0, 0xBF)
                        )

                        Canvas(
                            modifier = Modifier
                                .size(37.dp, it.value * maxBarSize)
                                .clip(RoundedCornerShape(8.dp)),
                            onDraw = {
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0f to Purple1.copy(alpha = 0.4f),
                                            0.5f to Aqua2.copy(alpha = 0.3f),
                                        )
                                    )
                                )
                            }
                        )
                    } else {

                        Text(
                            text = dataText[bar],
                            fontFamily = ManropeFamily,
                            fontSize = 12.sp,
                            color = Color(0, 0, 0, 0xBF)
                        )

                        Canvas(
                            modifier = Modifier
                                .size(37.dp, it.value * maxBarSize)
                                .clip(RoundedCornerShape(8.dp)),
                            onDraw = {
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0f to Purple1,
                                            0.5f to Blue5,
                                        )
                                    )
                                )
                            }
                        )
                    }
                    bar++
                }
                Spacer(modifier = Modifier.width(7.dp))
            }
        }
        Text(
            text = description,
            fontFamily = ManropeFamily,
            fontSize = 12.sp,
            color = Color(0, 0, 0, 0xBF),
            modifier = Modifier.offset(y = 25.dp)
        )
    }
}

@Composable
fun Legend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Canvas(
            modifier = Modifier
                .size(15.dp)
                .clip(RoundedCornerShape(3.dp)),
            onDraw = {
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Purple1,
                            0.5f to Blue5,
                        )
                    )
                )
            }
        )
        Text(
            text = "You",
            fontFamily = ManropeFamily,
            fontSize = 14.sp,
            color = Color(0, 0, 0, 0xBF),
            modifier = Modifier
                .padding(start = 5.dp, end = 25.dp)
        )
        Canvas(
            modifier = Modifier
                .size(15.dp)
                .clip(RoundedCornerShape(3.dp)),
            onDraw = {
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Purple1.copy(alpha = 0.4f),
                            0.5f to Aqua2.copy(alpha = 0.3f),
                        )
                    )
                )
            }
        )
        Text(
            text = "Average",
            fontFamily = ManropeFamily,
            fontSize = 14.sp,
            color = Color(0, 0, 0, 0xBF),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
        )
    }
}
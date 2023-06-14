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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.composeproject.ui.theme.Aqua2
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.Blue5
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.Purple1
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun StatsScreen() {
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
            TopBar()
            StatsCard(
                totalDistance = 90.0f,
                totalElevation = 6.0f,
                totalTime = "30h"
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
                Bars(
                    data = distanced,
                    description = "Total Distance"
                )
                Bars(
                    data = timed,
                    description = "Total Time"
                )
                Bars(
                    data = elevationd,
                    description = "Total Elevation"
                )
            }
        }
    }
}

@Composable
fun TopBar() {
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
                    onClick = { /*TODO*/ },
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
fun StatsCard(totalDistance: Float, totalElevation: Float, totalTime: String) {
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
                RouteInfoElement("$totalDistance" + "km", "Total Distance")
                RouteInfoElement(totalTime, "Total Time")
                RouteInfoElement("$totalElevation" + "m", "Total Elevation")
            }
        }
    }
}

val distanced = listOf( Bar(90.0f), Bar(43.5f))
val timed = listOf( Bar(30.0f), Bar(19.0f))
val elevationd = listOf( Bar(6.0f), Bar(12.0f))

@Composable
fun Bars(
    maxBarSize: Dp = 300.dp,
    animDuration: Int = 1000,
    animDelay: Int = 20,
    data: List<Bar>,
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
            var day = 0
            states.forEach {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    if (day == 1) {
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
                    }
                    else {
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
                    day++
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
    Row (
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
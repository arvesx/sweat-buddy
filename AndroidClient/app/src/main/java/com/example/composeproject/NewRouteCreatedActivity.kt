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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRouteCreatedScreen() {
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
        UpperSection("Route Name", 150)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            CardInMiddle(
                totalDistance = 12.0f,
                totalElevation = 7.0f,
                avgSpeed = 4.0f,
                totalTime = "1h25m"
            )
        }
    }
}

@Composable
fun CardInMiddle(totalDistance: Float, totalElevation: Float, avgSpeed: Float, totalTime: String) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
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
                    RouteInfoElement("$totalDistance" + "km", "Total Distance")
                    RouteInfoElement(totalTime, "Total Time")
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    RouteInfoElement("$totalElevation" + "m", "Total Elevation")
                    RouteInfoElement("$avgSpeed" + "km/h", "Average Speed")

                }
            }
        }

    }
}

@Composable
fun RouteInfoElement(upperText: String, lowerText: String) {
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
fun UpperSection(routeName: String, scorePoints: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(bottomEnd = 45.dp, bottomStart = 45.dp))
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
                    text = routeName, color = colorResource(id = R.color.white),
                    fontFamily = ManropeFamily,
                    fontSize = 18.sp,
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pencil),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center),
            ) {
                CircleShape(
                    130.dp, Color.White,
                    Modifier
                        .padding(bottom = 45.dp)
                        .alpha(0.1f)
                )
                CircleShape(
                    100.dp, Color.White,
                    Modifier
                        .padding(top = 15.dp)
                        .alpha(0.3f)
                )

                CircleShape(80.dp, Color.White, Modifier.padding(top = 25.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.padding(bottom = 80.dp)
                    ) {
                        Text(
                            text = "Your Score", color = Blue2,
                            fontFamily = ManropeFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$scorePoints", color = Blue2,
                                fontFamily = ManropeFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "pt", color = Blue2,
                                fontFamily = ManropeFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                }


            }
        }


    }
}

@Composable
fun CircleShape(size: Dp, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
fun ExampleBox(shape: Shape) {

}
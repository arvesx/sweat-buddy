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
import androidx.navigation.NavController
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1
import com.example.composeproject.viewmodel.SharedViewModel

//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRouteCreatedScreen(navController: NavController, sharedViewModel: SharedViewModel) {
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
        UpperSection(
            sharedViewModel.routeName.value,
            sharedViewModel.routePoints.value,
            navController
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {

                CardInMiddle(
                    totalDistance = sharedViewModel.totalDistance.value,
                    totalElevation = sharedViewModel.totalElevation.value,
                    avgSpeed = sharedViewModel.avgSpeed.value,
                    totalTime = sharedViewModel.totalTime.value
                )

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp).alpha(0.9f)
                    .clip(RoundedCornerShape(topEnd = 45.dp, topStart = 45.dp))
            ) {
                MapScreen(
                    coordinates = sharedViewModel.routeWaypoints.value,
                    cameraPositionState = sharedViewModel.cameraPositionState.value,
                    sharedViewModel = sharedViewModel
                )
            }
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
                    RouteInfoElement(String.format("%.1f", totalDistance) + "km", "Total Distance")
                    RouteInfoElement(totalTime, "Total Time")
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    RouteInfoElement(String.format("%.1f", totalElevation) + "m", "Total Elevation")
                    RouteInfoElement(String.format("%.1f", avgSpeed) + "km/h", "Average Speed")

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
fun UpperSection(routeName: String, scorePoints: Int, navController: NavController) {
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
                    onClick = { navController.navigate(Screen.AllRoutesScreen.route) },
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
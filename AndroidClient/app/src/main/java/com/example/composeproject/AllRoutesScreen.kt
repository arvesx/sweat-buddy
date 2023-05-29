package com.example.composeproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.data.DataProvider
import com.example.composeproject.data.RouteInfo
import com.example.composeproject.ui.theme.Aqua1
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.Pink1
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1

@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun RoutesScreen(navController: NavController) {

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
        UpperSection(120.dp, navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            TopCard(14, 41f)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Your routes",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
                RoutesContent()
            }
        }
    }
}

@Composable
fun RoutesContent() {
    val routes = remember { DataProvider.routesList }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(routes) { route ->
            RouteCard(route)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(route: RouteInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, hoveredElevation = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.2f to Pink1,
                            1.3f to Aqua1,
                        ),
                    )
                )
                .padding(start = 7.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Box(modifier = Modifier.background(Color.White)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        var iconType: Painter = painterResource(id = R.drawable.mountain)
                        var stringType: String = "UNKNOWN"
                        when (route.type) {
                            0 -> {
                                iconType = painterResource(id = R.drawable.mountain)
                                stringType = "HIKING"
                            }

                            1 -> {
                                iconType = painterResource(id = R.drawable.path)
                                stringType = "WALK"
                            }

                            2 -> {
                                iconType = painterResource(id = R.drawable.runningshoe)
                                stringType = "RUN"
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box() {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = iconType,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        stringType,
                                        fontWeight = FontWeight.Light,
                                        fontFamily = ManropeFamily,
                                        fontSize = 12.sp,
                                        modifier = Modifier.alpha(0.7f),
                                    )

                                }
                            }
                            Box() {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(
                                                CircleShape
                                            )
                                            .background(Color.Green)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        "IN PROGRESS",
                                        fontWeight = FontWeight.Light,
                                        fontFamily = ManropeFamily,
                                        fontSize = 12.sp,
                                        modifier = Modifier.alpha(0.7f),
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = route.title,
                                fontWeight = FontWeight.Bold,
                                fontFamily = ManropeFamily,
                                fontSize = 16.sp
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row() {

                            }
                            Text(
                                text = "12m", fontWeight = FontWeight.Light,
                                fontFamily = ManropeFamily,
                                fontSize = 10.sp,
                                modifier = Modifier.alpha(0.7f)
                            )

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopCard(totalNumberOfRoutes: Int, totalKilometers: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(105.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp, hoveredElevation = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$totalNumberOfRoutes",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total routes done",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = "this month",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$totalKilometers km",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total km this",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = " month",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
            }
        }
    }
}

@Composable
fun UpperSection(height: Dp, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Blue2,
                        1f to Blue1,
                    ),
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { navController.navigate(Screen.HomeScreen.route) },
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
                text = "Routes", color = colorResource(id = R.color.white),
                fontFamily = ManropeFamily,
                fontSize = 18.sp,
            )
            IconButton(
                onClick = { navController.navigate(Screen.NewRouteScreen.route) },
                modifier = Modifier.clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }

    }
}
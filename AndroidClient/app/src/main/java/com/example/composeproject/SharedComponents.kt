package com.example.composeproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.data.Info
import com.example.composeproject.data.RouteInfo
import com.example.composeproject.data.SegmentInfo
import com.example.composeproject.ui.theme.*
import com.example.composeproject.viewmodel.SharedViewModel

enum class SectionContent {
    ROUTES,
    SEGMENTS
}

@Composable
fun <T: Info> ContentCard(item: T) {
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
                        when (item.type) {
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
                                                androidx.compose.foundation.shape.CircleShape
                                            )
                                            .background(Blue2)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        "COMPLETED",
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
                                text = item.title,
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


@Composable
fun ContentList(
    content: SectionContent,
    sharedViewModel: SharedViewModel
)
{
    if (content == SectionContent.ROUTES)
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sharedViewModel.routes.value) { route ->
                val routeInfo = RouteInfo(1, route.routeName, route.routeType, 5)
                ContentCard(routeInfo)
            }
        }
    }
    else
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sharedViewModel.segments.value) { segment ->
                val segmentInfo = SegmentInfo(1, segment.segmentName, segment.segmentType, 5)
                ContentCard(segmentInfo)
            }
        }
    }
}

@Composable
fun ScreenUpperSection(
    content: SectionContent,
    height: Dp,
    navController: NavController
)
{
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
                onClick = {
                    navController.navigate(Screen.HomeScreen.route) {
                    }
                },
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
                text = content.toString().substring(0,1)+content.toString().substring(1).lowercase(), color = colorResource(id = R.color.white),
                fontFamily = ManropeFamily,
                fontSize = 18.sp,
            )
            IconButton(
                onClick = {
                    if (content == SectionContent.ROUTES)
                    {
                        navController.navigate(Screen.NewRouteScreen.route)
                    }
                    else
                    {
                        navController.navigate(Screen.NewSegmentScreen.route)
                    }
                },
                modifier = Modifier.clip(androidx.compose.foundation.shape.CircleShape)
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
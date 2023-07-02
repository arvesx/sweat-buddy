package com.example.composeproject

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.data.ContentCardType
import com.example.composeproject.data.Info
import com.example.composeproject.data.RouteInfo
import com.example.composeproject.data.SegmentInfo
import com.example.composeproject.ui.theme.*
import com.example.composeproject.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SectionContent {
    ROUTES,
    SEGMENTS
}

@Composable
fun <T : Info> ContentCard(
    item: T,
    type: ContentCardType,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 10.dp)
            .bounceClick { sharedViewModel.handleContentCardClick(type, navController, item.id, sharedViewModel) },
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
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    if (content == SectionContent.ROUTES) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sharedViewModel.routes.value) { route ->
                val routeInfo = RouteInfo(route.routeId, route.routeName, route.routeType, 5)
                ContentCard(routeInfo, ContentCardType.ROUTE, sharedViewModel, navController)
            }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sharedViewModel.segments.value) { segment ->
                val segmentInfo =
                    SegmentInfo(segment.segmentId, segment.segmentName, 2, 5)
                ContentCard(segmentInfo, ContentCardType.SEGMENT, sharedViewModel, navController)
            }
        }
    }
}

@Composable
fun ScreenUpperSection(
    content: SectionContent,
    height: Dp,
    navController: NavController,
    plusIcon: Boolean = true,
    screen: Screen = Screen.HomeScreen
) {
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
                        navController.navigate(screen.route) {
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
                text = content.toString().substring(0, 1) + content.toString().substring(1)
                    .lowercase(),
                color = colorResource(id = R.color.white),
                fontFamily = ManropeFamily,
                fontSize = 18.sp,
            )

            if (plusIcon) {
                IconButton(
                    onClick = {
                        if (content == SectionContent.ROUTES) {
                            navController.navigate(Screen.NewRouteScreen.route)
                        } else {
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
            else
            {
                Box(
                    modifier = Modifier.width(48.dp)
                )
            }
        }
    }

}


// Pulsate effect
enum class CardState {
    IDLE,
    PRESSED
}

fun Modifier.bounceClick(onClick: () -> Unit) = composed {

    val interactionSource = remember { MutableInteractionSource() }
    var state by remember { mutableStateOf(CardState.IDLE) }
    val scale by animateFloatAsState(
        targetValue = if (state == CardState.PRESSED) 0.9f else 1f,
        animationSpec = tween(
            durationMillis = 120,
            delayMillis = 0,
            easing = FastOutSlowInEasing
        )
    )
    val isPressed by interactionSource.collectIsPressedAsState()

    this
        .graphicsLayer {

            if (isPressed) {
                state = CardState.PRESSED
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    delay(200L)
                    state = CardState.IDLE
                }
            }

            scaleX = scale
            scaleY = scale
        }

        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    delay(100L)
                    state = CardState.IDLE
                    delay(130L)
                    onClick()
                }
            }
        )

}
























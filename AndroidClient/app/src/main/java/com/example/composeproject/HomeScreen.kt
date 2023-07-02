package com.example.composeproject

import android.graphics.Paint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.data.UserInfo
import com.example.composeproject.ui.theme.*
import com.example.composeproject.viewmodel.HomeViewModel
import com.example.composeproject.viewmodel.LoginViewModel
import com.example.composeproject.viewmodel.SharedViewModel
import java.time.MonthDay
import java.time.Year


@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val viewModel: HomeViewModel = viewModel()

    viewModel.onLeaderboardClick(navController, sharedViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to White1,
                        1f to WhiteBlue1,
                    ),
                )
            ),
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(268.dp)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Blue2,
                            0.6f to Blue1,
                        ),
                    )
                ),
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomStart)
                    .offset(x = 6.dp, y = 180.dp)
            )
            {
                Canvas(
                    modifier = Modifier
                        .size(60.dp),
                    onDraw = {
                        drawCircle(
                            color = Color(255, 255, 255),
                            alpha = 0.05f
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomStart)
                    .offset(x = 140.dp, y = 80.dp)
            )
            {
                Canvas(
                    modifier = Modifier
                        .size(40.dp),
                    onDraw = {
                        drawCircle(
                            color = Color(255, 255, 255),
                            alpha = 0.05f
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomStart)
                    .offset(x = 248.dp, y = 165.dp)
            )
            {
                Canvas(
                    modifier = Modifier
                        .size(30.dp),
                    onDraw = {
                        drawCircle(
                            color = Color(255, 255, 255),
                            alpha = 0.05f
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
                    .offset(x = 55.dp, y = (-75).dp)
            )
            {
                Canvas(
                    modifier = Modifier
                        .size(200.dp),
                    onDraw = {
                        drawCircle(
                            color = Color(255, 255, 255),
                            alpha = 0.05f
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
                    .offset(x = 148.dp, y = (-110).dp),
            )
            {
                Canvas(
                    modifier = Modifier
                        .size(400.dp),
                    //.border(2.dp, Color.Black),
                    onDraw = {
                        drawCircle(
                            color = Color(255, 255, 255),
                            alpha = 0.05f,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                )
            }


            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    UserAvatar(navController, viewModel, sharedViewModel)
                    TrophiesSection(trophies = sharedViewModel.userData.points)
                }

                DateSection()
                GreetingSection(sharedViewModel.username.value)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column {
                RoutesCard(navController, sharedViewModel)
                Spacer(modifier = Modifier.height(15.dp))
                LeaderBoardCard(navController, sharedViewModel, viewModel)
            }
            Spacer(modifier = Modifier.width(40.dp))
            Column {
                GoalsCard()
                Spacer(modifier = Modifier.height(15.dp))
                SegmentsCard(navController)
            }
        }

        // Bar Chart
        BarChart(
            maxBarSize = 180.dp,
            animDuration = 1000,
            animDelay = 20,
            data = weekWorkoutStats
        )

        Spacer(modifier = Modifier.height(30.dp))


    }


    //Text("Hello Bro!", style = TextStyle(color=Color.Red));
}


// Data
data class Bar(val value: Float, var perc: Float = 1f)

val weekDays = listOf(
    "Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"
)

val weekWorkoutStats = listOf(
    Bar(5f),
    Bar(10f),
    Bar(8f),
    Bar(2f),
    Bar(3f),
    Bar(20f),
    Bar(12f)
)

//Components
@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    maxBarSize: Dp = 160.dp,
    animDuration: Int,
    animDelay: Int,
    data: List<Bar>
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

    val paint = Paint()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .offset(x = 10.dp, y = (-25).dp),
        contentAlignment = Alignment.BottomCenter
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
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
                    Text(
                        text = "${data[day].value.toInt()}km",
                        fontFamily = ManropeFamily,
                        fontSize = 12.sp,
                        color = Color(0, 0, 0, 0xBF)
                    )

                    Canvas(
                        modifier = Modifier
                            .size(35.dp, it.value * maxBarSize)
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

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = weekDays[day],
                        fontFamily = ManropeFamily,
                        fontSize = 16.sp,
                        color = Color(0, 0, 0, 0xBF)
                    )

                    day++
                }

                Spacer(modifier = Modifier.width(20.dp))
            }
        }

    }

}

@Composable
fun UserAvatar(navController: NavController, homeViewModel: HomeViewModel, sharedViewModel:SharedViewModel) {
    var clicked by remember { mutableStateOf(false) }

    val boxWidthOpen by animateFloatAsState(
        targetValue = if (clicked) 1f else 0.33f,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = 0,
            easing = FastOutLinearInEasing
        )
    )

    val boxWidthClose by animateFloatAsState(
        targetValue = if (!clicked) 0.33f else boxWidthOpen,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = 0,
            easing = FastOutLinearInEasing
        )
    )

    Box(
        modifier = Modifier
            .height(73.dp)
            .padding(start = 25.dp)
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
                //.padding(start = 25.dp)
                .fillMaxWidth(0.5f)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(
                        fraction = if (clicked) boxWidthOpen else boxWidthClose
                    )
                    .fillMaxHeight()
                    .offset(0.dp, (-2).dp),
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    topStart = 35.dp,
                    topEnd = 28.dp,
                    bottomEnd = 28.dp
                ),
                color = Color.Blue.copy(alpha = 0.1f)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Spacer(modifier = Modifier.width(60.dp))
                    IconButton(
                        onClick = { navController.navigate(Screen.StatsScreen.route) },
                        modifier = Modifier
                            .clip(CircleShape),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.stats),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                            //.padding(end = 15.dp)
                            //.clip(CircleShape)
                        )
                    }
                    IconButton(
                        onClick = { homeViewModel.onLogoutClick(navController, sharedViewModel) },
                        modifier = Modifier.clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = null,
                            modifier = Modifier
                                .size(23.dp)
                            //.padding(end = 15.dp)
                            //.clip(CircleShape)
                        )
                    }

                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.manb),
            contentDescription = null,
            modifier = Modifier
                .size(65.dp)
                .border(1.dp, color = Color.White, CircleShape)
                .clip(CircleShape)
                .clickable { clicked = !clicked }
        )

    }


}

@Composable
fun TrophiesSection(trophies: Int) {
    Surface(
        modifier = Modifier
            .width(156.dp)
            .height(110.dp)
            .padding(40.dp),

        shape = CircleShape,
        color = Color(255, 255, 255, 0x66),
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
        )
        {
            Image(
                painter = painterResource(id = R.mipmap.trophy),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
            )

            Text(
                text = "$trophies",
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white),
                fontFamily = ManropeFamily,
                fontSize = 17.sp,
            )

        }


    }
}

@Composable
fun DateSection() {

    val year = Year.now() // requires API 26 current (min25) (fix: changed minSDK from 25 to 26)
    val month = MonthDay.now().month.toString().substring(0, 1) + MonthDay.now().month.toString()
        .substring(1).lowercase()
    val day = MonthDay.now().dayOfMonth

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 4.dp),
    )
    {
        Text(
            modifier = Modifier
                .offset(28.dp),
            text = "$month $day, $year",
            color = Color(255, 255, 255, 0x50),
            fontFamily = ManropeFamily,
            fontSize = 13.sp,
        )
    }
}

@Composable
fun GreetingSection(username: String = "Pappou") {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    )
    {
        Text(
            modifier = Modifier
                .offset(28.dp),
            text = "Good day, $username!",
            color = colorResource(id = R.color.white),
            fontFamily = ManropeFamily,
            fontSize = 22.sp,
        )

        Spacer(
            modifier = Modifier
                .width(28.dp)
        )

        Image(
            painter = painterResource(id = R.mipmap.fire_icon),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)

        )

    }
}

@Composable
fun RoutesCard(navController: NavController, sharedViewModel: SharedViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth(0.42f)
            .height(105.dp)
            .offset(x = 20.dp, y = (-50).dp)
            .bounceClick { navController.navigate(Screen.AllRoutesScreen.route) },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, hoveredElevation = 10.dp),
    )
    {
        Column(
            modifier = Modifier
                .padding(15.dp)
            //.padding(top = 4.dp)
            //horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "Routes",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp,
                )

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )

                Image(
                    painter = painterResource(id = R.mipmap.routes_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Most Recent",
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp,
                    color = Color(0, 0, 0, 0xBF)
                )

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )

                Text(

                    text = "${
                        String.format("%.1f", sharedViewModel.mostRecentRouteKm.value)
                    }km",
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    color = Color(0, 0, 0, 0x66)
                )

            }


        }

    }
}

@Composable
fun GoalsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(217.dp)
            .offset(x = 0.dp, y = (-50).dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        /*
        * cardElevation(
            defaultElevation: Dp,
            pressedElevation: Dp,
            focusedElevation: Dp,
            hoveredElevation: Dp,
            draggedElevation: Dp,
            disabledElevation: Dp
        )
        * */
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, hoveredElevation = 10.dp),
    )
    {
        Column(
            modifier = Modifier
                .padding(15.dp)
            //.padding(top = 4.dp)
            //horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "Today's Goals",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp,
                )

                Spacer(
                    modifier = Modifier
                        .width(7.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.goals_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            )
            {
                CircularProgressBar(
                    curNumber = 500,
                    goalNumber = 600,
                    animDelay = 300,
                    unit = "cal"
                )
            }

            Spacer(
                modifier = Modifier
                    .height(22.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            )
            {
                CircularProgressBar(
                    curNumber = 3,
                    goalNumber = 10,
                    animDelay = 800,
                    unit = "km"
                )
            }


        }
    }
}


@Composable
fun LeaderBoardCard(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: HomeViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.42f)
            .height(164.dp)
            .offset(x = 20.dp, y = (-50).dp)
            .bounceClick {
                viewModel.onLeaderboardClick(navController, sharedViewModel)
                navController.navigate(Screen.LeaderboardScreen.route)
            },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        /*
        * cardElevation(
            defaultElevation: Dp,
            pressedElevation: Dp,
            focusedElevation: Dp,
            hoveredElevation: Dp,
            draggedElevation: Dp,
            disabledElevation: Dp
        )
        * */
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

            if (sharedViewModel.leaderboardList.value.isNotEmpty()) {

                val userInfo: UserInfo =
                    sharedViewModel.getUserDataByID(sharedViewModel.userID.value)

                if (userInfo.position == 1) {
                    LeaderboardTextField(
                        user = userInfo,
                        focused = true
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    if (sharedViewModel.getUserDataByPosition(userInfo.position + 1).position != 0) {
                        LeaderboardTextField(
                            user = sharedViewModel.getUserDataByPosition(2),
                            focused = false
                        )
                        Spacer(modifier = Modifier.height(3.dp))

                        if (userInfo.position + 1 < sharedViewModel.leaderboardList.value.size) {
                            LeaderboardTextField(
                                user = sharedViewModel.getUserDataByPosition(3),
                                focused = false
                            )
                        }
                    }

                } else if (userInfo.position == sharedViewModel.leaderboardList.value.size) {

                    if (sharedViewModel.leaderboardList.value.size != 2)
                    {
                        LeaderboardTextField(
                            user = sharedViewModel.getUserDataByPosition(userInfo.position - 2),
                            focused = false
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                    }

                    LeaderboardTextField(
                        user = sharedViewModel.getUserDataByPosition(userInfo.position - 1),
                        focused = false
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    LeaderboardTextField(
                        user = userInfo,
                        focused = true
                    )
                }
                else {
                    LeaderboardTextField(
                        user = sharedViewModel.getUserDataByPosition(userInfo.position - 1),
                        focused = false
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    LeaderboardTextField(
                        user = userInfo,
                        focused = true
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    LeaderboardTextField(
                        user = sharedViewModel.getUserDataByPosition(userInfo.position + 1),
                        focused = false
                    )

                }
            }
        }
    }
}

@Composable
fun LeaderboardTextField(
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

@Composable
fun SegmentsCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(53.dp)
            .offset(x = 0.dp, y = (-50).dp)
            .bounceClick { navController.navigate(Screen.AllSegmentsScreen.route) },
        shape = RoundedCornerShape(25.dp),
        /*
        * cardElevation(
            defaultElevation: Dp,
            pressedElevation: Dp,
            focusedElevation: Dp,
            hoveredElevation: Dp,
            draggedElevation: Dp,
            disabledElevation: Dp
        )
        * */
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, hoveredElevation = 10.dp),
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Blue5,
                            1f to Purple1,
                        ),
                    )
                ),

            )
        {
            Column(
                modifier = Modifier
                    .padding(15.dp),
                //.padding(top = 4.dp)
                //horizontalAlignment = Alignment.CenterHorizontally
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
                        text = "Segments",
                        fontWeight = FontWeight.Bold,
                        fontFamily = ManropeFamily,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.segments_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)

                    )

                }
            }
        }

    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    curNumber: Int,
    goalNumber: Int,
    unit: String,
    fontSize: TextUnit = 22.sp,
    percentFontSize: TextUnit = 9.sp,
    inactiveBarColor: Color = White2,
    activeBarColor: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Pink1,
            0.3f to Pink2,
            0.6f to Aqua3,
            0.9f to Aqua1
        )
    ),
    radius: Dp = 28.dp,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val percentage = remember {
        mutableStateOf(curNumber.toFloat() / goalNumber)
    }

    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage.value else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay,
            //easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true)
    {
        animationPlayed = true
    }

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "$curNumber  $unit",
            fontFamily = ManropeFamily,
            fontSize = 16.sp,
            color = Color(0, 0, 0, 0xBF)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Box(
            modifier = Modifier
                .size(radius * 2f),
            contentAlignment = Alignment.Center
        )
        {
            Canvas(
                modifier = Modifier
                    .fillMaxSize(),
                onDraw = {
                    drawArc(
                        startAngle = -90f,
                        sweepAngle = -360f,
                        color = inactiveBarColor,
                        useCenter = false,
                        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize(),
                onDraw = {
                    drawArc(
                        startAngle = -90f,
                        sweepAngle = 360 * curPercentage.value,
                        brush = activeBarColor,
                        useCenter = false,
                        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
            )
            {
                Text(
                    text = (curPercentage.value * 100).toInt().toString(),
                    style = TextStyle(
                        brush = activeBarColor,
                    ),
                    fontFamily = ManropeFamily,
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraBold,
                )

                Text(
                    modifier = Modifier
                        .padding(start = 2.dp, top = 10.dp),
                    text = "%",
                    style = TextStyle(
                        brush = activeBarColor
                    ),
                    fontFamily = ManropeFamily,
                    fontSize = percentFontSize,
                    fontWeight = FontWeight.ExtraBold
                )
            }

        }


    }


}

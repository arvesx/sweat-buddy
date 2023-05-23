package com.example.composeproject

import android.graphics.Paint.Align
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeproject.ui.theme.*
import java.time.MonthDay
import java.time.Year

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun UserAvatar(avatar: String) {
    Image (
        painter = painterResource(id = R.mipmap.old_man),
        contentDescription = null,
        //contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .padding(28.dp)
            .border(1.dp, color = Color.White, CircleShape)
            .clip(CircleShape)

    )
}

@Composable
fun TrophiesSection(trophies: Int)
{
    Surface(
        modifier = Modifier
            .width(156.dp)
            .height(110.dp)
            .padding(40.dp),

        shape = CircleShape,
        color = Color(255, 255, 255, 0x66),
    )
    {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
        )
        {
            Image (
                painter = painterResource(id = R.mipmap.trophy),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
            )

            Text (
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
    val month = MonthDay.now().month.toString().substring(0, 1) + MonthDay.now().month.toString().substring(1).lowercase()
    val day = MonthDay.now().dayOfMonth

    Row (
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

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        //horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    )
    {
        Text (
            modifier = Modifier
                .offset(28.dp),
            text = "Good day, $username!",
            color = colorResource(id = R.color.white),
            fontFamily = ManropeFamily,
            fontSize = 22.sp,
        )

        Spacer (
            modifier = Modifier
                .width(28.dp)
        )

        Image (
            painter = painterResource(id = R.mipmap.fire_icon),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)

        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesCard()
{
    Card(
        modifier = Modifier
            .fillMaxWidth(0.42f)
            .height(105.dp)
            .offset(x = 20.dp, y = (-50).dp),
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
            Row (
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

                Spacer (
                    modifier = Modifier
                        .width(5.dp)
                )

                Image (
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

            Row (
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

                Spacer (
                    modifier = Modifier
                        .width(5.dp)
                )

                Text(
                    text = "18km",
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    color = Color(0, 0, 0, 0x66)
                )

            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsCard()
{
    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(207.dp)
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
            Row (
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

                Spacer (
                    modifier = Modifier
                        .width(7.dp)
                )

                Image (
                    painter = painterResource(id = R.drawable.goals_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                /*
                Text(
                    text = "Most Recent",
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp,
                    color = Color(0, 0, 0, 0xBF)
                )


                Spacer (
                    modifier = Modifier
                        .width(5.dp)
                )

                Text(
                    text = "18km",
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    color = Color(0, 0, 0, 0x66)
                )
                */

            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderBoardCard()
{
    Card(
        modifier = Modifier
            .fillMaxWidth(0.42f)
            .height(164.dp)
            .offset(x = 20.dp, y = (-50).dp),
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
            Row (
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

                Spacer (
                    modifier = Modifier
                        .width(5.dp)
                )

                Image (
                    painter = painterResource(id = R.drawable.leaderboard_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(bottom = 7.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Row (
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

                Spacer (
                    modifier = Modifier
                        .width(5.dp)
                )

                Text(
                    text = "18km",
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    color = Color(0, 0, 0, 0x66)
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentsCard()
{
    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(62.dp)
            .offset(x = 0.dp, y = (-50).dp),
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
        Box (
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
                    .padding(17.dp),
                //.padding(top = 4.dp)
                //horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Row (
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

                    Spacer (
                        modifier = Modifier
                            .width(20.dp)
                    )

                    Image (
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

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreen() {
    Column (
        modifier = Modifier
            .fillMaxHeight()
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
        Box (
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
            //modifier = Modifier
            //.clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 60.dp, y = (-100).dp),
                contentAlignment = Alignment.BottomEnd,
            )
            {
                Surface(
                    modifier = Modifier
                        .size(225.dp),
                    shape = CircleShape,
                    color = Color(255, 255, 255, 0x12)
                ) {}
            }

            Box(
                modifier = Modifier
                    //.fillMaxWidth()
                    //.offset(x = 110.dp, y = (-110).dp)
                    .clip(CircleShape)
                    .size(310.dp)
                    .border(width = 2.dp, color = Color(255, 255, 255, 0x00))
                    .background(Color(255, 255, 255, 0x00)),
                contentAlignment = Alignment.BottomEnd,
            ) {}

            Column {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    UserAvatar("ss")
                    TrophiesSection(trophies = 1589)
                }

                DateSection()
                GreetingSection()
            }
        }

        Row (
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column {
                RoutesCard()
                Spacer(modifier = Modifier.height(15.dp))
                LeaderBoardCard()
            }
            Spacer(modifier = Modifier.width(40.dp))
            Column {
                GoalsCard()
                Spacer(modifier = Modifier.height(15.dp))
                SegmentsCard()
            }
        }


    }





    //Text("Hello Bro!", style = TextStyle(color=Color.Red));
}


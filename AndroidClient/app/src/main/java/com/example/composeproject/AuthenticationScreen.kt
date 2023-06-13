package com.example.composeproject

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.White1
import com.example.composeproject.ui.theme.WhiteBlue1


@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun AuthenticationScreen(navController: NavController) {
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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Text(
                text = "Sweat Buddy", color = Blue1,
                fontFamily = ManropeFamily,
                fontSize = 37.sp
            )
            Text(
                text = "Less Sweaty, More Buddy!", color = Blue1,
                fontFamily = ManropeFamily,
                fontSize = 13.sp, style = TextStyle(letterSpacing = 3.sp),
                modifier = Modifier.alpha(0.9f)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Blue2,
                                1f to Blue1,
                            ),
                        )
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 340.dp)
                ) {
                    val canvasWidth = size.width * 2
                    val arcHeight = 3600f
                    drawArc(
                        color = Blue2.copy(alpha = 0.05f),
                        startAngle = 0f,
                        sweepAngle = -180f,
                        useCenter = false,
                        topLeft = Offset(-size.width / 2, -arcHeight / 2),
                        size = Size(canvasWidth, arcHeight)
                    )
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 450.dp)
                ) {
                    val canvasWidth = size.width * 2
                    val arcHeight = 3600f
                    drawArc(
                        color = Blue2.copy(alpha = 0.1f),
                        startAngle = 0f,
                        sweepAngle = -180f,
                        useCenter = false,
                        topLeft = Offset(-size.width / 2, -arcHeight / 2),
                        size = Size(canvasWidth, arcHeight)
                    )
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 550.dp),
                ) {
                    val canvasWidth = size.width * 2
                    val arcHeight = 3600f
                    drawArc(
                        color = Blue2,
                        startAngle = 0f,
                        sweepAngle = -180f,
                        useCenter = false,
                        topLeft = Offset(-size.width / 2, -arcHeight / 2),
                        size = Size(canvasWidth, arcHeight)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(55.dp),
                        onClick = { navController.navigate(Screen.LoginScreen.route) },
                        shape = RoundedCornerShape(17.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WhiteBlue1),
                    ) {
                        Text(
                            text = "Log In", color = Blue1, fontFamily = ManropeFamily,
                            fontSize = 19.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(55.dp),
                        onClick = { navController.navigate(Screen.SignUpScreen.route) },
                        shape = RoundedCornerShape(17.dp),
                        border = BorderStroke(2.dp, Color.White),
                    ) {
                        Text(
                            text = "Sign Up", color = Color.White, fontFamily = ManropeFamily,
                            fontSize = 19.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
        Box(modifier = Modifier.padding(top = 220.dp, start = 50.dp)) {
            Canvas(
                modifier = Modifier
                    .size(8.dp),
                onDraw = {
                    drawCircle(
                        color = Blue1
                    )
                }
            )
        }
        Box(modifier = Modifier.padding(top = 260.dp, start = 230.dp)) {
            Canvas(
                modifier = Modifier
                    .size(10.dp),
                onDraw = {
                    drawCircle(
                        color = Blue1
                    )
                }
            )
        }
        Box(modifier = Modifier.padding(top = 370.dp, start = 355.dp)) {
            Canvas(
                modifier = Modifier
                    .size(10.dp),
                onDraw = {
                    drawCircle(
                        color = Blue1
                    )
                }
            )
        }
    }
}
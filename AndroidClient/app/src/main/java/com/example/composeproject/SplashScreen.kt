package com.example.composeproject

import androidx.compose.animation.core.Animatable
import android.graphics.ColorMatrix
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.ui.theme.*
import kotlinx.coroutines.delay

@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun SplashScreen(navController: NavController) {

    val scale = remember {
        Animatable(0f)
    }

    val opacity = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true)
    {
        scale.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1000L)
        opacity.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1500L)
        navController.navigate(Screen.AuthenticationScreen.route){
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to PurpleLogo,
                        //0.5f to Blue5,
                        0.9f to AquaLogo
                    )
                )
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = 6.dp)
                    .scale(scale.value),
                painter = painterResource(id = R.drawable.logo),
                colorFilter = ColorFilter.tint(
                    color = Grey1
                ),
                contentDescription = "logo_shadow"
            )
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = (-125).dp)
                    .scale(scale.value),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )

            Text(
                modifier = Modifier
                    .offset(y = (-75).dp)
                    .alpha(opacity.value),
                text = "Sweat Buddy",
                color = colorResource(id = R.color.white),
                fontFamily = OutfitFamily,
                fontSize = 52.sp,
            )
        }

    }


}
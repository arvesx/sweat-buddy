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
import com.example.composeproject.viewmodel.SharedViewModel

@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun RoutesScreen(navController: NavController, sharedViewModel: SharedViewModel) {

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
        ScreenUpperSection(SectionContent.ROUTES, 120.dp, navController)
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
            RoutesTopCard(
                sharedViewModel.totalNumberOfRoutes.value,
                sharedViewModel.totalKilometeres.value
            )
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
                ContentList(SectionContent.ROUTES, sharedViewModel, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesTopCard(totalNumberOfRoutes: Int, totalKilometers: Float) {
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
                    text = String.format("%.1f", totalKilometers) + " km",
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

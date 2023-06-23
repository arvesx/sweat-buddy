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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.android.animation.SegmentType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeproject.data.RouteInfo
import com.example.composeproject.data.SegmentInfo
import com.example.composeproject.ui.theme.*
import com.example.composeproject.viewmodel.SharedViewModel

@Composable
fun SegmentsScreen(navController: NavController, sharedViewModel: SharedViewModel) {

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
    )
    {
        ScreenUpperSection(SectionContent.SEGMENTS, 120.dp, navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            SegmentsTopCard( 2, 3
                //sharedViewModel.totalNumberOfRoutes.value,
                //sharedViewModel.totalKilometeres.value
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Your segments",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 16.sp
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
                ContentList(SectionContent.SEGMENTS, sharedViewModel, navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentsTopCard(totalNumberOfSegments: Int, bestPosition: Int) {
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
                    text = "$totalNumberOfSegments",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total segments",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = "you participate in",
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
                    text = "$bestPosition",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFamily,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Best position you",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = "have in a segment",
                    fontWeight = FontWeight.Light,
                    fontFamily = ManropeFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f),
                )
            }
        }
    }
}
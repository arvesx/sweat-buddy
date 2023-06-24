package com.example.composeproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeproject.dependencies.user.Route
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.utils.calculateCameraPosition
import com.example.composeproject.viewmodel.NewSegmentViewModel
import com.example.composeproject.viewmodel.SharedViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NewSegmentScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Blue2,
                        1f to Blue1,
                    ),
                )
            )
    ) {
        var coordinates by remember {
            mutableStateOf(emptyList<LatLng>()) //emptyList<LatLng>()
        }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(1.35, 103.87), 10f)
        }

        val viewModel: NewSegmentViewModel = viewModel()

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
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
                        text = "Segments", color = colorResource(id = R.color.white),
                        fontFamily = ManropeFamily,
                        fontSize = 18.sp,
                    )

                    TextButton (
                        onClick = {
                            viewModel.onCreate(navController, sharedViewModel)
                        },
                        modifier = Modifier.clip(androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Text(
                            text = "Create", color = Color.White, fontFamily = ManropeFamily,
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(130.dp))
            Card(
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                modifier = Modifier
                    .fillMaxSize()

            ) {

                MapScreen(
                    coordinates,
                    cameraPositionState,
                    canSelectSegment = true,
                    sharedViewModel
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
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
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            //Spinner
                            Spinner(
                                viewModel,
                                navController,
                                routes = sharedViewModel.routes.value
                            ) { selectedCoordinates ->
                                coordinates = selectedCoordinates
                                val newCameraLatLng = calculateCameraPosition(coordinates)
                                val cameraNewZoom = 14.0f

                                cameraPositionState.position =
                                    CameraPosition.fromLatLngZoom(newCameraLatLng, cameraNewZoom)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RouteNameTextField(
                                title = "Give your segment a name",
                                viewModel.textFieldValue
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                        }
                        if (viewModel.selectedRoute.value.routeName != null) {

                            Text(
                                text = "Adjust your segment:",
                                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                                fontFamily = ManropeFamily,
                                fontSize = 14.sp,
                                color = Color(0, 0, 0, 0xBF)

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
fun Spinner(
    viewModel: NewSegmentViewModel,
    navController: NavController,
    routes: List<Route>,
    onResponse: (List<LatLng>) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .wrapContentSize(Alignment.TopStart),
        expanded = viewModel.isExpanded.value,
        onExpandedChange = {
            viewModel.isExpanded.value = it
        }
    )
    {
        var curRouteName = ""
        if (viewModel.selectedRoute.value.routeName != null) {
            curRouteName = viewModel.selectedRoute.value.routeName
        }

        OutlinedTextField(
            value = curRouteName,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Select your Route") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.isExpanded.value)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Blue2,
                focusedLabelColor = Blue2,
                unfocusedIndicatorColor = Color.Black,
                containerColor = Color.White,
            ),
            modifier = Modifier
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = viewModel.isExpanded.value,
            onDismissRequest = { viewModel.isExpanded.value = false },
            modifier = Modifier
                .background(Color.White)
        )
        {
            routes.forEach { route ->
                DropdownMenuItem(
                    text = { Text(text = route.routeName) },
                    onClick = {
                        viewModel.onPressItem(
                            navController,
                            route
                        ) { selectedCoordinates ->
                            onResponse(selectedCoordinates)
                        }

                    },
                )
            }
        }

    }


}

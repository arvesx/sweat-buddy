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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.MapStyle
import com.example.composeproject.utils.getGpxWaypoints
import com.example.composeproject.viewmodel.NewRouteViewModel
import com.example.composeproject.viewmodel.RegisterViewModel
import com.example.composeproject.viewmodel.SharedViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun NewRouteScreen(navController: NavController, sharedViewModel: SharedViewModel) {
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

        val viewModel: NewRouteViewModel = viewModel()

        // File picker
        var showFilePicker by remember { mutableStateOf(false) }
        val context = LocalContext.current

        FilePicker(showFilePicker, fileExtensions = listOf("xml")) { path ->
            showFilePicker = false
            if (path != null) {
                viewModel.pathChosen.value = path.path
                getGpxWaypoints(
                    viewModel.pathChosen.value,
                    context
                ) { newList, cameraNewLatLng, cameraNewZoom ->
                    coordinates = newList

                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(cameraNewLatLng, cameraNewZoom)

                    println(coordinates)
                    println(cameraPositionState.position)
                }

            }
            showFilePicker = false
        }


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


                    TextButton(
                        onClick = {
                            viewModel.onCreate(navController, sharedViewModel, context)
                        },
                        modifier = Modifier.clip(CircleShape)
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

                MapScreen(coordinates, cameraPositionState)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column() {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                            RouteNameTextField(
                                title = "Give your route a name",
                                viewModel.textFieldValue
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = { showFilePicker = true },
                                modifier = Modifier.clip(CircleShape),
                            ) {
                                Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                            }
                        }

                        Text(
                            text = "After you select a file your route will appear on the map:",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }

                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteNameTextField(title: String, text: MutableState<TextFieldValue>) {
    OutlinedTextField(
        value = text.value,
        label = { Text(text = title) },
        onValueChange = {
            text.value = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Blue2
        ),
    )
}

@Composable
fun MapScreen(coordinates: List<LatLng>, cameraPositionState: CameraPositionState) {

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions(MapStyle.json1)
        )
    ) {
        if (coordinates.isNotEmpty()) {
            Circle(center = coordinates[0], radius = 15.0, fillColor = Blue2, strokeColor = Blue2)
            Circle(
                center = coordinates[coordinates.size - 1],
                radius = 15.0,
                fillColor = Blue2,
                strokeColor = Blue2
            )
            Polyline(
                points = coordinates, color = Blue2, jointType = JointType.ROUND
            )
        }


    }
}
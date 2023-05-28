package com.example.composeproject.utils

import android.content.Context
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

val parser = GPXParser()

@OptIn(DelicateCoroutinesApi::class)
fun getGpxWaypoints(
    path: String,
    context: Context,
    onResponse: (List<LatLng>, LatLng, Float) -> Unit
) {
    GlobalScope.launch {
        val coordinatesList = mutableListOf<LatLng>()
        try {
            val input: InputStream? = context.contentResolver.openInputStream(Uri.parse(path))
            val parsedGpx: Gpx? = parser.parse(input)
            parsedGpx?.let {
                for (wp in it.wayPoints) {
                    val point = LatLng(wp.latitude, wp.longitude)
                    coordinatesList.add(point)
                }
                val newCameraLatLng = calculateCameraPosition(coordinatesList)
                withContext(Dispatchers.Main) {
                    onResponse(coordinatesList, newCameraLatLng, 14.0f)
                }
            } ?: {
                // error parsing track
            }
        } catch (e: IOException) {
            // do something with this exception
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            // do something with this exception
            e.printStackTrace()
        }
    }
}

fun calculateCameraPosition(waypointsList: List<LatLng>): LatLng {
    val wpNumber = waypointsList.size
    var latTotal = 0.0
    var lonTotal = 0.0
    for (wp in waypointsList) {
        latTotal += wp.latitude
        lonTotal += wp.longitude
    }
    return LatLng(latTotal / wpNumber, lonTotal / wpNumber)
}
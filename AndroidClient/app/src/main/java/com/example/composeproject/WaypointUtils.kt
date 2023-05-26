package com.example.composeproject

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

val parser = GPXParser()

@OptIn(DelicateCoroutinesApi::class)
fun getGpxWaypoints(path: String, context: Context, onResponse: (List<LatLng>) -> Unit) {
    GlobalScope.launch {
        val coordinatesList = mutableListOf<LatLng>()
        try {
            val input: InputStream = context.assets.open(path)
            val parsedGpx: Gpx? = parser.parse(input) // consider using a background thread
            parsedGpx?.let {
                for (wp in it.wayPoints) {
                    coordinatesList.add(LatLng(wp.latitude, wp.longitude))
                }
                onResponse(coordinatesList)
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
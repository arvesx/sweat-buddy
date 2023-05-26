package com.example.composeproject.data

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser
import java.net.URL

object GeoDirections {
    // Uses OpenRouteService's API to find the best route between 2 points.

    fun makeApiRequest(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): MutableList<LatLng> {
        val finalCoordinatesList = mutableListOf<LatLng>()


        val apiKey = "5b3ce3597851110001cf624881846c35bb0242aaa79d92bff2cfeb7f"

        val jsonString =
            URL("https://api.openrouteservice.org/v2/directions/foot-walking?api_key=$apiKey&start=$startLongitude,$startLatitude&end=$endLongitude,$endLatitude").readText()


        val coordinates = JsonParser.parseString(
            JsonParser.parseString(jsonString)
                .asJsonObject.get("features")
                .asJsonArray[0].toString()
        )
            .asJsonObject.get("geometry")
            .asJsonObject
            .get("coordinates")
            .asJsonArray.asList()

        for (i in coordinates) {
            val latLng = LatLng(
                i.asJsonArray[1].asDouble,
                i.asJsonArray[0].asDouble
            )
            finalCoordinatesList.add(latLng)
        }
        return finalCoordinatesList
    }
}

fun main() {
//    GeoDirections.makeApiRequest(
//        38.0225277212646,
//        23.707137722238368,
//        38.01276758232183,
//        23.75313026588741,
//    )
}
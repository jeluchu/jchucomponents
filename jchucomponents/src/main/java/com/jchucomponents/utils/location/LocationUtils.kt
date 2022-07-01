/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.location

import kotlin.math.*

/**
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 *
 *
 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
 * el2 End altitude in meters
 *
 * @returns Distance in Meters
 */
fun getDistance(
    lat1: Double, lat2: Double, lon1: Double,
    lon2: Double
): Double {
    val radiusOfEarth = 6371
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = (sin(latDistance / 2) * sin(latDistance / 2)
            + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
            * sin(lonDistance / 2) * sin(lonDistance / 2)))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    var distance = radiusOfEarth * c * 1000
    distance = distance.pow(2.0) + 0.0.pow(2.0)
    return sqrt(distance)
}
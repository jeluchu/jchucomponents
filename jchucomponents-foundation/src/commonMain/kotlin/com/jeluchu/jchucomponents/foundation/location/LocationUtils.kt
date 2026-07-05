package com.jeluchu.jchucomponents.foundation.location

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/** Calculates the Haversine distance between two coordinates in meters. */
fun getDistanceInMeters(
    latitudeStart: Double,
    longitudeStart: Double,
    latitudeEnd: Double,
    longitudeEnd: Double,
    altitudeStart: Double = 0.0,
    altitudeEnd: Double = 0.0
): Double {
    val latitudeDistance = (latitudeEnd - latitudeStart).toRadians()
    val longitudeDistance = (longitudeEnd - longitudeStart).toRadians()
    val haversine =
        sin(latitudeDistance / 2).pow(2.0) +
            cos(latitudeStart.toRadians()) *
            cos(latitudeEnd.toRadians()) *
            sin(longitudeDistance / 2).pow(2.0)
    val angularDistance = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))
    val groundDistance = EARTH_RADIUS_KM * angularDistance * METERS_IN_KILOMETER
    val altitudeDistance = altitudeStart - altitudeEnd

    return sqrt(groundDistance.pow(2.0) + altitudeDistance.pow(2.0))
}

private fun Double.toRadians(): Double = this * PI / 180.0

private const val EARTH_RADIUS_KM = 6371
private const val METERS_IN_KILOMETER = 1000

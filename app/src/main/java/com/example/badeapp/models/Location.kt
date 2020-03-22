package com.example.badeapp.models

sealed class Location(
    val lat: String,
    val lon: String,
    val name: String) {

    lateinit var weatherForecast: WeatherForecast
    lateinit var oceanForecast: OceanForecast

    object Hovedoya: Location("59.898397", "10.738595", "Hovedøya")
    object Sorenga:  Location("59.894894", "10.724028", "Sørenga")
    object Tjuvholmen: Location("59.906102", "10.720453", "Tjuvholmen")
    object Paradisbukta: Location("59.901614", "10.665422", "Paradisbukta")
}

// For å få tak i listen med objekter:
//  val locations: List<Location> = Location::class.nestedClasses.map {
//    it.objectInstance as Location
//}
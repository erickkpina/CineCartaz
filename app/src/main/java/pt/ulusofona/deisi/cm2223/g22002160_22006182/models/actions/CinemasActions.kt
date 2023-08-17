package pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema

abstract class CinemasActions {
    abstract fun getCinemas(onFinished: (Result<List<Cinema>>) -> Unit)
    abstract fun insertCinema(cinema: Cinema, onFinished: () -> Unit)
    abstract fun clearAllCinemas(onFinished: () -> Unit)
    abstract fun getCinemaById(id: Int, onFinished: (Result<Cinema>) -> Unit)
    abstract fun getCinemaByName(id: String, onFinished: (Result<String>) -> Unit)
}
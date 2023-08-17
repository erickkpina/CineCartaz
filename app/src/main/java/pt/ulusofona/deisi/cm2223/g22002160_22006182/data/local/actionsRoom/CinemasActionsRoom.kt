package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.CinemaDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.CinemasActions

class CinemasActionsRoom(private val dao: CinemaDao) : CinemasActions() {
    override fun getCinemas(onFinished: (Result<List<Cinema>>) -> Unit) {

        val cinemas = dao.getAll().map {
            Cinema(
                it.id,
                it.cinemaName,
                it.latitude,
                it.longitude,
                it.address,
                it.postcode,
                it.county,
                it.foto
            )
        }
        onFinished(Result.success(cinemas))

    }

    override fun insertCinema(cinema: Cinema, onFinished: () -> Unit) {
        val cinema2 = CinemasTable(
            cinema.cinemaId,
            cinema.cinemaName,
            cinema.latitude,
            cinema.longitude,
            cinema.address,
            cinema.postcode,
            cinema.country,
            cinema.foto
        )
        dao.insert(cinema2)
        onFinished()
    }

    override fun clearAllCinemas(onFinished: () -> Unit) {
        dao.deleteAll()
        onFinished()
    }

    override fun getCinemaById(id: Int, onFinished: (Result<Cinema>) -> Unit) {
        val cinema = dao.getById(id.toString())
        val cinema2 = Cinema(
            cinema.id,
            cinema.cinemaName,
            cinema.latitude,
            cinema.longitude,
            cinema.address,
            cinema.postcode,
            cinema.county,
            cinema.foto
        )
        onFinished(Result.success(cinema2))
    }

    override fun getCinemaByName(id: String, onFinished: (Result<String>) -> Unit) {
        onFinished(Result.success(dao.getNameById(id)))
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.CinemasActions

class CinemasRepository(
    private val local: CinemasActions
) : CinemasActions() { // Busca as funções do CinemasActionsRoom

    override fun getCinemas(onFinished: (Result<List<Cinema>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getCinemas(onFinished)
        }
    }

    override fun insertCinema(cinema: Cinema, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.insertCinema(cinema,onFinished)
        }
    }

    override fun clearAllCinemas(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.clearAllCinemas(onFinished)
        }
    }

    override fun getCinemaById(id: Int, onFinished: (Result<Cinema>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getCinemaById(id, onFinished)
        }
    }

    override fun getCinemaByName(id: String, onFinished: (Result<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getCinemaByName(id, onFinished)
        }
    }

    companion object {

        private var instance: CinemasRepository? = null

        // Temos de executar o init antes do getInstance
        fun init(local: CinemasActions) {
            if (instance == null) {
                instance = CinemasRepository(local)
            }
        }

        fun getInstance(): CinemasRepository {
            if (instance == null) {
                // Primeiro temos de invocar o init, se não lança esta Exception
                throw IllegalStateException("singleton not initialized")
            }
            return instance as CinemasRepository
        }
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.CinemasActions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.OpinioesActions

class OpinioesRepository (
    private val local: OpinioesActions
) : OpinioesActions() { // Busca as funções do OpinioesActionsRoom

    override fun getOpinioes(onFinished: (Result<List<Opiniao>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getOpinioes(onFinished)
        }
    }

    override fun insertOpiniao(opiniao: OpiniaoTable, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.insertOpiniao(opiniao,onFinished)
        }
    }

    override fun clearAllOpinioes(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.clearAllOpinioes(onFinished)
        }
    }

    override fun getOpiniaoByFilmeId(filmeId: String, onFinished: (Result<Opiniao>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getOpiniaoByFilmeId(filmeId,onFinished)
        }
    }

    companion object {

        private var instance: OpinioesRepository? = null

        // Temos de executar o init antes do getInstance
        fun init(local: OpinioesActions) {
            if (instance == null) {
                instance = OpinioesRepository(local)
            }
        }

        fun getInstance(): OpinioesRepository {
            if (instance == null) {
                // Primeiro temos de invocar o init, se não lança esta Exception
                throw IllegalStateException("singleton not initialized")
            }
            return instance as OpinioesRepository
        }

    }
}
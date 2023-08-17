package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FotosTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FotosActions

class FotosRepository(
    private val local: FotosActions
) : FotosActions(){
    override fun getFotos(onFinished: (Result<List<FotosTable>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getFotos(onFinished)
        }
    }

    override fun insertFoto(opiniaoId: String, foto: String, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.insertFoto(opiniaoId, foto, onFinished)
        }
    }

    override fun getByOpiniaoId(id: String, onFinished: (Result<List<String>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getByOpiniaoId(id, onFinished)
        }
    }

    override fun clearAllFotos(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.clearAllFotos(onFinished)
        }
    }

    companion object {

        private var instance: FotosRepository? = null

        // Temos de executar o init antes do getInstance
        fun init(local: FotosActions) {
            if (FotosRepository.instance == null) {
                FotosRepository.instance = FotosRepository(local)
            }
        }

        fun getInstance(): FotosRepository {
            if (instance == null) {
                // Primeiro temos de invocar o init, se não lança esta Exception
                throw IllegalStateException("singleton not initialized")
            }
            return instance as FotosRepository
        }

    }
}
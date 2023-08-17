package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.ConnectivityUtil
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FilmesActions

class FilmesRepository(
    private val context: Context,
    private val local: FilmesActions,
    private val remote: FilmesActions
) : FilmesActions() {

    override fun getFilmeByTitle(titulo: String, onFinished: (Result<Filme>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            if (ConnectivityUtil.isOnline(context)) {
                remote.getFilmeByTitle(titulo) { result ->
                    if (result.isSuccess) {
                        result.getOrNull()?.let { filme ->
                            // Se tiver personagens para apresentar entra aqui
                            Log.i("APP", "Got ${filme.nome} from the server")
                            // Retirar esta linha quando forem fazer o exercício 1 da ficha
                            onFinished(Result.success(filme))
                        }
                    } else {
                        Log.w("APP", "Error getting movies from server")
                        onFinished(result)  // propagate the remote failure
                    }
                }

            } else {
                Log.i("APP", "App is offline. Getting movies from the database")
                local.getFilmeByTitle(titulo, onFinished)
            }
        }
    }

    override fun getFilmeById(filmeId: String, onFinished: (Result<Filme>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getFilmeById(filmeId, onFinished)
        }
    }

    override fun getFilmes(onFinished: (Result<List<Filme>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            local.getFilmes(onFinished)
        }
    }

    override fun existsId(id: String, onFinished: (Result<Boolean>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            local.existsId(id, onFinished)
        }
    }

    override fun getFilmesBySearch(titulo: String, onFinished: (Result<List<String>>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            remote.getFilmesBySearch(titulo) { result ->
                if (result.isSuccess) {
                    result.getOrNull()?.let { filmes ->
                        Log.i("APP", "Got ${filmes.size} movies from the server")
                        onFinished(Result.success(filmes))
                    }
                } else {
                    Log.w("APP", "Error getting movies from server")
                    onFinished(result)
                }
            }
        } else {
            Log.i("APP", "App is offline. Getting movies from the database")
            local.getFilmesBySearch(titulo, onFinished)
        }
    }

    override fun insertFilme(filme: Filme, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            local.insertFilme(filme) {
                onFinished()
            }
        }
    }

    override fun clearAllFilmes(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            local.clearAllFilmes(onFinished)
        }
    }

    companion object {

        private var instance: FilmesRepository? = null

        // Temos de executar o init antes do getInstance
        fun init(context: Context, local: FilmesActions, remote: FilmesActions) {
            if (instance == null) {
                instance = FilmesRepository(context, local, remote)
            }
        }

        fun getInstance(): FilmesRepository {
            if (instance == null) {
                // Primeiro temos de invocar o init, se não lança esta Exception
                throw IllegalStateException("singleton not initialized")
            }
            return instance as FilmesRepository
        }

    }


}
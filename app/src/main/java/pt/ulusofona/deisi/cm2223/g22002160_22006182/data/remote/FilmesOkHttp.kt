package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.remote

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FilmesActions
import java.io.IOException
import java.util.*

class FilmesOkHttp(
    private val baseUrl: String,
    private val apiKey: String,
    private val client: OkHttpClient
) : FilmesActions() {
    private val conversores = Conversores()
    override fun getFilmeByTitle(titulo: String, onFinished: (Result<Filme>) -> Unit) {

        val request: Request = Request.Builder()
            .url("$baseUrl/?apikey=$apiKey&t=$titulo")
            .build()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                // Se a resposta devolver um erro, ex: 403 acesso negado ao web service
                if (!response.isSuccessful) {
                    onFinished(Result.failure(IOException("Unexpected code $response")))
                } else {
                    val body = response.body?.string()
                    if (body != null) {
                        // Estamos a guardar o objeto assinalado a amarelo no exemplo aqui
                        val jsonObject = JSONObject(body)
                        var data = jsonObject.getString("Released")
                        if (data == "N/A") {
                            data = "N/A"
                        }else{
                            data = conversores.convertStringToTimestamp(jsonObject.getString("Released"))
                        }
                        val filme = Filme(
                            jsonObject.getString("imdbID"),
                            jsonObject.getString("Title"),
                            jsonObject.getString("Poster"),
                            jsonObject.getString("Genre").split(", ")[0],
                            jsonObject.getString("Plot"),
                            data,
                            jsonObject.getString("imdbRating"),
                            jsonObject.getString("imdbVotes").replace(",", ""),
                            "https://www.imdb.com/title/${jsonObject.getString("imdbID")}/",
                        )
                        onFinished(Result.success(filme))
                    }
                }
            }
        })
    }

    override fun getFilmesBySearch(titulo: String, onFinished: (Result<List<String>>) -> Unit) {
        val request: Request = Request.Builder()
            .url("$baseUrl/?apikey=$apiKey&s=$titulo")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onFinished(Result.failure(IOException("Unexpected code $response")))
                } else {
                    val body = response.body?.string()
                    if (body != null) {
                        val jsonObject = JSONObject(body)
                        if (jsonObject.getString("Response") == "False" && jsonObject.getString("Error") == "Too many results.") {
                            Log.i("App", "Too many results.")
                            onFinished(Result.success(mutableListOf<String>()))
                            return
                        }
                        if (jsonObject.getString("Response") == "False" && jsonObject.getString("Error") == "Movie not found!") {
                            Log.i("App", "Movie not found!")
                            onFinished(Result.success(mutableListOf<String>()))
                            return

                        }
                        val jsonFilmeList = jsonObject["Search"] as JSONArray
                        val titlesList = mutableListOf<String>()
                        for (i in 0 until jsonFilmeList.length()) {
                            val filme = jsonFilmeList[i] as JSONObject
                            titlesList.add(filme.getString("Title"))
                        }
                        onFinished(Result.success(titlesList))
                    }
                }
            }
        })
    }

    override fun getFilmeById(filmeId: String, onFinished: (Result<Filme>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getFilmes(onFinished: (Result<List<Filme>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun existsId(id: String, onFinished: (Result<Boolean>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun insertFilme(filme: Filme, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllFilmes(onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }
}

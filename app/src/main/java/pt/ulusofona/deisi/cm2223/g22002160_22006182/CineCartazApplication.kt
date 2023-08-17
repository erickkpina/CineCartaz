package pt.ulusofona.deisi.cm2223.g22002160_22006182

import android.app.Application
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom.CinemasActionsRoom
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom.FilmesActionsRoom
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom.FotosActionsRoom
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom.OpinioesActionsRoom
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.database.AppDatabase
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.remote.FilmesOkHttp
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FotosRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.CinemasActions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FilmesActions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FotosActions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.OpinioesActions
import java.io.InputStream
import java.io.InputStreamReader

class CineCartazApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FilmesRepository.init(this, initFilmesRoom(), initFilmesOkHttp())
        CinemasRepository.init(initCinemasRoom())
        OpinioesRepository.init(initOpinioesRoom())
        FotosRepository.init(initFotosRoom())
        getCinemas(this)

    }

    private fun initFilmesOkHttp(): FilmesOkHttp {
        return FilmesOkHttp(
            "https://www.omdbapi.com",
            "4cff25fd",
            OkHttpClient()
        )
    }

    private fun initFilmesRoom(): FilmesActions {
        return FilmesActionsRoom(
            AppDatabase.getInstance(applicationContext).filmeDao()
        )
    }

    private fun initCinemasRoom(): CinemasActions {
        return CinemasActionsRoom(
            AppDatabase.getInstance(applicationContext).cinemaDao()
        )
    }

    private fun initOpinioesRoom(): OpinioesActions {
        return OpinioesActionsRoom(
            AppDatabase.getInstance(applicationContext).opiniaoDao()
        )
    }

    private fun initFotosRoom(): FotosActions {
        return FotosActionsRoom(
            AppDatabase.getInstance(applicationContext).fotoDao()
        )
    }

    private fun getCinemas(context: Context) {
        val jsonString = lerArquivoJson(context)
        val jsonObject = JSONObject(jsonString)
        val cinemasArray = jsonObject["cinemas"] as JSONArray


        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0 until cinemasArray.length()) {
                val cinema = cinemasArray.getJSONObject(i)
                var foto = "N/A"
                if (cinema.has("photos")){
                    if (cinema.getJSONArray("photos").length() > 0)
                        foto =  cinema.getJSONArray("photos").getString(0)
                }
                CinemasRepository.getInstance().insertCinema(
                    Cinema(
                        cinema.getInt("cinema_id"),
                        cinema.getString("cinema_name"),
                        cinema.getDouble("latitude"),
                        cinema.getDouble("longitude"),
                        cinema.getString("address"),
                        cinema.getString("postcode"),
                        cinema.getString("county"),
                        foto
                    )
                ) {
                    Log.i("Cinemas", "Cinema inserted ${cinema.getString("cinema_name")} ")
                }
            }
            Log.i("APP", "Li todos os cinemas")
        }
    }

    private fun lerArquivoJson(context: Context): String {
        val inputStream: InputStream = context.assets.open("cinemas.json")
        val reader = InputStreamReader(inputStream)
        val jsonString = reader.readText()
        reader.close()
        inputStream.close()
        return jsonString
    }
}

package pt.ulusofona.deisi.cm2223.g22002160_22006182.models

import android.graphics.Bitmap
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import java.text.SimpleDateFormat
import java.util.*

class Opiniao(
    val id: String,
    val avaliacao: Int,
    val dataTimeStamp: String,
    val observacoes: String,
    val filmeId :String,
    val cinemaId: Int,
) {

}
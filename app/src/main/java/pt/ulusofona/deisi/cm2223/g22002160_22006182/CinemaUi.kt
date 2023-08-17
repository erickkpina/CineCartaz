package pt.ulusofona.deisi.cm2223.g22002160_22006182

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
class CinemaUi(
    val cinemaId: Int,
    val cinemaName: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val postcode: String,
    val country: String,
    val foto: String
) : Parcelable {
}
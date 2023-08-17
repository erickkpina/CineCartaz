package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class CinemasTable (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "cinema_name") val cinemaName: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val postcode: String,
    val county: String,
    val foto: String
)
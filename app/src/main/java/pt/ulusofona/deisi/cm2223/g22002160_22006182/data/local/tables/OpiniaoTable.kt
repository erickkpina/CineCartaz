package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables

import androidx.room.*
import java.util.*

@Entity(tableName = "opinioes")
data class OpiniaoTable(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val avaliacao: Int,
    @ColumnInfo(name = "data_timestamp") val dataTimestamp: String,
    val observacao: String,
    @ColumnInfo(name = "cinema_id") val cinemaId: Int,
    @ColumnInfo(name = "filme_id") val filmeId: String
)
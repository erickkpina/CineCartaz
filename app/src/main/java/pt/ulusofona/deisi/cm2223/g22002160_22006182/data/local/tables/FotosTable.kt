package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "fotos")
data class FotosTable(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "opiniao_id") val opiniaoId: String,
    val foto: String
)

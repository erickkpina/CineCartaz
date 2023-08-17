package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FotosTable

@Dao
interface FotoDao {
    @Insert
    fun insert(foto: FotosTable)
    @Query("SELECT * FROM fotos WHERE opiniao_id = :id")
    fun getByOpiniaoId(id: String): List<FotosTable>

    @Query("SELECT * FROM fotos")
    fun getAll(): List<FotosTable>

    @Query("DELETE FROM fotos")
    fun deleteAll()
}
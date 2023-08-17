package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable

@Dao
interface OpiniaoDao {
    @Insert
    fun insert(opiniao: OpiniaoTable)
    @Query("SELECT * FROM opinioes")
    fun getAll(): List<OpiniaoTable>
    @Query("SELECT * FROM opinioes WHERE filme_id = :filmeId")
    fun getOpiniaoByFilmeId(filmeId: String): OpiniaoTable

    @Query("SELECT * FROM opinioes WHERE id = :id")
    fun getById(id: String): OpiniaoTable

    @Query("DELETE FROM opinioes WHERE id = :id")
    fun deleteById(id: String)
}
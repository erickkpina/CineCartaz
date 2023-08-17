package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema

@Dao
interface CinemaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cinema: CinemasTable)

    @Query("SELECT * FROM cinemas ORDER BY cinema_name ASC")
    fun getAll(): List<CinemasTable>

    @Query("SELECT cinema_name FROM cinemas WHERE id = :id")
    fun getNameById(id: String): String

    @Query("SELECT * FROM cinemas WHERE id = :id")
    fun getById(id: String): CinemasTable

    @Query("DELETE FROM cinemas")
    fun deleteAll()

}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FilmesTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme

@Dao
interface FilmeDao {
    @Insert
    fun insert(movie: FilmesTable)

    @Query("SELECT * FROM filmes ORDER BY nome ASC")
    fun getAll(): List<FilmesTable>

    @Query("SELECT * FROM filmes WHERE id = :id")
    fun getById(id: String): FilmesTable

    @Query("SELECT EXISTS (SELECT 1 FROM filmes WHERE id = :id)")
    fun existsId(id: String): Boolean

    @Query("SELECT * FROM filmes ORDER BY nome DESC LIMIT 1")
    fun getLast(): FilmesTable

    @Query("DELETE FROM filmes WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM filmes")
    fun deleteAll()

    @Query("SELECT * FROM filmes WHERE nome LIKE :nome")
    fun getByTitle(nome: String): FilmesTable
}
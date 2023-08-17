package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FilmeDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FilmesTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FilmesActions
import java.util.Date

class FilmesActionsRoom(private val dao: FilmeDao) :
    FilmesActions() { // Implementa as funções da classe FilmesActions
    override fun getFilmeByTitle(titulo: String, onFinished: (Result<Filme>) -> Unit) {
        val filme = dao.getByTitle(titulo)
        val result = Filme(
            filme.id,
            filme.nome,
            filme.imagemCartaz,
            filme.genero,
            filme.sinopse,
            filme.dataLancamento,
            filme.avaliacaoIMDB,
            filme.numeroAvaliacoes,
            filme.linkIMDB
        )
        onFinished(Result.success(result))
    }

    override fun getFilmeById(filmeId: String, onFinished: (Result<Filme>) -> Unit) {
        val filme = dao.getById(filmeId)
        val result = Filme(
            filme.id,
            filme.nome,
            filme.imagemCartaz,
            filme.genero,
            filme.sinopse,
            filme.dataLancamento,
            filme.avaliacaoIMDB,
            filme.numeroAvaliacoes,
            filme.linkIMDB
        )
        onFinished(Result.success(result))
    }

    override fun getFilmes(onFinished: (Result<List<Filme>>) -> Unit) {
        val filmes = dao.getAll()
        val result = filmes.map {
            Filme(
                it.id,
                it.nome,
                it.imagemCartaz,
                it.genero,
                it.sinopse,
                it.dataLancamento,
                it.avaliacaoIMDB,
                it.numeroAvaliacoes,
                it.linkIMDB
            )
        }
        onFinished(Result.success(result))
    }

    override fun existsId(id: String, onFinished: (Result<Boolean>) -> Unit) {
        val result = dao.existsId(id)
        onFinished(Result.success(result))
    }

    override fun insertFilme(filme: Filme, onFinished: () -> Unit) {

            val filmeInsert = FilmesTable(
                filme.filmeId,
                filme.nome,
                filme.imagemCartaz,
                filme.genero,
                filme.sinopse,
                filme.dataLancamento,
                filme.avaliacaoIMDB,
                filme.numeroAvaliacoes,
                filme.linkIMDB
            )
            dao.insert(filmeInsert)
            Log.i("APP", "Inserted ${filme} in filmesDB")

        onFinished()

    }

    override fun getFilmesBySearch(titulo: String, onFinished: (Result<List<String>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllFilmes(onFinished: () -> Unit) {
        dao.deleteAll()
        onFinished()
    }


}
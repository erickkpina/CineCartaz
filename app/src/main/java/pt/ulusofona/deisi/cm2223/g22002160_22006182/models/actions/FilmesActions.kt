package pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions

import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme

abstract class FilmesActions {
    abstract fun getFilmeByTitle(titulo: String, onFinished: (Result<Filme>) -> Unit)
    abstract fun getFilmesBySearch(titulo: String, onFinished: (Result<List<String>>) -> Unit)
    abstract fun getFilmeById(filmeId: String, onFinished: (Result<Filme>) -> Unit)
    abstract fun getFilmes(onFinished: (Result<List<Filme>>) -> Unit)
    abstract fun existsId(id: String, onFinished: (Result<Boolean>) -> Unit)
    abstract fun insertFilme(filme: Filme, onFinished: () -> Unit)
    abstract fun clearAllFilmes(onFinished: () -> Unit)
}
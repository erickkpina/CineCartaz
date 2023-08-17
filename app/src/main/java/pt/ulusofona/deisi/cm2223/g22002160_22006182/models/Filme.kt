package pt.ulusofona.deisi.cm2223.g22002160_22006182.models

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FilmeDao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Filme(
    var filmeId: String,
    val nome: String,
    val imagemCartaz: String,
    val genero: String,
    val sinopse: String,
    val dataLancamento: String,
    val avaliacaoIMDB: String,
    val numeroAvaliacoes : String,
    val linkIMDB : String
) {

}
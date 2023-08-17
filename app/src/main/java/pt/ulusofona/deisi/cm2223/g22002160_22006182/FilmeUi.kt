package pt.ulusofona.deisi.cm2223.g22002160_22006182

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FilmeUi(
    val id: String,
    val nome: String,
    val imagemCartaz: String,
    val genero: String,
    val sinopse: String,
    val dataLancamento: String,
    val avaliacaoIMDB: String,
    val numeroAvaliacoes: String,
    val linkIMDB: String,
) : Parcelable {
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class FilmesTable (
    @PrimaryKey var id: String,
    val nome: String,
    @ColumnInfo(name = "imagem_cartaz") val imagemCartaz: String,
    val genero: String,
    val sinopse: String,
    @ColumnInfo(name = "data_lancamento") val dataLancamento: String,
    @ColumnInfo(name = "avaliacao_imdb") val avaliacaoIMDB: String,
    @ColumnInfo(name = "numero_avaliacoes") val numeroAvaliacoes : String,
    @ColumnInfo(name = "link_imdb") val linkIMDB : String
)


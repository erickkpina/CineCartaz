package pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.FilmeUi
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemFilmeBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemFilmesLandBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao

class FilmesAdapter(
    private var items: List<Filme> = listOf(),
    private val clicked: (FilmeUi) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val conversores = Conversores()

    inner class FilmesViewHolder(val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FilmesViewHolderLand(val binding: ItemFilmesLandBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val orientation = parent.context.resources.configuration.orientation

        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            FilmesViewHolderLand(
                ItemFilmesLandBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        } else {

            FilmesViewHolder(
                ItemFilmeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val filme = items[position]
        var opiniaoTemp: Opiniao? = null


        val filmeUi = FilmeUi(
            filme.filmeId,
            filme.nome,
            filme.imagemCartaz,
            filme.genero,
            filme.sinopse,
            conversores.timestampToString(filme.dataLancamento),
            filme.avaliacaoIMDB,
            filme.numeroAvaliacoes,
            filme.linkIMDB,
        )

        if (holder is FilmesViewHolder) {
            if (filme.imagemCartaz == "N/A") {
                Glide.with(holder.itemView.context).load(R.drawable.ic_poster)
                    .into(holder.binding.filmImage)
            } else {
                Glide.with(holder.itemView.context).load(filme.imagemCartaz)
                    .into(holder.binding.filmImage)
            }

            holder.binding.filmTitle.text = filme.nome
            if (filme.avaliacaoIMDB == "N/A") {
                holder.binding.ratingBar.visibility = View.GONE
            } else {
                holder.binding.ratingBar.rating = filme.avaliacaoIMDB.toFloat()
            }

            holder.binding.filmGenre.text = filme.genero
            holder.binding.realeseDate.text =
                conversores.timestampToString(filme.dataLancamento)
        } else if (holder is FilmesViewHolderLand) {
            if (filme.imagemCartaz == "N/A") {
                Glide.with(holder.itemView.context).load(R.drawable.ic_poster)
                    .into(holder.binding.filmImage)
            } else {
                Glide.with(holder.itemView.context).load(filme.imagemCartaz)
                    .into(holder.binding.filmImage)
            }
            holder.binding.filmTitle.text = filme.nome
            if (filme.avaliacaoIMDB == "N/A") {
                holder.binding.ratingBar.visibility = View.GONE
            } else {
                holder.binding.ratingBar.rating = filme.avaliacaoIMDB.toFloat()
            }
            if (filme.sinopse == "N/A") {
                holder.binding.filmSynospe.text = R.string.sinopseIndisponivel.toString()
            } else {
                holder.binding.filmSynospe.text = filme.sinopse
            }

            holder.binding.filmGenre.text = filme.genero
            holder.binding.realeseDate.text =
                conversores.timestampToString(filme.dataLancamento)
        }

        holder.itemView.setOnClickListener {
            clicked(filmeUi)
        }


    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<Filme>) {
        this.items = items
        notifyDataSetChanged()
    }
}



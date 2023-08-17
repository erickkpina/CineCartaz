package pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import pt.ulusofona.deisi.cm2223.g22002160_22006182.CinemaUi
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemCinemaBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema


class CinemasAdapter(
    private var items: List<Cinema> = listOf(),
    private val clicked: (CinemaUi) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val conversores = Conversores()

    inner class CinemasViewHolder(val binding: ItemCinemaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CinemasViewHolder(
            ItemCinemaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cinema = items[position]
        val cinemaUi = CinemaUi(
            cinema.cinemaId,
            cinema.cinemaName,
            cinema.latitude,
            cinema.longitude,
            cinema.address,
            cinema.postcode,
            cinema.country,
            cinema.foto
        )

        if (holder is CinemasViewHolder) {
            if (cinema.foto == "N/A") {
                holder.binding.imagemCinema.visibility = View.GONE
            } else {
                Glide.with(holder.itemView.context).load(cinema.foto).centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.binding.imagemCinema.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.binding.imagemCinema.visibility = View.VISIBLE
                            return false
                        }
                    }).into(holder.binding.imagemCinema)
            }
            holder.binding.cinemaName.setText(cinema.cinemaName)
            holder.binding.localidade.setText(cinema.country)
        }
        holder.itemView.setOnClickListener{
            clicked(cinemaUi)
        }

    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<Cinema>) {
        this.items = items
        notifyDataSetChanged()
    }
}

package pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemNovidadesBinding

class NovidadeAdapter(private val filmeList: List<Filme>) :
    RecyclerView.Adapter<NovidadeAdapter.NovidadeViewHolder>() {

    inner class NovidadeViewHolder(val binding: ItemNovidadesBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovidadeViewHolder {
        return NovidadeViewHolder(
            ItemNovidadesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NovidadeViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(filmeList[position].imagemCartaz).into(holder.binding.imagemFilme)
        holder.binding.filmTitle.setText(filmeList[position].nome)
    }

    override fun getItemCount(): Int {
        return filmeList.size
    }
}

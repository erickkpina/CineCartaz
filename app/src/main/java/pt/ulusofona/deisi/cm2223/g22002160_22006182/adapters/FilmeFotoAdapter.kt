package pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemFotoBinding

class FilmeFotoAdapter(private val imageList: MutableList<Bitmap>) :
    RecyclerView.Adapter<FilmeFotoAdapter.FilmeFotoViewHolder>() {

    inner class FilmeFotoViewHolder(val binding: ItemFotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeFotoViewHolder {
        return FilmeFotoViewHolder(
            ItemFotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilmeFotoViewHolder, position: Int) {
        val imageBitmap = imageList[position]
        holder.binding.ivPhoto.setImageBitmap(imageBitmap)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
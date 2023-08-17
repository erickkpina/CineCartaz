package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.CinemaUi
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentDetalhesCinemasBinding

private const val ARG_PARAM1 = "param1"

class CinemasDetalhesFragment() : Fragment() {
    private var cinemaUi: CinemaUi? = null
    private lateinit var binding: FragmentDetalhesCinemasBinding
    private var map: GoogleMap? = null
    private var conversores = Conversores()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cinemaUi = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_detalhes_cinemas, container, false
        )
        binding = FragmentDetalhesCinemasBinding.bind(view)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (cinemaUi != null) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = cinemaUi?.cinemaName
            if (cinemaUi?.foto == "N/A") {
                //binding.imagemCinema.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(cinemaUi?.foto).centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.imagemCinema.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.imagemCinema.visibility = View.VISIBLE
                            return false
                        }
                    }).into(binding.imagemCinema)
            }
            if (cinemaUi?.latitude != null && cinemaUi?.longitude != null) {
                var staticMapUrl =
                    "https://maps.googleapis.com/maps/api/staticmap?center=${cinemaUi?.latitude},${cinemaUi?.longitude}&zoom=16&size=400x400&markers=color:red|label:A|${cinemaUi?.latitude},${cinemaUi?.longitude}&key=AIzaSyBtrR-UTPJsGFC90YeCT3rMTwanUGivtVY"
                Glide.with(this)
                    .load(staticMapUrl)
                    .centerCrop()
                    .into(binding.map)
            }
            binding.address.setText(cinemaUi?.address)
            binding.postalCode.setText(cinemaUi?.postcode)

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(cinemaUi: CinemaUi) =
            CinemasDetalhesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, cinemaUi)
                }
            }
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.*
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentMapBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import java.util.*

//NAO FAZ ZOOM AO ACEITAR A PERMISSAO
class MapFragment : Fragment(), OnLocationChangedListener {
    private lateinit var binding: FragmentMapBinding
    //private lateinit var geocoder: Geocoder
    // Esta variável irá guardar uma referência para o mapa
    private var map: GoogleMap? = null
    private var conversores = Conversores()
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_map, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.filmes)
        binding = FragmentMapBinding.bind(view)
        if (!ConnectivityUtil.isOnline(requireContext())){
            binding.noWifi.visibility = View.VISIBLE
            binding.map.visibility = View.GONE
        }
        setupListButton()
        FusedLocation.start(this.requireContext())

        //MAPA
        binding.map.onCreate(savedInstanceState)
        //geocoder = Geocoder(context, Locale.getDefault())

        binding.map.getMapAsync { map ->
            this.map = map
            // Coloca um ponto azul no mapa com a localização do utilizador
            map.isMyLocationEnabled = true


            //ZOOM INICIAL
            val fusedLocation = FusedLocation.getInstance(requireContext())
            fusedLocation.getCurrentLocation { currentLatLng ->
                currentLatLng?.let {
                    placeCamera(currentLatLng.latitude, currentLatLng.longitude)
                }
            }

            map.setOnMarkerClickListener { marker ->
                val filmeName = marker.snippet
                if (filmeName != null){
                    FilmesRepository.getInstance().getFilmeByTitle(filmeName){
                        if (it.isSuccess){
                            val filme = it.getOrDefault(null)
                            if (filme != null){
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
                                NavigationManager.goToFilmesDetalhes(parentFragmentManager, filmeUi)
                            }
                        }
                    }
                }

                true
            }

            OpinioesRepository.getInstance().getOpinioes { result ->
                if (result.isSuccess){
                    val opinioes = result.getOrDefault(mutableListOf())
                    val cinemasLidos = mutableListOf<Int>()
                    opinioes.forEach { opiniao ->
                        FilmesRepository.getInstance().getFilmeById(opiniao.filmeId) {
                            if (it.isSuccess){
                                val filme = it.getOrDefault(null)
                                if (filme != null){
                                    CinemasRepository.getInstance().getCinemaById(opiniao.cinemaId) {
                                        if (it.isSuccess){
                                            val cinema = it.getOrDefault(null)
                                            if (cinema != null){
                                                if (!(cinema.cinemaId in cinemasLidos)){
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        map.addMarker(
                                                            MarkerOptions()
                                                                .position(LatLng(cinema.latitude, cinema.longitude))
                                                                .title(cinema.cinemaName)
                                                                .snippet(filme.nome)
                                                                .icon(BitmapDescriptorFactory.defaultMarker(corMarker(opiniao.avaliacao))))
                                                    }
                                                    cinemasLidos.add(cinema.cinemaId)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }
            FusedLocation.registerListener(this)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }


    // Este método será invocado sempre que a posição alterar
    override fun onLocationChanged(latitude: Double, longitude: Double) {
        //placeCamera(latitude, longitude)
    }

    // Atualiza e faz zoom no mapa de acordo com a localização
    private fun placeCamera(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(12f)
            .build()

        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    // Se o fragmento do mapa for destruído queremos parar de receber a
    // localização, se não podemos ter uma NullPointerException
    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener()
    }


    private fun setupListButton(){
        binding.mapFab.setOnClickListener {
            NavigationManager.goToFilmesFragment(parentFragmentManager)
        }
    }

    private fun corMarker(avaliacao: Int): Float{
        if (avaliacao in 1..2){
            //Muito Fraco
           return 0f
        }else if (avaliacao in 3..4) {
            //Fraco
            return 24f
        } else if (avaliacao in 5..6) {
            //Médio
            return 48f
        } else if (avaliacao in 7..8) {
            //Bom
            return 84f
        } else if (avaliacao in 9..10){
            //Excelente
           return 120f
        }
        throw java.lang.IllegalArgumentException("Avaliacao invalida")
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.Manifest
import android.util.Log
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import pt.ulusofona.deisi.cm2223.g22002160_22006182.*
import pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters.FilmesAdapter
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.remote.FilmesOkHttp
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentFilmesBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme

class FilmesFragment : Fragment() {
    private lateinit var binding: FragmentFilmesBinding
    private lateinit var adapter: FilmesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_filmes, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.filmes)
        binding = FragmentFilmesBinding.bind(view)

        FilmesRepository.getInstance().getFilmes(){result ->
            if (result.isSuccess){
                val filmesComOpiniao = result.getOrDefault(mutableListOf()).toMutableList()
                adapter = FilmesAdapter(filmesComOpiniao, ::filmClicked)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    if(adapter != null) {
                        binding.recyclerView.adapter = adapter
                    }
                    val recyclerView: RecyclerView = binding.recyclerView
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            if (dy > 0 && binding.mapFab.isExtended) {
                                // Scrolling down and the extended FAB is visible. Shrink the FAB
                                binding.mapFab.shrink()
                            } else if (dy < 0 && !binding.mapFab.isExtended) {
                                // Scrolling up and the extended FAB is hidden. Extend the FAB
                                binding.mapFab.extend()
                            }
                        }
                    })
                    setupMapButton()
                }

            }else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        requireContext(),
                        result.exceptionOrNull()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun filmClicked(filme: FilmeUi) {
        NavigationManager.goToFilmesDetalhes(parentFragmentManager, filme)
    }

    private fun setupMapButton() {
        binding.mapFab.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).build().send { result ->
                if (result.allGranted()) {
                    NavigationManager.goToMap(parentFragmentManager)
                }
            }

        }
    }
}

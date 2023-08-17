package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.CinemaUi
import pt.ulusofona.deisi.cm2223.g22002160_22006182.NavigationManager
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters.CinemasAdapter
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentExtraBinding

class ExtraFragment : Fragment() {
    private lateinit var binding: FragmentExtraBinding
    private lateinit var adapter: CinemasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_extra, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.cinemas)
        binding = FragmentExtraBinding.bind(view)

        CinemasRepository.getInstance().getCinemas(){ result ->
            if (result.isSuccess){
                val cinemas = result.getOrDefault(mutableListOf()).toMutableList()
                adapter = CinemasAdapter(cinemas, ::cinemaClicked)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    if(adapter != null) {
                        binding.recyclerView.adapter = adapter
                    }
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

    private fun cinemaClicked(cinema: CinemaUi){
        NavigationManager.goToCinemasDetalhes(parentFragmentManager,cinema)
    }

}
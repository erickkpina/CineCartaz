package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters.FilmeFotoAdapter
import pt.ulusofona.deisi.cm2223.g22002160_22006182.FilmeUi
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FotosRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentDetalhesBinding

private const val ARG_FILMEUI = "param1"

class DetalhesFragment : Fragment() {
    private var filmeUi: FilmeUi? = null
    private lateinit var binding: FragmentDetalhesBinding
    private lateinit var imageAdapter: FilmeFotoAdapter
    private var conversores = Conversores()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmeUi = it.getParcelable(ARG_FILMEUI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_detalhes, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.detalhes)
        binding = FragmentDetalhesBinding.bind(view)


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (filmeUi != null) {

            //CoroutineScope(Dispatchers.Main).launch {
            if (filmeUi?.imagemCartaz == "N/A") {
                Glide.with(requireContext()).load(R.drawable.ic_poster)
                    .into(binding.filmPoster)
            } else {
                Glide.with(requireContext()).load(filmeUi?.imagemCartaz).into(binding.filmPoster)
            }

            binding.filmTitle.setText(filmeUi?.nome)
            binding.releaseDate.setText(filmeUi?.dataLancamento)
            binding.filmGenre.setText(filmeUi?.genero)
            binding.imdbButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(filmeUi?.linkIMDB))
                startActivity(intent)
            }
            if (filmeUi?.avaliacaoIMDB == "N/A" && filmeUi?.numeroAvaliacoes == "N/A") {
                binding.filmRating.visibility = View.GONE
                binding.filmNumberRatings.visibility = View.GONE
                binding.imageView.visibility = View.GONE
                binding.textView.visibility = View.GONE
            }

            binding.filmRating.setText(filmeUi?.avaliacaoIMDB)
            if (filmeUi?.numeroAvaliacoes != null) {
                if (filmeUi?.numeroAvaliacoes == "N/A") {
                    binding.filmNumberRatings.setText("")
                } else {
                    binding.filmNumberRatings.setText(conversores.formatoNumeroAvaliacoes(filmeUi?.numeroAvaliacoes!!))
                }

            }
            if (filmeUi?.sinopse == "N/A") {
                binding.filmSynospe.setText(R.string.sinopseIndisponivel)
            } else {
                binding.filmSynospe.setText(filmeUi?.sinopse)
            }


            OpinioesRepository.getInstance().getOpiniaoByFilmeId(filmeUi!!.id) { result ->
                if (result.isSuccess) {
                    val opiniaoTemp = result.getOrNull()

                    if (opiniaoTemp != null) {
                        var opiniao = opiniaoTemp
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.ratingBar.rating = opiniao.avaliacao.toFloat()
                            binding.ratingValue.setText(opiniao.avaliacao.toString())
                            binding.data.setText(conversores.timestampToString(opiniao.dataTimeStamp))
                            CinemasRepository.getInstance().getCinemaById(opiniao.cinemaId) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (it.isSuccess) {

                                        binding.textCinema.text = it.getOrDefault(null)?.cinemaName
                                    }

                                    if (opiniao.observacoes == null) {
                                        binding.observacao.visibility = View.GONE
                                    } else {
                                        binding.observacao.visibility = View.VISIBLE
                                        binding.observacao.setText(opiniao.observacoes)
                                    }
                                }
                            }
                        }

                        var fotos: List<String>

                        FotosRepository.getInstance().getByOpiniaoId(opiniao.id) { result ->
                            if (result.isSuccess) {
                                fotos = result.getOrDefault(emptyList())
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (fotos.isNotEmpty()) {
                                        binding.observacao.visibility = View.VISIBLE
                                        imageAdapter =
                                            FilmeFotoAdapter(
                                                conversores.base64ListToBitmapList(
                                                    fotos
                                                )
                                            )
                                        binding.rvTeste.layoutManager =
                                            LinearLayoutManager(
                                                context,
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                        binding.rvTeste.adapter = imageAdapter
                                    } else {
                                        binding.rvTeste.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(filmeUi: FilmeUi) =
            DetalhesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILMEUI, filmeUi)
                }
            }
    }
}
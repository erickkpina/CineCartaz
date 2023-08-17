package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.Conversores
import pt.ulusofona.deisi.cm2223.g22002160_22006182.R
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao
import java.util.Date


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val TAG = DashboardFragment::class.java.simpleName
    private val conversores = Conversores()

    override fun onStart() {
        super.onStart()


        FilmesRepository.getInstance().getFilmes { result ->
            if (result.isSuccess){
                var filmes = result.getOrDefault(mutableListOf()) as MutableList<Filme>
                var ultimoFilmeAssistido = conversores.ultimoFilmeAssistido(filmes)
                var dataDeVizualizacao= ""
                if(ultimoFilmeAssistido != null) {
                    OpinioesRepository.getInstance().getOpiniaoByFilmeId(ultimoFilmeAssistido.filmeId){result ->
                        val opiniaoTemp = result.getOrNull()
                        if (result.isSuccess){
                            if (opiniaoTemp != null) {
                                dataDeVizualizacao = opiniaoTemp.dataTimeStamp
                            }
                        }
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {


                    if (ultimoFilmeAssistido != null){
                        Glide.with(binding.filmImage).load(ultimoFilmeAssistido.imagemCartaz).into(binding.filmImage)

                        binding.filmTitleDashboard.text = ultimoFilmeAssistido.nome
                        binding.filmDate.text = conversores.timestampToString(dataDeVizualizacao)
                    }
                }
            }
        }
        OpinioesRepository.getInstance().getOpinioes { result ->
            if (result.isSuccess){
                var opinioes = result.getOrDefault(mutableListOf()) as MutableList<Opiniao>
                CoroutineScope(Dispatchers.Main).launch {
                    binding.totalAvaliacoes.setText(opinioes.size.toString())
                    var media = conversores.mediaAvaliacoes(opinioes)
                    binding.mediaAvaliacoes.setText(media.toString())
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.fragment_dashboard, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.dashboard_page_name)
        binding = FragmentDashboardBinding.bind(view)

        binding.apply {

            FilmesRepository.getInstance().getFilmes { result ->
                if (result.isSuccess){
                    var filmes = result.getOrDefault(mutableListOf()) as MutableList<Filme>
                    CoroutineScope(Dispatchers.Main).launch {
                        var teste = conversores.generosMaisVistos(filmes)
                        barChart.barsColor = Color.parseColor("#001F54")
                        barChart.animation.duration = animationsDuration
                        barChart.animate(conversores.generosMaisVistos(filmes))
                    }
                }
            }

        }

        return binding.root
    }

    companion object{
        private const val animationsDuration = 1000L
    }
}
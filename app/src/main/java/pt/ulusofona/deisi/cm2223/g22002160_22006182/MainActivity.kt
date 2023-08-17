package pt.ulusofona.deisi.cm2223.g22002160_22006182

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ActivityMainBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.ItemPopupBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var bindingMic: ItemPopupBinding
    private var conversores = Conversores()
    private lateinit var tituloFilme: String
    private val TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMic = ItemPopupBinding.inflate(layoutInflater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        setContentView(binding.root)
        if (!screenRotated(savedInstanceState)) {
            NavigationManager.goToDashboardFragment(supportFragmentManager)
        }
    }

    override fun onStart() {
        super.onStart()
        setupBottomBarMenu()
        showPopUp()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }

    private fun setupBottomBarMenu() {
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.background = null
        bottomNavigationView.setOnItemSelectedListener {
            onClickNavigationItem(it)
        }
    }

    private fun onClickNavigationItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dashboard ->
                NavigationManager.goToDashboardFragment(
                    supportFragmentManager
                )
            R.id.filmes ->
                NavigationManager.goToFilmesFragment(
                    supportFragmentManager
                )
            R.id.registarFilme ->
                NavigationManager.goToRegisterFragment(
                    supportFragmentManager
                )
            R.id.novidades ->
                NavigationManager.goToNovidadesFragment(
                    supportFragmentManager
                )
        }
        return true
    }

    private fun criarData(dataString: String): Date {
        val partes = dataString.split("/")
        val calendario = Calendar.getInstance()
        calendario.set(partes[0].toInt(), partes[1].toInt() - 1, partes[2].toInt())
        return calendario.time
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // Speech recognition service is ready for user speech input
            bindingMic.movieNameTextView.text = R.string.mic_escutando.toString()
            bindingMic.retryButton.visibility = View.INVISIBLE // Hide Retry button
        }

        override fun onBeginningOfSpeech() {
            // User started speaking
        }

        override fun onRmsChanged(rmsdB: Float) {
            // RMS (Root Mean Square) dB value of the captured audio
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // Called when partial recognition results are available
        }

        override fun onEndOfSpeech() {
            // User finished speaking
            bindingMic.movieNameTextView.text = R.string.mic_processando.toString()

        }

        override fun onError(error: Int) {
            // Speech recognition error occurred
            bindingMic.movieNameTextView.text = R.string.mic_erro.toString()
            bindingMic.retryButton.visibility = View.VISIBLE
        }

        override fun onResults(results: Bundle?) {
            // Speech recognition results are available
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                val spokenText = matches[0] // Get the most likely recognition result
                bindingMic.movieNameTextView.text = spokenText
                tituloFilme = spokenText
            }
            bindingMic.searchButton.visibility = View.VISIBLE
            bindingMic.retryButton.visibility = View.VISIBLE
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // Called when partial recognition results are available
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // Called when a speech recognition event occurs
        }
    }

    private fun startSpeechRecognition() {
        bindingMic.movieNameTextView.text = R.string.mic_escutando.toString()
        bindingMic.searchButton.visibility = View.GONE

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        speechRecognizer.startListening(intent)
    }

    private fun searchFilme(filme: String) {
        val nomeFilme = filme.trim()

        if (nomeFilme.isNotEmpty()) {
            FilmesRepository.getInstance().getFilmes { result ->
                if (result.isSuccess){
                    var filmes = result.getOrDefault(mutableListOf()) as MutableList<Filme>

                    if (filmes.size > 0){
                        filmes.forEach {
                            if (it.nome == nomeFilme){
                                val filmeUi = FilmeUi(
                                    it.filmeId,
                                    it.nome,
                                    it.imagemCartaz,
                                    it.genero,
                                    it.sinopse,
                                    conversores.timestampToString(it.dataLancamento),
                                    it.avaliacaoIMDB,
                                    it.numeroAvaliacoes,
                                    it.linkIMDB
                                )
                                NavigationManager.goToFilmesDetalhes(supportFragmentManager,filmeUi)
                            }
                        }
                    }
                }else{
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@MainActivity, R.string.filme_nao_encontrado, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun showPopUp() {
        binding.micFab.setOnClickListener {
            permissionsBuilder(android.Manifest.permission.RECORD_AUDIO).build().send() { result ->
                if (result.allGranted()) {
                    val dialog = Dialog(this)
                    bindingMic = ItemPopupBinding.inflate(layoutInflater, null, false)

                    val cancelButton: Button = bindingMic.cancelButton
                    cancelButton.setOnClickListener {
                        dialog.dismiss()
                    }

                    val retryButton: Button = bindingMic.retryButton
                    val searchButton: Button = bindingMic.searchButton

                    retryButton.setOnClickListener {
                        bindingMic.movieNameTextView.text = ""
                        startSpeechRecognition()
                    }

                    searchButton.setOnClickListener {
                        searchFilme(tituloFilme)
                        dialog.dismiss()
                    }
                    bindingMic.retryButton.visibility = View.INVISIBLE

                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.applicationContext)
                    speechRecognizer.setRecognitionListener(recognitionListener)

                    startSpeechRecognition()

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(bindingMic.root)
                    dialog.show()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

}
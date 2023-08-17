package pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.*
import pt.ulusofona.deisi.cm2223.g22002160_22006182.adapters.FilmeFotoAdapter
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.CinemasRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FilmesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.FotosRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.repositories.OpinioesRepository
import pt.ulusofona.deisi.cm2223.g22002160_22006182.databinding.FragmentRegisterBinding
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Cinema
import java.util.*

class RegisterFragment : Fragment() {
    private val TAG = RegisterFragment::class.java.simpleName
    private lateinit var binding: FragmentRegisterBinding
    private var filmeSelecionado = 0
    private var conversor = Conversores()
    var cinemas: MutableMap<String, Int> = mutableMapOf()
    private var imageList = mutableListOf<Bitmap>()
    private lateinit var imageAdapter: FilmeFotoAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_register, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.register)
        binding = FragmentRegisterBinding.bind(view)
        if (!ConnectivityUtil.isOnline(requireContext())) {
            binding.noWifi.visibility = View.VISIBLE
            binding.nestedScrollView.visibility = View.GONE
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setUpSlider()
        setUpDatePicker()
        submmitButtonClick()
        complete()
        tituloFilmeButton()
        galeriaButton()
    }

    private fun setUpSlider() {
        binding.slider.valueFrom = 1f
        binding.slider.valueTo = 10f
        binding.slider.value = 5f
        binding.slider.stepSize = 1f

        binding.slider.addOnChangeListener { slider, value, fromUser ->
            binding.sliderLabelBottom.text = DecimalFormat("#.#").format(value)
        }
    }

    private fun setUpDatePicker() {

        binding.dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val tomorrow = calendar.time
            // Criar DatePickerDialog
            val ctw = ContextThemeWrapper(context, R.style.MyDatePickerTheme)
            val datePickerDialog = DatePickerDialog(
                ctw,
                { _, year, month, dayOfMonth ->
                    // Validar se a data selecionada está no passado
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(
                        Calendar.MONTH,
                        month - 1
                    ) // O mês é baseado em 0, portanto, subtraímos 1
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val selectedDate = calendar.time
                    if (selectedDate.before(tomorrow)) {
                        // Formatar a data como string
                        val date = "$year/%02d/$dayOfMonth".format(month + 1)
                        // Atualizar o campo de texto com a data selecionada
                        binding.dateEditText.setText(date)
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            resources.getString(R.string.selecionar_data),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Definir data máxima como a data atual
            calendar.add(Calendar.DAY_OF_MONTH, -1) //para aparecer logo no dia atual
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis

            // Exibir o DatePickerDialog
            datePickerDialog.show()
        }
    }

    fun submmitButtonClick() {
        val submmitButton = binding.submmitButton
        //registar todos os fields do form
        val titulo = binding.inputNomeFilme
        val cinema = binding.inputCinema
        val data = binding.dateEditTextLayout
        val avaliacao = binding.slider
        var observacoes: String
        var cinemaId = 0

        submmitButton.setOnClickListener {
            var allFieldsValid = true

            if (cinemas.containsKey(cinema.text.toString())) {
                cinema.error = null
                cinemaId = cinemas[cinema.text.toString()]!!
            } else {
                binding.layoutCinema.error = resources.getString(R.string.required)
                allFieldsValid = false
            }

            if (titulo.text.isNullOrEmpty()) {
                binding.layoutNome.error = resources.getString(R.string.required)
                allFieldsValid = false
            } else {
                titulo.error = null
            }
            if (data.editText?.text.toString().isEmpty()) {
                data.error = resources.getString(R.string.required)
                allFieldsValid = false
            } else {
                data.error = null
            }

            if (allFieldsValid) {
                var filme: Filme? = null // Inicializa a variável com um valor nulo

                FilmesRepository.getInstance()
                    .getFilmeByTitle(titulo.text.toString()) { result ->
                        if (result.isSuccess) {
                            filme = result.getOrNull() as Filme
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    requireContext(),
                                    result.exceptionOrNull()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        if (binding.inputObservacoes.text.toString().isNullOrEmpty()) {
                            observacoes = ""
                        } else {
                            observacoes = binding.inputObservacoes.text.toString()
                        }

                        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        val timestamp = dateFormat.parse("2023/05/7")?.time.toString()


                        if (filme != null) { // Verifica se o filme não é nulo antes de criar a opinião

                            FilmesRepository.getInstance().existsId(filme!!.filmeId) { result ->
                                if (result.isSuccess) {
                                    if (result.getOrNull() as Boolean) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                requireContext(),
                                                R.string.filmeJaRegistado,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        if (filme!!.dataLancamento == "N/A"){
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(
                                                    context,
                                                    R.string.filme_sem_data,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }else if (filme!!.dataLancamento.toLong() < Date().time){
                                            val opiniao = OpiniaoTable(
                                                avaliacao = avaliacao.value.toInt(),
                                                dataTimestamp = timestamp,
                                                observacao = observacoes,
                                                cinemaId = cinemaId,
                                                filmeId = filme!!.filmeId,
                                            )

                                            if (imageList.size > 0) {
                                                imageList.forEach {
                                                    FotosRepository.getInstance()
                                                        .insertFoto(opiniao.id, conversor.bitmapToString(it)) {}
                                                }
                                            }

                                            FilmesRepository.getInstance().insertFilme(filme!!) {}

                                            OpinioesRepository.getInstance().insertOpiniao(opiniao) {}

                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(
                                                    context,
                                                    resources.getString(R.string.filme_sucesso),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            NavigationManager.goToFilmesFragment(parentFragmentManager)
                                        }else {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(
                                                    context,
                                                    R.string.filme_nao_lancado,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                    }
                                } else {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            requireContext(),
                                            result.exceptionOrNull()?.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                        }
                    }
            }
        }
    }

    private fun complete() {
        CinemasRepository.getInstance().getCinemas { result ->
            if (result.isSuccess) {
                CoroutineScope(Dispatchers.Main).launch {
                    var items = result.getOrDefault(mutableListOf())
                    items.forEach { cinema ->
                        cinemas[cinema.cinemaName] = cinema.cinemaId
                    }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.select_dialog_item_material,
                        cinemas.keys.toList()
                    )
                    binding.inputCinema.threshold = 1
                    binding.inputCinema.setAdapter(adapter)
                    binding.inputNomeFilme.showDropDown()
                }
            } else {
                val exception = result.exceptionOrNull()
            }
        }


        binding.inputNomeFilme.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    return
                }
                FilmesRepository.getInstance()
                    .getFilmesBySearch(s.toString()) { result ->
                        if (result.isSuccess) {
                            var filmesNome = result.getOrNull() as MutableList<String>
                            if (filmesNome.isNotEmpty()) {
                                val adapterFilmes = ArrayAdapter<String>(
                                    requireContext(),
                                    androidx.appcompat.R.layout.select_dialog_item_material,
                                    filmesNome
                                )
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.inputNomeFilme.threshold = 1
                                    binding.inputNomeFilme.setAdapter(adapterFilmes)
                                    binding.inputNomeFilme.showDropDown()
                                }

                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                result.exceptionOrNull()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.inputNomeFilme.setOnItemClickListener { parent, _, position, _ ->
            binding.inputNomeFilme.dismissDropDown()
        }
    }

    private fun galeriaButton() {
        imageAdapter = FilmeFotoAdapter(imageList)
        binding.rvSelectedImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedImages.adapter = imageAdapter

        // Configurar o botão para abrir a galeria
        val btnOpenGallery = binding.btnOpenGallery
        btnOpenGallery.setOnClickListener {

            permissionsBuilder(android.Manifest.permission.CAMERA).build().send() { result ->
                if (result.allGranted()) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, 1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageList.add(imageBitmap)
            if (imageList.isEmpty()) {
                binding.rvSelectedImages.visibility = View.GONE
            } else {
                binding.rvSelectedImages.visibility = View.VISIBLE
            }
            imageAdapter.notifyDataSetChanged()
        }
    }

    private fun tituloFilmeButton() {
        binding.inputNomeFilme.setOnClickListener {
            val titulo = binding.inputNomeFilme.text.toString()
            if (titulo.isNotEmpty()) {
                val builder = AlertDialog.Builder(context)
                CoroutineScope(Dispatchers.IO).launch {
                    FilmesRepository.getInstance().getFilmesBySearch(titulo) { result ->
                        if (result.isSuccess) {
                            val filmesNome = result.getOrNull() as MutableList<String>
                            if (filmesNome.isEmpty()) {
                                builder.setTitle(R.string.filme_nao_encontrado)
                                builder.setMessage(R.string.filme_nao_encontrado_pesquisa)
                                builder.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                            } else {
                                CoroutineScope(Dispatchers.Main).launch {
                                    builder.setTitle(R.string.selecione_filme)
                                    builder.setSingleChoiceItems(
                                        filmesNome.toTypedArray(),
                                        filmeSelecionado
                                    ) { dialog, which ->
                                        filmeSelecionado = which
                                    }

                                    builder.setPositiveButton("OK") { dialog, which ->
                                        // Exiba o nome do cinema no text field
                                        binding.inputNomeFilme.setText(filmesNome[filmeSelecionado])
                                    }

                                    builder.setNegativeButton(
                                        resources.getString(R.string.cancelar),
                                        null
                                    )
                                }
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                val dialog = builder.create()
                                dialog.show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                result.exceptionOrNull()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import android.graphics.BitmapFactory
import android.util.Base64
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Filme
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao
import java.text.NumberFormat

import java.util.*

class Conversores {
    //criar um date no formato dd MMM yyyy da api
    fun criarData(data: String): Date {
        return SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(data)
    }

    fun timestampToString(timestamp: String): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date(timestamp.toLong())
        return dateFormat.format(date)
    }

    fun timestampToString2(timestamp: String): String {
        val date = Date(timestamp.toLong())
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun convertStringToTimestamp(dateString: String): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val date = dateFormat.parse(dateString)
        return date?.time.toString()
    }

    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64: String): Bitmap {
        val byteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun base64ListToBitmapList(base64List: List<String>): MutableList<Bitmap> {
        return base64List.map { base64ToBitmap(it) }.toMutableList()
    }

    fun formatoNumeroAvaliacoes(numero: String): String {
        val numeroAvaliacoes = numero.toInt()
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formatterNumber = when {
            numeroAvaliacoes >= 1000000 -> {
                String.format("%.2fM", numeroAvaliacoes / 1000000.0)
            }
            numeroAvaliacoes >= 1000 -> {
                String.format("%.2fK", numeroAvaliacoes / 1000.0)
            }
            else -> {
                numberFormat.format(numeroAvaliacoes)
            }
        }

        return formatterNumber.replace(",", ".")
    }

    fun ultimoFilmeAssistido(filmes: List<Filme>): Filme? {
        var ultimoFilme = filmes.sortedByDescending { it.dataLancamento.toFloat() }
            .maxByOrNull { it.dataLancamento.toFloat() }
        return ultimoFilme
    }

    fun mediaAvaliacoes(opinioes: List<Opiniao>): Double {
        return opinioes.mapNotNull { it.avaliacao }.average()
    }

    fun generosMaisVistos(filmes: List<Filme>): List<Pair<String, Float>> {
        val generoCount = HashMap<String, Float>()

        for (filme in filmes) {
            if (filme.genero == "N/A") {
                continue
            }
            if (filme.genero in generoCount) {
                generoCount[filme.genero] = generoCount[filme.genero]!! + 1F
            } else {
                generoCount[filme.genero] = 1F
            }


        }
        val list = generoCount.toList()

        // Ordenar a lista com base nos valores em ordem decrescente
        list.sortedByDescending { (_, value) -> value }

        // Criar uma lista de Pairs para armazenar os pares chave-valor ordenados
        val sortedPairs = mutableListOf<Pair<String, Float>>()

        // Adicionar os pares chave-valor ordenados à lista de Pairs
        for ((key, value) in list) {
            sortedPairs.add(Pair(key, value))
        }

        return sortedPairs.take(5)
    }

}
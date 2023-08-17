package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FilmeDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.OpiniaoDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FilmesActions
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.OpinioesActions

class OpinioesActionsRoom(private val dao: OpiniaoDao) : OpinioesActions() {

    override fun getOpinioes(onFinished: (Result<List<Opiniao>>) -> Unit) {
        val opinioes = dao.getAll().map {
            Opiniao(
                it.id,
                it.avaliacao,
                it.dataTimestamp,
                it.observacao,
                it.filmeId,
                it.cinemaId,
            )
        }
        onFinished(Result.success(opinioes))

    }

    override fun insertOpiniao(opiniao: OpiniaoTable, onFinished: () -> Unit) {
        dao.insert(opiniao)
        onFinished()
    }

    override fun getOpiniaoByFilmeId(filmeId: String, onFinished: (Result<Opiniao>) -> Unit) {
        val opiniao = dao.getOpiniaoByFilmeId(filmeId)
        onFinished(Result.success(Opiniao(
            opiniao.id,
            opiniao.avaliacao,
            opiniao.dataTimestamp,
            opiniao.observacao,
            opiniao.filmeId,
            opiniao.cinemaId
        )))
    }

    override fun clearAllOpinioes(onFinished: () -> Unit) {

    }
}
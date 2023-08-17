package pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.Opiniao

abstract class OpinioesActions {
    abstract fun getOpinioes(onFinished: (Result<List<Opiniao>>) -> Unit)
    abstract fun insertOpiniao(opiniao: OpiniaoTable, onFinished: () -> Unit)
    abstract fun clearAllOpinioes(onFinished: () -> Unit)
    abstract fun getOpiniaoByFilmeId(filmeId: String, onFinished: (Result<Opiniao>) -> Unit)
}
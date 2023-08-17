package pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FotosTable

abstract class FotosActions {

    abstract fun getFotos(onFinished: (Result<List<FotosTable>>) -> Unit)
    abstract fun insertFoto(opiniaoId: String, foto: String, onFinished: () -> Unit)
    abstract fun getByOpiniaoId(id: String, onFinished: (Result<List<String>>) -> Unit)
    abstract fun clearAllFotos(onFinished: () -> Unit)
}
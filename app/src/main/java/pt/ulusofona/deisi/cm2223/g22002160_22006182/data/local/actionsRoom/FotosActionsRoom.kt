package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.actionsRoom

import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FotoDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FotosTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.models.actions.FotosActions

class FotosActionsRoom(private val dao: FotoDao): FotosActions() {
    override fun getFotos(onFinished: (Result<List<FotosTable>>) -> Unit) {
        val fotos = dao.getAll()
        onFinished(Result.success(fotos))
    }

    override fun insertFoto(opiniaoId: String, foto: String, onFinished: () -> Unit) {
        val foto = FotosTable(opiniaoId = opiniaoId, foto=foto)
        dao.insert(foto)
        onFinished()
    }

    override fun getByOpiniaoId(id: String, onFinished: (Result<List<String>>) -> Unit) {
        var fotosUrl = mutableListOf<String>()
        val fotos = dao.getByOpiniaoId(id)
        fotos.forEach(){
            fotosUrl.add(it.foto)
        }
        onFinished(Result.success(fotosUrl))
    }

    override fun clearAllFotos(onFinished: () -> Unit) {
        dao.deleteAll()
        onFinished()
    }
}
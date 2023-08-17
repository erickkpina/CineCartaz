package pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.CinemaDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FilmeDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.FotoDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.dao.OpiniaoDao
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.CinemasTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FilmesTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.FotosTable
import pt.ulusofona.deisi.cm2223.g22002160_22006182.data.local.tables.OpiniaoTable

@Database(entities = [FilmesTable::class, CinemasTable::class, OpiniaoTable::class, FotosTable::class], version = 8)
abstract class AppDatabase: RoomDatabase() {

    abstract fun cinemaDao(): CinemaDao
    abstract fun filmeDao(): FilmeDao
    abstract fun opiniaoDao(): OpiniaoDao
    abstract fun fotoDao(): FotoDao

    companion object{

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance as AppDatabase
            }
        }
    }
}
package pt.ulusofona.deisi.cm2223.g22002160_22006182

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.cm2223.g22002160_22006182.controllers.*

object NavigationManager {
    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val transition = fm.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToDashboardFragment(fm: FragmentManager) {
        placeFragment(fm, DashboardFragment())
    }

    fun goToFilmesFragment(fm: FragmentManager) {
        placeFragment(fm, FilmesFragment())
    }

    fun goToRegisterFragment(fm: FragmentManager) {
        placeFragment(fm, RegisterFragment())
    }

    fun goToFilmesDetalhes(fm: FragmentManager, filmeUi: FilmeUi){
        placeFragment(fm, DetalhesFragment.newInstance(filmeUi))
    }

    fun goToMap(fm: FragmentManager){
        placeFragment(fm, MapFragment())
    }

    fun goToNovidadesFragment(fm: FragmentManager){
        placeFragment(fm, ExtraFragment())
    }

    fun goToCinemasDetalhes(fm: FragmentManager, cinemaUi: CinemaUi){
        placeFragment(fm, CinemasDetalhesFragment.newInstance(cinemaUi))
    }
}
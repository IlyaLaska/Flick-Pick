package com.flickPick

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flickPick.databinding.FragmentFilmViewBinding
import com.flickPick.db.FilmDatabase
import com.flickPick.db.FilmDatabaseDao

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FilmViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilmViewFragment : Fragment() {

    private lateinit var profileDao: FilmDatabaseDao
    private lateinit var db: FilmDatabase
    private lateinit var filmViewModel: FilmViewModel
    private lateinit var film: FilmProperty


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_film_view, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDatabaseDao
        val viewModelFactory = FilmViewModelFactory(dataSource, application)
//        filmViewModel = ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)
        filmViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(FilmViewModel::class.java)

        val binding: FragmentFilmViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_film_view, container, false)


        binding.filmViewModel = filmViewModel
        binding.lifecycleOwner = this

        val args = FilmViewFragmentArgs.fromBundle(requireArguments())
        film = args.film
//        val args = FilmViewFragmentArgs by navArgs()
        Log.i("ARG:", args.toString())
        binding.film = film

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.special_films_folders, menu)
        filmViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if(user?.Interested?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[0].isChecked = true
            if(user?.WatchLater?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[1].isChecked = true
            if(user?.CurrentlyWatching?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[2].isChecked = true
            if(user?.Finished?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[3].isChecked = true
            if(user?.Liked?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[4].isChecked = true
            if(user?.Loved?.contains(Pair(film.filmType, film.id.toInt())) == true) menu[5].isChecked = true
        })
//        Log.i("MNU:", menu.size().toString())
//        for (x in 0 until menu.size()) {
//            Log.i("MNU:", menu[x].toString())
//            menu[x].isChecked = filmViewModel.setMenuItemChecked(menu[x].toString(), film.id.toInt())//TODO is it truly passed by reference
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Based on item selected write to db
        Log.i("FilmViewFragment", "|$item|")
        Log.i("FilmViewFragmentID", film.id)
        Log.i("CHK:", item.toString())
        //replace with DB.push specialFilms[item.toString()]
        //TODO show correct checkmark
        item.isChecked = !item.isChecked//invert checkmark TODO implement addORRemove item
        filmViewModel.addOrRemoveSpecialFilm(item.toString(), Pair(film.filmType, film.id.toInt()))
//        when (item.toString()) {
//            "Interested" -> Log.i("FilmViewFragment", "00000000000000000000000000000000000000000000")
//            else -> return super.onOptionsItemSelected(item)
//            //else -> it is a user category
//        }
        return super.onOptionsItemSelected(item)
    }
}

//when user creates a category, update DB list with needed element name
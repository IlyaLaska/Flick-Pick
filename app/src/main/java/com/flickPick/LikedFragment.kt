package com.flickPick

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.flickPick.databinding.FragmentLikedBinding
import com.flickPick.databinding.FragmentWatchLaterBinding
import com.flickPick.db.FilmDatabase

class LikedFragment : Fragment() {
    private lateinit var filmsListViewModel: FilmsListViewModel
    private lateinit var filmViewModel: FilmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Model", "Interested Fragment created")
        val binding: FragmentLikedBinding = DataBindingUtil.inflate(//TODO - change
            inflater, R.layout.fragment_liked, container, false)//TODO - change

        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDatabaseDao
        val viewModelFactory = FilmViewModelFactory(dataSource, application)
//        filmViewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(FilmViewModel::class.java) }!!
        filmViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(FilmViewModel::class.java)
//        filmViewModel = ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)

        filmsListViewModel =  ViewModelProvider(this).get(FilmsListViewModel::class.java) //ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)
//        binding.filmsListViewModel = filmsListViewModel
        Log.i("TST:SPCL_FILMS-FRAGMENT", filmViewModel.specialFilmsNew.toString())
//        filmsListViewModel.getInterested(filmViewModel.specialFilmsNew.interested)//TODO call correct function

//        filmsListViewModel._interestedList.value = null
//        var updated = false
        filmViewModel.user.observe(viewLifecycleOwner, Observer {
            Log.i("OBSER", "OBSER")
            it?.let{
//                Log.i("OBSER", "OBSER-IT NOT NULL")
//                Log.i("OBSER-IntrList", filmsListViewModel.interestedList.value.toString())
                Log.i("OBSER-IT", it.toString())
                Log.i("OBSER-User", filmViewModel.user.value.toString())
                Log.i("OBSER-LIST", filmsListViewModel.filmOrSeriesList.value.toString())
//                if (filmsListViewModel.interestedList.value.isNullOrEmpty()) {
//                filmsListViewModel._interestedList.value =  filmViewModel.user.value.Interested.toSet()
//                filmsListViewModel._interestedList.value = null
                filmsListViewModel.searchNeededFilms(it.Liked.toSet())}
//                    filmsListViewModel.getInterested(it.Interested.toSet())}
//                    updated = true
//            }
        })

        val adapter = FavouritesAdapter(FilmOrSeriesListener {
                film -> filmsListViewModel.onFilmOrSeriesClicked(film)
        })
        binding.filmsList.adapter = adapter

        //TODO decide if I need this stuff
//        binding.filmsList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                filmsListViewModel.loadMoreIfNeeded(filmsList, "Popular Films")
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })

        //TODO replace top rated with regular films
        filmsListViewModel.filmOrSeriesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        filmsListViewModel.navigateToFilmOrSeries.observe(viewLifecycleOwner, Observer {film ->
            film?.let {
                this.findNavController().navigate(
//                    R.id.action_filmsFragment_to_filmViewFragment
                    LikedFragmentDirections.actionLikedFragmentToFilmOrSeriesViewFragment(film)
                )
                filmsListViewModel.onFilmOrSeriesNavigated()
            }
        })
//        setHasOptionsMenu(true)
        return binding.root
    }
}
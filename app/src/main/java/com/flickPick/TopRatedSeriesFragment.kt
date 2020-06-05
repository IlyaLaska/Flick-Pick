package com.flickPick

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.flickPick.databinding.FragmentTopRatedSeriesBinding
import com.flickPick.databinding.FragmentUpcomingFilmsBinding
import kotlinx.android.synthetic.main.fragment_top_rated.*

class SeriesFragment : BackHandlerFragment() {
    private lateinit var filmsListViewModel: FilmsListViewModel
    lateinit var searchView: SearchView
    val thisFragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTopRatedSeriesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_top_rated_series, container, false)

        filmsListViewModel =  ViewModelProvider(this).get(FilmsListViewModel::class.java) //ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)
//        binding.filmsListViewModel = filmsListViewModel
        filmsListViewModel.getNeededFilms("Top Rated Series")

        val adapter = TopRatedAdapter(FilmListener {
                film -> filmsListViewModel.onFilmClicked(film)
        })
        binding.filmsList.adapter = adapter

        binding.filmsList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy != 0) searchView.onActionViewCollapsed()
                filmsListViewModel.loadMoreIfNeeded(filmsList, "Top Rated Series")
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        //TODO replace top rated with regular films
        filmsListViewModel.filmsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        filmsListViewModel.navigateToFilm.observe(viewLifecycleOwner, Observer {film ->
            film?.let {
                this.findNavController().navigate(
//                    R.id.action_filmsFragment_to_filmViewFragment
                    SeriesFragmentDirections.actionSeriesFragmentToFilmViewFragment(film)
                )
                filmsListViewModel.onFilmNavigated()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_bar, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        Log.i("SRCH:", searchItem.toString())
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Enter Title:"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val query1 = query
                Log.i("QUE:_SUBMIT", query1)
                if(query != null && query.isNotBlank()) {
                    Log.i("QUE:_SUBMIT", "TIME TO GO")
                    thisFragment.findNavController().navigate(
                        SeriesFragmentDirections.actionTopRatedSeriesFragmentToSearchResultsListFragment(query)
                    )
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO("Not yet implemented")
                val query1 = newText
                Log.i("QUE:_CHANGE", query1)
                return true
            }
        })
    }
    override fun onBackPressed(): Boolean {
        Log.i("FOC:", "BACK PRESSED")
        return if (!searchView.isIconified) {
            searchView.isIconified = true;//Clear text
            searchView.onActionViewCollapsed()//Collapse view
            true
        } else false
    }
}
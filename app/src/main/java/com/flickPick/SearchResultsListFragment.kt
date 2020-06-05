package com.flickPick

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.flickPick.FilmOrSeriesViewFragmentArgs.Companion.fromBundle
import com.flickPick.databinding.FragmentSearchResultsListBinding
import kotlinx.android.synthetic.main.fragment_top_rated.*

class SearchResultsListFragment : BackHandlerFragment() {
    private lateinit var filmsListViewModel: FilmsListViewModel
    lateinit var searchView: SearchView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSearchResultsListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_results_list, container, false)

        filmsListViewModel =  ViewModelProvider(this).get(FilmsListViewModel::class.java) //ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)
        binding.filmsListViewModel = filmsListViewModel
        val args = SearchResultsListFragmentArgs.fromBundle(requireArguments())
        val searchQuery = args.searchQuery
        filmsListViewModel.getNeededFilms("Search Results", searchQuery)//TODO call to populate list

        val adapter = TopRatedAdapter(FilmListener {
                film -> filmsListViewModel.onFilmClicked(film)
        })
        binding.filmsList.adapter = adapter
//        binding.filmsList.layoutManager = LinearLayoutManager(context)
        binding.filmsList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {//TODO update other fragents
                if(dy != 0) {
                    searchView.onActionViewCollapsed()
                }
                filmsListViewModel.loadMoreIfNeeded(filmsList, "Search Results", searchQuery)//TODO add correct func
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        filmsListViewModel.filmsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        filmsListViewModel.navigateToFilm.observe(viewLifecycleOwner, Observer {film ->
//            var a = ""
//            if(film == null) {
//                a = "NULL"
//            } else a = film.toString()
//            Log.i("WTF", a)
            film?.let {
//                this.findNavController().popBackStack(0x7f0800ec,false)
                this.findNavController().navigate(
                    SearchResultsListFragmentDirections.actionSearchResultsListFragmentToFilmViewFragment(film)
//                    TopRatedFragmentDirections.actionTopRatedFragmentToFilmViewFragment(film)
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
        searchView.queryHint = "Enter Film Title"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("QUE:_SUBMIT", query)
                //TODO reset the list and search again
                if(query != null && query.isNotBlank()) {
                    filmsListViewModel.clearFilmsList()//_searchResultsList.value = null
                    filmsListViewModel.getNeededFilms("Search Results", query)
                }
                return true
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
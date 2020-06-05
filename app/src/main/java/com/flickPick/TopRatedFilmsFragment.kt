package com.flickPick

//import androidx.core.content.ContextCompat.getSystemService
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
import com.flickPick.databinding.FragmentTopRatedBinding
import kotlinx.android.synthetic.main.fragment_top_rated.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TopRatedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopRatedFragment : BackHandlerFragment() {
    private lateinit var filmsListViewModel: FilmsListViewModel
    lateinit var searchView: SearchView
    val thisFragment = this
//    private val filmsListViewModel by lazy { ViewModelProvider(this).get(FilmsListViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTopRatedBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_top_rated, container, false)

        filmsListViewModel =  ViewModelProvider(this).get(FilmsListViewModel::class.java) //ViewModelProvider(this, viewModelFactory).get(FilmViewModel::class.java)
        binding.filmsListViewModel = filmsListViewModel
        filmsListViewModel.getNeededFilms("Top Rated Films")
        val adapter = TopRatedAdapter(FilmListener {
            film -> filmsListViewModel.onFilmClicked(film)
        })
        binding.filmsList.adapter = adapter
//        binding.filmsList.layoutManager = LinearLayoutManager(context)
        binding.filmsList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {//TODO update other fragents
                if(dy != 0) searchView.onActionViewCollapsed()
                filmsListViewModel.loadMoreIfNeeded(filmsList, "Top Rated Films")
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        filmsListViewModel.filmsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        filmsListViewModel.navigateToFilm.observe(viewLifecycleOwner, Observer {film ->
            var a = ""
            if(film == null) {
                a = "NULL"
            } else a = film.toString()
            Log.i("WTF", a)
            film?.let {
//                this.findNavController().popBackStack(0x7f0800ec,false)
                this.findNavController().navigate(
                    TopRatedFragmentDirections.actionTopRatedFragmentToFilmViewFragment(film)
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
                        TopRatedFragmentDirections.actionTopRatedFragmentToSearchResultsListFragment(query)
//                        ,NavOptionsBuilder().popUpTo(parentFragment!!.id, )
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

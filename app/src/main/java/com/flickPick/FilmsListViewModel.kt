package com.flickPick

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class FilmsListViewModel: ViewModel() {
    private val apiKey = "ee3eb8d2fcb78819e8da76ea3cc4c49f"
    private var page = 1

    private val _filmsList = MutableLiveData<List<FilmProperty>>()
    val filmsList: LiveData<List<FilmProperty>>
        get() = _filmsList

    fun clearFilmsList() {_filmsList.value = null}

    private val _filmOrSeriesList = MutableLiveData<Set<FilmOrSeries>>()
    val filmOrSeriesList: LiveData<Set<FilmOrSeries>>
        get() = _filmOrSeriesList

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.i("Model:", "filmsListViewModel created")
//        Log.i("OBSER:", "filmsListViewModel created")
    }
    override fun onCleared() {
        Log.i("Model:", "filmsListViewModel onCleared")
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToFilm = MutableLiveData<FilmProperty>()
    val navigateToFilm
        get() = _navigateToFilm

    fun onFilmClicked(film: FilmProperty) {
        _navigateToFilm.value = film
    }

    fun onFilmNavigated() {
        _navigateToFilm.value = null
    }

    private val _navigateToFilmOrSeries = MutableLiveData<FilmOrSeries>()
    val navigateToFilmOrSeries
        get() = _navigateToFilmOrSeries

    fun onFilmOrSeriesClicked(film: FilmOrSeries) {
        _navigateToFilmOrSeries.value = film
    }

    fun onFilmOrSeriesNavigated() {
        _navigateToFilmOrSeries.value = null
    }

    fun getNeededFilms(funcToUse: String, filmName: String = "") {
        coroutineScope.launch {
            var getAllDeferred = when (funcToUse) {
                "Top Rated Films" -> IMDBAPI.retrofitService.getTopRatedFilmsAsync(apiKey, page)//getTopRatedFilms()
                "Popular Films" -> IMDBAPI.retrofitService.getPopularFilmsAsync(apiKey, page)//getPopularFilms()
                "Currently Playing Films" -> IMDBAPI.retrofitService.getNowPlayingFilmsAsync(apiKey, page)//getNowPlayingFilms()
                "Upcoming Films" -> IMDBAPI.retrofitService.getUpcomingFilmsAsync(apiKey, page)//getUpcomingFilms()
                "Top Rated Series" -> IMDBAPI.retrofitService.getTopRatedSeriesAsync(apiKey, page)//getTopRatedSeries()
                "Popular Series" -> IMDBAPI.retrofitService.getPopularSeriesAsync(apiKey, page)//getPopularSeries()
                "Currently Playing Series" -> IMDBAPI.retrofitService.getOnAirSeriesAsync(apiKey, page)//getNowPlayingSeries()
                "Search Results" -> IMDBAPI.retrofitService.getSearchResultsAsync(filmName, apiKey, page)//getSearchResultsFilms(searchQuery)
                else -> throw IllegalArgumentException("SEEMS LIKE YOU PASSED THE WRONG ARGUMENT TO " +
                        "getNeededFilms, BUDDY")
            }
//                IMDBAPI.retrofitService.getPopularFilmsAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                Log.i("RES:Before", listResult.results.toString())
                Log.i("RES:Before", listResult.results.size.toString())
                Log.i("RES:CALLED?", (funcToUse == "Search Results").toString())
                if (funcToUse == "Search Results")
                    listResult.results = listResult.results.filter{
                        it.filmName != "" || it.filmGenres != "" || it.filmDescription != ""
                                || it.filmOriginalTitle != ""
                    }
                Log.i("RES:AFTER", listResult.results.toString())
                Log.i("RES:AFTER", listResult.results.size.toString())
                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun loadMoreIfNeeded(filmsList: RecyclerView, funcToUse: String, searchQuery: String = ""): Unit {
//        val visibleItemCount: Int = (filmsList.layoutManager as LinearLayoutManager).childCount
        val firstVisibleItemId = (filmsList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val totalItems = (filmsList.adapter as TopRatedAdapter).itemCount
//                if((visibleItemCount + firstVisibleItemId) >= totalItems) {
        if(firstVisibleItemId >= (totalItems - 10)) {//time to load more
//            getTopRatedFilms()
            when(funcToUse) {
                "Top Rated Films" -> getNeededFilms("Top Rated Films")//getTopRatedFilms()
                "Popular Films" -> getNeededFilms("Popular Films")//getPopularFilms()
                "Currently Playing Films" -> getNeededFilms("Currently Playing Films")//getNowPlayingFilms()
                "Upcoming Films" -> getNeededFilms("Upcoming Films")//getUpcomingFilms()
                "Top Rated Series" -> getNeededFilms("Top Rated Series")//getTopRatedSeries()
                "Popular Series" -> getNeededFilms("Popular Series")//getPopularSeries()
                "Currently Playing Series" -> getNeededFilms("Currently Playing Series")//getNowPlayingSeries()
                "Search Results" -> getNeededFilms("Search Results", searchQuery)//getSearchResultsFilms(searchQuery)
            }
        }
    }

    fun searchNeededFilms(neededList: Set<Pair<String, Int>>) {
        _filmOrSeriesList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(neededList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    private fun getAllFilmDeferreds(neededList: Set<Pair<String, Int>>): List<Deferred<FilmOrSeries>> {
        val deferreds = mutableListOf<Deferred<FilmOrSeries>>()
        for (film in neededList) {
            var urlStr = if(film.first/*film.filmType*/ == "Film") "movie/"
            else "tv/"
            urlStr += film.second//film.id
            deferreds.add(IMDBAPI.retrofitService.getMovieOrSeriesByIdAsync(urlStr, apiKey))
        }
        return deferreds.toList()
    }

    //    fun getSearchResultsFilms(filmName: String) {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getSearchResultsAsync(filmName ,apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                Log.i("RES:", listResult.results.toString())
//                listResult.results = listResult.results.filter{it.filmType == "Film" || it.filmType == "Series"}
//                _searchResultsList.value = _searchResultsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//    fun getTopRatedFilms() {
//        Log.i("API:", "get() CALLED")
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getTopRatedFilmsAsync(apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                Log.i("API:", listResult.results.toString())
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//                Log.i("API:_TOP", _filmsList.value.toString())
//                Log.i("API:TOP", filmsList.value.toString())
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getPopularFilms() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getPopularFilmsAsync(apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getNowPlayingFilms() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getNowPlayingFilmsAsync(apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getUpcomingFilms() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getUpcomingFilmsAsync(apiKey, page)
//            Log.i("PAPI:PAGE", page.toString())
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                Log.i("API:", listResult.results.toString())
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//                Log.i("API:_TOP", _filmsList.value.toString())
//                Log.i("API:TOP", filmsList.value.toString())
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//
//    fun getTopRatedSeries() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getTopRatedSeriesAsync(apiKey, page)
//            Log.i("ERR:API:page", page.toString())
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getPopularSeries() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getPopularSeriesAsync(apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getNowPlayingSeries() {
//        coroutineScope.launch {
//            var getAllDeferred = IMDBAPI.retrofitService.getOnAirSeriesAsync(apiKey, page)
//            try {
//                page++
//                var listResult = getAllDeferred.await()
//                _filmsList.value = _filmsList.value?.plus(listResult.results) ?: listResult.results
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getWatchLater(watchLaterList: Set<Pair<String, Int>>) {
//        _filmOrSeriesList.value = null
//        coroutineScope.launch {
//            var getAllDeferred = getAllFilmDeferreds(watchLaterList)
//            try {
//                var listResult = getAllDeferred.awaitAll()
//                Log.i("API:A", listResult.toString())
////                val a = listResult.map { it.results }
//                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getCurrentlyWatching(currentlyWatchingList: Set<Pair<String, Int>>) {
//        _filmOrSeriesList.value = null
//        coroutineScope.launch {
//            var getAllDeferred = getAllFilmDeferreds(currentlyWatchingList)
//            try {
//                var listResult = getAllDeferred.awaitAll()
//                Log.i("API:A", listResult.toString())
////                val a = listResult.map { it.results }
//                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getFinished(finishedList: Set<Pair<String, Int>>) {
//        _filmOrSeriesList.value = null
//        coroutineScope.launch {
//            var getAllDeferred = getAllFilmDeferreds(finishedList)
//            try {
//                var listResult = getAllDeferred.awaitAll()
//                Log.i("API:A", listResult.toString())
////                val a = listResult.map { it.results }
//                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getLiked(likedList: Set<Pair<String, Int>>) {
//        _filmOrSeriesList.value = null
//        coroutineScope.launch {
//            var getAllDeferred = getAllFilmDeferreds(likedList)
//            try {
//                var listResult = getAllDeferred.awaitAll()
//                Log.i("API:A", listResult.toString())
////                val a = listResult.map { it.results }
//                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//
//    fun getLoved(lovedList: Set<Pair<String, Int>>) {
//        _filmOrSeriesList.value = null
//        coroutineScope.launch {
//            var getAllDeferred = getAllFilmDeferreds(lovedList)
//            try {
//                var listResult = getAllDeferred.awaitAll()
//                Log.i("API:A", listResult.toString())
////                val a = listResult.map { it.results }
//                _filmOrSeriesList.value = _filmOrSeriesList.value?.plus(listResult.toSet()) ?: listResult.toSet()
//            } catch (t: Throwable) {
//                Log.i("ERR:API:", t.message!!)
//            }
//        }
//    }
//    private fun getIMDBFilms() {
//        Log.i("API:", "get() CALLED")
////        _topRatedFilmsListNew.value = listOf(FilmProperty(filmName = "AWAITING", f))
//        IMDBAPI.retrofitService.getAll(apiKey).enqueue(object: Callback<Json> {
//            override fun onFailure(call: Call<Json>, t: Throwable) {
//                Log.i("API:", t.message!!)
////                _topRatedFilmsListNew.value = "Fail: " + t.message
//            }
//
//            override fun onResponse(call: Call<Json>, response: Response<Json>) {
//                Log.i("API:", response.body()?.results.toString())
//                _topRatedFilmsListNew.value = response.body()?.results
//            }
//        })
//    }

//    private fun getIMDBFilms() {
//        Log.i("API:", "get() CALLED")
//        _response.value = "Awaiting data..."
//        IMDBAPI.retrofitService.getAll(apiKey).enqueue(object: Callback<Json> {
//            override fun onFailure(call: Call<Json>, t: Throwable) {
//                Log.i("API:", t.message!!)
//                _response.value = "Fail: " + t.message
//            }
//
//            override fun onResponse(call: Call<Json>, response: Response<Json>) {
//                Log.i("API:", response.body()?.results.toString())
//                _response.value = "RETRIEVED: ${response.body()?.results}"
//            }
//        })
//    }

    //    private val _popularFilmsListNew = MutableLiveData<List<FilmProperty>>()
//    val popularFilmsListNew: LiveData<List<FilmProperty>>
//        get() = _popularFilmsListNew
//
//    private val _nowPlayingFilmsListNew = MutableLiveData<List<FilmProperty>>()
//    val nowPlayingFilmsListNew: LiveData<List<FilmProperty>>
//        get() = _nowPlayingFilmsListNew
//
//    private val _upcomingFilmsListNew = MutableLiveData<List<FilmProperty>>()
//    val upcomingFilmsListNew: LiveData<List<FilmProperty>>
//        get() = _upcomingFilmsListNew
//
//    private val _topRatedSeriesListNew = MutableLiveData<List<FilmProperty>>()
//    val topRatedSeriesListNew: LiveData<List<FilmProperty>>
//        get() = _topRatedSeriesListNew
//
//    private val _popularSeriesListNew = MutableLiveData<List<FilmProperty>>()
//    val popularSeriesListNew: LiveData<List<FilmProperty>>
//        get() = _popularSeriesListNew
//
//    private val _nowPlayingSeriesListNew = MutableLiveData<List<FilmProperty>>()
//    val nowPlayingSeriesListNew: LiveData<List<FilmProperty>>
//        get() = _nowPlayingSeriesListNew

//    private val _watchLaterList = MutableLiveData<Set<FilmOrSeries>>()
//    val watchLaterList: LiveData<Set<FilmOrSeries>>
//        get() = _watchLaterList
//
//    private val _currentlyWatchingList = MutableLiveData<Set<FilmOrSeries>>()
//    val currentlyWatchingList: LiveData<Set<FilmOrSeries>>
//        get() = _currentlyWatchingList
//
//    private val _finishedList = MutableLiveData<Set<FilmOrSeries>>()
//    val finishedList: LiveData<Set<FilmOrSeries>>
//        get() = _finishedList
//
//    private val _likedList = MutableLiveData<Set<FilmOrSeries>>()
//    val likedList: LiveData<Set<FilmOrSeries>>
//        get() = _likedList
//
//    private val _lovedList = MutableLiveData<Set<FilmOrSeries>>()
//    val lovedList: LiveData<Set<FilmOrSeries>>
//        get() = _lovedList

//    private val _response = MutableLiveData<String>()
//    val response: LiveData<String>
//        get() = _response
    //    val _searchResultsList = MutableLiveData<List<FilmProperty>>()
//    val searchResultsList: LiveData<List<FilmProperty>>
//        get() = _searchResultsList
}

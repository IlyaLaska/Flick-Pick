package com.flickPick

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*


class FilmsListViewModel: ViewModel() {
    //TODO decide when to make request for more films - maybe get a 1000 for now (then - 100 at a time)
//    var topRatedFilmsList = MutableLiveData<MutableList<Map<String, String>>>()
//    var filmsList = MutableLiveData<MutableList<Map<String, String>>>()
    private val apiKey = "ee3eb8d2fcb78819e8da76ea3cc4c49f"
    private var page = 1

    val test = listOf(Pair("Film", 278), Pair("Series", 278))

    private val _topRatedFilmsListNew = MutableLiveData<List<FilmProperty>>()
    val topRatedFilmsListNew: LiveData<List<FilmProperty>>
        get() = _topRatedFilmsListNew

    private val _popularFilmsListNew = MutableLiveData<List<FilmProperty>>()
    val popularFilmsListNew: LiveData<List<FilmProperty>>
        get() = _popularFilmsListNew

    private val _nowPlayingFilmsListNew = MutableLiveData<List<FilmProperty>>()
    val nowPlayingFilmsListNew: LiveData<List<FilmProperty>>
        get() = _nowPlayingFilmsListNew

    private val _upcomingFilmsListNew = MutableLiveData<List<FilmProperty>>()
    val upcomingFilmsListNew: LiveData<List<FilmProperty>>
        get() = _upcomingFilmsListNew

    private val _topRatedSeriesListNew = MutableLiveData<List<FilmProperty>>()
    val topRatedSeriesListNew: LiveData<List<FilmProperty>>
        get() = _topRatedSeriesListNew

    private val _popularSeriesListNew = MutableLiveData<List<FilmProperty>>()
    val popularSeriesListNew: LiveData<List<FilmProperty>>
        get() = _popularSeriesListNew

    private val _nowPlayingSeriesListNew = MutableLiveData<List<FilmProperty>>()
    val nowPlayingSeriesListNew: LiveData<List<FilmProperty>>
        get() = _nowPlayingSeriesListNew

    val _searchResultsList = MutableLiveData<List<FilmProperty>>()
    val searchResultsList: LiveData<List<FilmProperty>>
        get() = _searchResultsList

    //FAVOURITES VALUES TODO Maybe init lists with values from DB
    private val _interestedList = MutableLiveData<Set<FilmOrSeries>>()
    val interestedList: LiveData<Set<FilmOrSeries>>
        get() = _interestedList

    private val _watchLaterList = MutableLiveData<Set<FilmOrSeries>>()
    val watchLaterList: LiveData<Set<FilmOrSeries>>
        get() = _watchLaterList

    private val _currentlyWatchingList = MutableLiveData<Set<FilmOrSeries>>()
    val currentlyWatchingList: LiveData<Set<FilmOrSeries>>
        get() = _currentlyWatchingList

    private val _finishedList = MutableLiveData<Set<FilmOrSeries>>()
    val finishedList: LiveData<Set<FilmOrSeries>>
        get() = _finishedList

    private val _likedList = MutableLiveData<Set<FilmOrSeries>>()
    val likedList: LiveData<Set<FilmOrSeries>>
        get() = _likedList

    private val _lovedList = MutableLiveData<Set<FilmOrSeries>>()
    val lovedList: LiveData<Set<FilmOrSeries>>
        get() = _lovedList

//    private val _response = MutableLiveData<String>()
//    val response: LiveData<String>
//        get() = _response

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.i("Model:", "filmsListViewModel created")
        Log.i("OBSER:", "filmsListViewModel created")
    }
    override fun onCleared() {
        Log.i("Model:", "filmsListViewModel onCleared")
        super.onCleared()
        viewModelJob.cancel()
    }

//    private val user = MutableLiveData<UserProfile?>()
//
//    private var specialFilms: MutableLiveData<MutableMap<String, ArrayList<Int>>> = MutableLiveData(mutableMapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList()))

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

    fun getTopRatedFilms() {
        Log.i("API:", "get() CALLED")
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getTopRatedFilmsAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                Log.i("API:", listResult.results.toString())
                _topRatedFilmsListNew.value = _topRatedFilmsListNew.value?.plus(listResult.results) ?: listResult.results
                Log.i("API:_TOP", _topRatedFilmsListNew.value.toString())
                Log.i("API:TOP", topRatedFilmsListNew.value.toString())
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getPopularFilms() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getPopularFilmsAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                _popularFilmsListNew.value = _popularFilmsListNew.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getNowPlayingFilms() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getNowPlayingFilmsAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                _nowPlayingFilmsListNew.value = _nowPlayingFilmsListNew.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getUpcomingFilms() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getUpcomingFilmsAsync(apiKey, page)
            Log.i("PAPI:PAGE", page.toString())
            try {
                page++
                var listResult = getAllDeferred.await()
                Log.i("API:", listResult.results.toString())
                _upcomingFilmsListNew.value = _upcomingFilmsListNew.value?.plus(listResult.results) ?: listResult.results
                Log.i("API:_TOP", _upcomingFilmsListNew.value.toString())
                Log.i("API:TOP", upcomingFilmsListNew.value.toString())
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getSearchResultsFilms(filmName: String) {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getSearchResultsAsync(filmName ,apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                Log.i("RES:", listResult.results.toString())
                listResult.results = listResult.results.filter{it.filmType == "Film" || it.filmType == "Series"}
                _searchResultsList.value = _searchResultsList.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getTopRatedSeries() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getTopRatedSeriesAsync(apiKey, page)
            Log.i("ERR:API:page", page.toString())
            try {
                page++
                var listResult = getAllDeferred.await()
                _topRatedSeriesListNew.value = _topRatedSeriesListNew.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getPopularSeries() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getPopularSeriesAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                _popularSeriesListNew.value = _popularSeriesListNew.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getNowPlayingSeries() {
        coroutineScope.launch {
            var getAllDeferred = IMDBAPI.retrofitService.getOnAirSeriesAsync(apiKey, page)
            try {
                page++
                var listResult = getAllDeferred.await()
                _nowPlayingSeriesListNew.value = _nowPlayingSeriesListNew.value?.plus(listResult.results) ?: listResult.results
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getInterested(interestedList: Set<Pair<String, Int>>) {
        _interestedList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(interestedList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _interestedList.value = _interestedList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getWatchLater(watchLaterList: Set<Pair<String, Int>>) {
        _watchLaterList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(watchLaterList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _watchLaterList.value = _watchLaterList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getCurrentlyWatching(currentlyWatchingList: Set<Pair<String, Int>>) {
        _currentlyWatchingList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(currentlyWatchingList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _currentlyWatchingList.value = _currentlyWatchingList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getFinished(finishedList: Set<Pair<String, Int>>) {
        _finishedList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(finishedList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _finishedList.value = _finishedList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getLiked(likedList: Set<Pair<String, Int>>) {
        _likedList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(likedList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _likedList.value = _likedList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getLoved(lovedList: Set<Pair<String, Int>>) {
        _lovedList.value = null
        coroutineScope.launch {
            var getAllDeferred = getAllFilmDeferreds(lovedList)
            try {
                var listResult = getAllDeferred.awaitAll()
                Log.i("API:A", listResult.toString())
//                val a = listResult.map { it.results }
                _lovedList.value = _lovedList.value?.plus(listResult.toSet()) ?: listResult.toSet()// TODO
            } catch (t: Throwable) {
                Log.i("ERR:API:", t.message!!)
            }
        }
    }

    fun getAllFilmDeferreds(interestedList: Set<Pair<String, Int>>): List<Deferred<FilmOrSeries>> {
        val deferreds = mutableListOf<Deferred<FilmOrSeries>>()
        for (film in interestedList) {
            var urlStr = if(film.first/*film.filmType*/ == "Film") "movie/"
            else "tv/"
            urlStr += film.second//film.id
            deferreds.add(IMDBAPI.retrofitService.getMovieOrSeriesByIdAsync(urlStr, apiKey))
        }
        return deferreds.toList()
    }

    fun loadMoreIfNeeded(filmsList: RecyclerView, funcToUse: String, searchQuery: String = ""): Unit {
//        val visibleItemCount: Int = (filmsList.layoutManager as LinearLayoutManager).childCount
        val firstVisibleItemId = (filmsList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val totalItems = (filmsList.adapter as TopRatedAdapter).itemCount
//                if((visibleItemCount + firstVisibleItemId) >= totalItems) {
        if(firstVisibleItemId >= (totalItems - 10)) {//time to load more
//            getTopRatedFilms()
            when(funcToUse) {
                "Top Rated Films" -> getTopRatedFilms()
                "Popular Films" -> getPopularFilms()
                "Currently Playing Films" -> getNowPlayingFilms()
                "Upcoming Films" -> getUpcomingFilms()
                "Top Rated Series" -> getTopRatedSeries()
                "Popular Series" -> getPopularSeries()
                "Currently Playing Series" -> getNowPlayingSeries()
                "Search Results" -> getSearchResultsFilms(searchQuery)
            }
        }
    }


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

}

//TODO decide where to get username and how to store it and update username var
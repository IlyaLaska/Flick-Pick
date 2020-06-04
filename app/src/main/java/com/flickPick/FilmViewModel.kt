package com.flickPick

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.flickPick.db.UserProfile
import com.flickPick.db.FilmDatabaseDao
import android.util.Log
import kotlinx.coroutines.*

class FilmViewModel (val database: FilmDatabaseDao, application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()

    override fun onCleared() {
        Log.i("OBSER:VM", "FILMviewModel onCleared")
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val user = MutableLiveData<UserProfile?>()
    private val username = "User"

//    private var user = UserProfile()

//    private var specialFilms: Map<String, List<Int>> = mapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList())

    //NOT MUTABLELIVEDATA FOR NOW
//    private var specialFilms: MutableMap<String, ArrayList<Int>> = mutableMapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList())

    data class SpecialFilmsClass (
        val interested: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val watchLater: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val currentlyWatching: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val finished: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val liked: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val loved: MutableSet<Pair<String, Int>> = mutableSetOf()
    )

    var specialFilmsNew = SpecialFilmsClass()

//    private var specialFilms: MutableLiveData<MutableMap<String, ArrayList<Int>>> = MutableLiveData(mutableMapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList()))



//    private var interestedFilms = database.getCertainFilmsByName("Interested", user.value!!.Name)
//    private var watchLaterFilms = database.getCertainFilmsByName("WatchLater", user.value!!.Name)
//    private var currentlyWatchingFilms = database.getCertainFilmsByName("CurrentlyWatching", user.value!!.Name)
//    private var finishedFilms = database.getCertainFilmsByName("Finished", user.value!!.Name)
//    private var likedFilms = database.getCertainFilmsByName("Liked", user.value!!.Name)
//    private var lovedFilms = database.getCertainFilmsByName("Loved", user.value!!.Name)

//    private var specialFilms: MutableMap<String, ArrayList<Int>> = mutableMapOf(
//        "Interested" to ArrayList(database.getCertainFilmsByName("Interested", user.value!!.Name).value!!),
//        "Watch Later" to ArrayList(database.getCertainFilmsByName("WatchLater", user.value!!.Name).value!!),
//            "Currently Watching" to ArrayList(database.getCertainFilmsByName("CurrentlyWatching", user.value!!.Name).value!!),
//        "Finished" to ArrayList(database.getCertainFilmsByName("Finished", user.value!!.Name).value!!),
//        "Liked" to ArrayList(database.getCertainFilmsByName("Liked", user.value!!.Name).value!!),
//        "Loved" to ArrayList(database.getCertainFilmsByName("Loved", user.value!!.Name).value!!))

//    private var specialFilms = database.getSpecialFilmsByName(user.value!!.name)

    init {
        Log.i("OBSER:FILM", "FILMviewModel created")
        initUser()
        initSpecialFilms()
    }
    private fun initUser() {
        uiScope.launch {
            Log.i("AB:", "BEFORE: ${user.value}")
            val a = getAllFromDB()
            user.value = getUserFromDB()
//            user.value = UserProfile(Name=username, Interested=listOf(Pair("Film", 278)), WatchLater=listOf(Pair("Film", 278)),
//                CurrentlyWatching=listOf(Pair("Film", 278)), Finished=listOf(Pair("Film", 278)), Liked=listOf(Pair("Film", 278)), Loved=listOf(Pair("Film", 278)))
            Log.i("DB", a.toString())
            Log.i("AB:", "After: ${user.value}")
        }
    }


    private suspend fun getAllFromDB(): List<UserProfile> {
        return withContext(Dispatchers.IO) {
//            database.clearTable()
//            database.insert(UserProfile(Name=username, Interested=listOf(Pair("Film", 278)), WatchLater=listOf(Pair("Film", 278)),
//                CurrentlyWatching=listOf(Pair("Film", 278)), Finished=listOf(Pair("Film", 278)), Liked=listOf(Pair("Film", 278)), Loved=listOf(Pair("Film", 278))))
            database.getAllTable()
//            listOf(UserProfile(Name=username, Interested=listOf(Pair("Film", 278)), WatchLater=listOf(Pair("Film", 278)),
//                CurrentlyWatching=listOf(Pair("Film", 278)), Finished=listOf(Pair("Film", 278)), Liked=listOf(Pair("Film", 278)), Loved=listOf(Pair("Film", 278))))
        }
    }

    private suspend fun getUserFromDB():UserProfile {
        return withContext(Dispatchers.IO) {
//            database.update(UserProfile(Id = 1, Name="User", Interested=listOf(), WatchLater=listOf(),
//                CurrentlyWatching=listOf(), Finished=listOf(), Liked=listOf(), Loved=listOf()))
            database.getUserByName(username)
        }
    }

    private fun initSpecialFilms() {
        uiScope.launch {
            specialFilmsNew = getSpecialFilmsFromDBNew()
            Log.i("TST:SPCL_FILMS-VEWMODEL", specialFilmsNew.toString())
            Log.i("AB:FILMS", specialFilmsNew.toString())
//            specialFilms.value = getSpecialFilmsFromDB()
        }
    }
    //TODO turn into livedata (mutable?)
    //TODO REPLACE COUROUTINE - just use user I already have
    private suspend fun getSpecialFilmsFromDBNew(): SpecialFilmsClass {
        return withContext(Dispatchers.IO) {
            val gotUser = database.getUserByName(username)
//            val gotUser = user.value ?: UserProfile(Name=username, Interested=listOf(Pair("Film", 278)), WatchLater=listOf(Pair("Film", 278)),
//                CurrentlyWatching=listOf(Pair("Film", 278)), Finished=listOf(Pair("Film", 278)), Liked=listOf(Pair("Film", 278)), Loved=listOf(Pair("Film", 278)))
//            val test = database.getCertainFilmsTest(username)
//            val ttt = database.getCertainFilmTTT(username)
            val interested = gotUser.Interested.toMutableSet() //(database.getCertainFilmsByName("Interested", username).toMutableSet())
            val watchLater = gotUser.WatchLater.toMutableSet() //(database.getCertainFilmsByName("Watch Later", username).toMutableSet())
            val currentlyWatching = gotUser.CurrentlyWatching.toMutableSet() //(database.getCertainFilmsByName("Currently Watching", username).toMutableSet())
            val finished = gotUser.Finished.toMutableSet() //(database.getCertainFilmsByName("Finished", username).toMutableSet())
            val liked = gotUser.Liked.toMutableSet() //(database.getCertainFilmsByName("Liked", username).toMutableSet())
            val loved = gotUser.Loved.toMutableSet() //(database.getCertainFilmsByName("Loved", username).toMutableSet())
//            Log.i("AB:TEST", test.toString())
//            Log.i("AB:TTT", ttt.toString())
            Log.i("AB:GotUser", gotUser.toString())
            return@withContext SpecialFilmsClass(interested = interested, watchLater = watchLater,
            currentlyWatching = currentlyWatching, finished = finished, liked = liked, loved = loved)
        }
    }

    //NOT MUTABLE LIVEDATA YET
//    private suspend fun getSpecialFilmsFromDB(): MutableMap<String, ArrayList<Int>> {
//        return withContext(Dispatchers.IO) {
//            val interested = ArrayList(database.getCertainFilmsByName("Interested", username))
//            val watchLater = ArrayList(database.getCertainFilmsByName("Watch Later", username))
//            val currentlyWatching = ArrayList(database.getCertainFilmsByName("Currently Watching", username))
//            val finished = ArrayList(database.getCertainFilmsByName("Finished", username))
//            val liked = ArrayList(database.getCertainFilmsByName("Liked", username))
//            val loved = ArrayList(database.getCertainFilmsByName("Loved", username))
//            return@withContext mutableMapOf("Interested" to interested,
//                "Watch Later" to watchLater, "Currently Watching" to currentlyWatching,
//                "Finished" to finished, "Liked" to liked, "Loved" to loved)
//        }
//    }

//    fun setMenuItemChecked(menuItem: String, filmId: Int): Boolean {
//        Log.i("MNU:CHK:SPECIAL_FILMS", specialFilmsNew.toString())
//        val test = when (menuItem) {
//            "Interested" -> specialFilmsNew.interested.contains(filmId)
//            "Watch Later" -> specialFilmsNew.watchLater.contains(filmId)
//            "Currently Watching" -> specialFilmsNew.currentlyWatching.contains(filmId)
//            "Finished" -> specialFilmsNew.finished.contains(filmId)
//            "Liked" -> specialFilmsNew.liked.contains(filmId)
//            "Loved" -> specialFilmsNew.loved.contains(filmId)
//            else -> {
//                Log.i("ERR:", "Unknown menu item: $menuItem")
//                //throw Exception("Unknown Favourite Category")
//                return false
//            }
//        }
//        Log.i("MNU:CHK:ITEM", menuItem)
//        Log.i("MNU:CHK:ID", filmId.toString())
//        Log.i("MNU:CHK:", test.toString())
//        return test
//    }

    fun addOrRemoveSpecialFilm(category: String, filmPair: Pair<String, Int>) {
        Log.i("OBSER", "ADDorREMFunc")
        Log.i("OBSER: USER IN VM BEFOR", user.value.toString())
        Log.i("AB:SPCL", filmPair.toString())
       val addAttemptRes = when (category) {
            "Interested" -> specialFilmsNew.interested.add(filmPair)
            "Watch Later" -> specialFilmsNew.watchLater.add(filmPair)
            "Currently Watching" -> specialFilmsNew.currentlyWatching.add(filmPair)
            "Finished" -> specialFilmsNew.finished.add(filmPair)
            "Liked" -> specialFilmsNew.liked.add(filmPair)
            "Loved" -> specialFilmsNew.loved.add(filmPair)
           else -> {
               Log.i("ERR:", "Unknown category: ${category}")
               //throw Exception("Unknown Favourite Category")
               return
           }
       }
        //        specialFilms.value!![category]?.add(id)
//        specialFilms.value!![category]!!.add(id)//Add locally
        Log.i("AB:COULD_ADD", addAttemptRes.toString())
        if(addAttemptRes) {//element successfully added
            when(category) {
                "Interested" -> {
                    user.value!!.Interested += filmPair
//                    user.value = user.value
                }
                "Watch Later" -> user.value!!.WatchLater += filmPair
                "Currently Watching" -> user.value!!.CurrentlyWatching += filmPair
                "Finished" -> user.value!!.Finished += filmPair
                "Liked" -> user.value!!.Liked += filmPair
                "Loved" -> user.value!!.Loved += filmPair
            }
            uiScope.launch {
                update(user.value!!)
                Log.i("OBSER:AB:ADD", "${user.value}")
            }
        } else {//add failed - item is there so we have to remove it
            when(category) {
                "Interested" -> {
                    user.value!!.Interested -= filmPair
//                    user.value = user.value
                    specialFilmsNew.interested.remove(filmPair)
                }
                "Watch Later" -> {
                    user.value!!.WatchLater -= filmPair
                    specialFilmsNew.watchLater.remove(filmPair)
                }
                "Currently Watching" -> {
                    user.value!!.CurrentlyWatching -= filmPair
                    specialFilmsNew.currentlyWatching.remove(filmPair)
                }
                "Finished" -> {
                    user.value!!.Finished -= filmPair
                    specialFilmsNew.finished.remove(filmPair)
                }
                "Liked" -> {
                    user.value!!.Liked -= filmPair
                    specialFilmsNew.liked.remove(filmPair)
                }
                "Loved" -> {
                    user.value!!.Loved -= filmPair
                    specialFilmsNew.loved.remove(filmPair)
                }
            }
            uiScope.launch {
                update(user.value!!)
                Log.i("OBSER:AB:REM", "${user.value}")
            }
        }
        Log.i("OBSER: USER IN VM AFTER", user.value.toString())
    }

//    fun addSpecialFilm(category: String, id: Int) {
//        specialFilms.value!![category]?.add(id)
////        specialFilms.value!![category]!!.add(id)//Add locally
//        when(category) {
//            "Interested" -> user.value!!.Interested += id
//            "Watch Later" -> user.value!!.WatchLater += id
//            "Currently Watching" -> user.value!!.CurrentlyWatching += id
//            "Finished" -> user.value!!.Finished += id
//            "Liked" -> user.value!!.Liked += id
//            "Loved" -> user.value!!.Loved += id
//        }
//        uiScope.launch {
//            update(user.value!!)
//            Log.i("AB:", "${user.value}")
//        }
//    }

    private suspend fun update(user: UserProfile) {
        withContext(Dispatchers.IO) {
            Log.i("AB:ADD", user.toString())
            database.update(user)
//            database.test(user.Interested)
        }
    }
}

//TODO decide where to get username and how to store it and update username var
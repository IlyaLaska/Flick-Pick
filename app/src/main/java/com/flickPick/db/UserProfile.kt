package com.flickPick.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "userProfilesTable")
data class UserProfile (
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0,
    var Name: String = "",
//    var Interested: Set<Int> = setOf<Int>(),
//    var WatchLater: Set<Int> = setOf<Int>(),
//    var CurrentlyWatching: Set<Int> = setOf<Int>(),
//    var Finished: Set<Int> = setOf<Int>(),
//    var Liked: Set<Int> = setOf<Int>(),
//    var Loved: Set<Int> = setOf<Int>()
    var Interested: List<Pair<String, Int>> = listOf(),
    var WatchLater: List<Pair<String, Int>> = listOf(),
    var CurrentlyWatching: List<Pair<String, Int>> = listOf(),
    var Finished: List<Pair<String, Int>> = listOf(),
    var Liked: List<Pair<String, Int>> = listOf(),
    var Loved: List<Pair<String, Int>> = listOf()
//    var specialFilms: ArrayList<Int> = ArrayList()
//    @TypeConverters(SpecialFilmsConverter::class)
//    var specialFilms: Map<String, List<Int>> = mapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList())
//    var specialFilms: MutableMap<String, ArrayList<Int>> = mutableMapOf("Interested" to ArrayList(),
//        "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
//        "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList())
)
package com.flickPick.db

import android.util.Log
import androidx.room.TypeConverter
import java.lang.StringBuilder

class SpecialFilmsConverter {
    @TypeConverter
    fun fromFilmList(films: List<Pair<String, Int>>): String {
//        val list =
        return films.map{(name, id) -> "${name}=${id}"}.joinToString(",")
    }
    @TypeConverter
    fun toFilmList(filmStr: String): List<Pair<String, Int>> {
        val filmArray = if (filmStr == "") listOf() else filmStr.split(",")
        val list = mutableListOf<Pair<String, Int>>()
        for (film in filmArray) {
            val pairArr = film.split("=")
            list.add(Pair(pairArr[0], pairArr[1].toInt()))
        }
        return list
//        if(filmStr.isBlank()) return listOf()
//        val filmArr =  filmStr.split(",")
//        Log.i("CON:", filmArr.toString())
//        Log.i("CON:FIRST", filmArr[0])
//        Log.i("CON:NOT-EMPTY", filmArr.isNotEmpty().toString())
//        Log.i("CON:SIZE", filmArr.size.toString())
//        return if(filmArr.isNotEmpty()) filmArr.map {it.toInt()}
//        else listOf()
    }
//    @TypeConverter
//    fun fromSpecialFilms(specialFilms: Map<String, List<Int>>): String {
//        val mapAsString = StringBuilder("")
//        for (key in specialFilms.keys) {
//            mapAsString.append(key + "=" + specialFilms[key]!!.joinToString(",") + ";")
//        }
//        mapAsString.delete(mapAsString.length-1, mapAsString.length).append("")
//        return mapAsString.toString()
//    }
////        return specialFilms.toString()//stream().collect(Collectors.joining(","))
////    }
//
//    @TypeConverter
//    fun toSpecialFilms(data: String): Map<String, List<Int>> {
//        return (data.split(";").associate {
//            val (left, right) = it.split("=")
//            left to ArrayList(right.split(",").map{ x -> x.toInt()})
//        }).toMutableMap()
////        return linkedMapOf("Interested" to ArrayList(),
////            "Watch Later" to ArrayList(), "Currently Watching" to ArrayList(),
////            "Finished" to ArrayList(), "Liked" to ArrayList(), "Loved" to ArrayList())//Arrays.asList(data.split(",").toTypedArray())
//    }
}
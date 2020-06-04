package com.flickPick

import android.os.Parcelable
import androidx.annotation.Nullable
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import kotlinx.android.parcel.Parcelize

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class DateAnnotation

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class GenreAnnotation

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToEmptyStrAnnotation

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class TypeAnnotation

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class GenreAGAINAnnotation

//TODO make sure I do not have more than 1 Int and Double field
//TODO ADAPTER for simple transformations?
@Parcelize
data class FilmProperty (
    @Json(name="poster_path") @NullToEmptyStrAnnotation var filmImagePath: String? = "",
    @Json(name="id") val id: String,
    @Json(name="genre_ids") @GenreAnnotation val filmGenres: String = "",
    @Json(name="title") val filmName: String = "",
    @Json(name="vote_average") val filmRating: String = "",
    @Json(name="overview") val filmDescription: String = "",
    @Json(name="release_date") @DateAnnotation val filmReleaseDate: String = "",
    @Json(name="first_air_date") @DateAnnotation val seriesReleaseDate: String = "",
    @Json(name="original_language") val filmOriginalLanguage: String = "",
    @Json(name="original_title") val filmOriginalTitle: String = "",
    @Json(name="name") val seriesName: String = "",
    @Json(name="original_name") val seriesOriginalTitle: String = "",
    @Json(name="origin_country") @TypeAnnotation val filmType: String = "Film"
) : Parcelable

@Parcelize
data class FilmOrSeries (
    @Json(name="poster_path") @NullToEmptyStrAnnotation var filmImagePath: String? = "",
    @Json(name="id") val id: String,
    @Json(name="genres") @GenreAGAINAnnotation val filmGenres: String,
    @Json(name="title") val filmName: String = "",
    @Json(name="vote_average") val filmRating: String,
    @Json(name="overview") val filmDescription: String,
    @Json(name="release_date") @DateAnnotation val filmReleaseDate: String = "",
    @Json(name="first_air_date") @DateAnnotation val seriesReleaseDate: String = "",
    @Json(name="original_language") val filmOriginalLanguage: String,
    @Json(name="original_title") val filmOriginalTitle: String = "",
    @Json(name="name") val seriesName: String = "",
    @Json(name="original_name") val seriesOriginalTitle: String = "",
    @Json(name="languages") @TypeAnnotation val filmType: String = "Film"
) : Parcelable

data class Json(
    var results: List<FilmProperty>
)

//@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)

class GenreAGAINAdapter {
    @ToJson
    fun toJson(@GenreAGAINAnnotation type: String): List<Map<String, String>>{
        return listOf(mapOf<String, String>())
    }
    @FromJson
    @GenreAGAINAnnotation
    fun fromJson(genres: List<Map<String, String>>): String {
        val list = mutableListOf<String>()
        genres.forEach {
            it -> list.add(it["name"]?: "UNKNOWN GENRE")
        }
        return list.joinToString(", ")
//        return if (originCountry.isNotEmpty()) "Series"
//        else "Film"
    }
}

class TypeAdapter {
    @ToJson
    fun toJson(@TypeAnnotation type: String): List<String> {
        return listOf(type)
    }
    @FromJson
    @TypeAnnotation
    fun fromJson(originCountry: List<String>): String {
        return "Series"
//        return if (originCountry.isNotEmpty()) "Series"
//        else "Film"
    }
}

class DateAdapter {
    @ToJson
    fun toJson(@DateAnnotation date: String): String {
        return date.split(" ").joinToString("-")
    }
    @FromJson
    @DateAnnotation
    fun fromJson(date: String): String {
        if(date.isBlank()) return "N/A"
        val dateArr =  date.split("-")
        val month = when(dateArr[1]) {
            "1" -> "Jan"
            "01" -> "Jan"
            "2" -> "Feb"
            "02" -> "Feb"
            "3" -> "Mar"
            "03" -> "Mar"
            "4" -> "Apr"
            "04" -> "Apr"
            "5" -> "May"
            "05" -> "May"
            "6" -> "Jun"
            "06" -> "Jun"
            "7" -> "Jul"
            "07" -> "Jul"
            "8" -> "Aug"
            "08" -> "Aug"
            "9" -> "Sep"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dec"
            else -> "WTFMonth: " + dateArr[1]
        }
        val retString = dateArr[2] +" "+ month +" "+ dateArr[0]
        return retString
    }
}

class GenreAdapter  {

    @ToJson
    fun toJson(@GenreAnnotation genres: String): List<Int> {
//        return genres.map { genre -> genre.id}
        val genresArr: MutableList<Int> = mutableListOf()
        for (genre in genres.split(", ")) {
            genresArr.add(
                when (genre) {
                "Action" -> 28
                "Adventure" -> 12
                "Animation" -> 16
                "Comedy" -> 35
                "Crime" -> 80
                "Documentary" -> 99
                "Drama" -> 18
                "Family" -> 10751
                "Fantasy" -> 14
                "History" -> 36
                "Horror" -> 27
                "Music" -> 10402
                "Romance" -> 10749
                "Mystery" -> 9648
                "Science Fiction" -> 878
                "TV Movie" -> 10770
                "Mystery" -> 53 //TODO find out why are there 2 Mysteries
                "War" -> 10752
                "Western" -> 37
                "Action & Adventure" -> 10759
                "Sci-Fi & Fantasy" -> 10765
                "Kids" -> 10762
                "News" -> 10763
                "Reality" -> 10764
                "Soap" -> 10766
                "Talk" -> 10767
                "War & Politics" -> 10768
                else -> -1 //throw JsonDataException("unknown genre : $genre ")
            }
            )
        }
        return genresArr
    }

    @FromJson
    @GenreAnnotation
    fun fromJson( genreIdList: List<Int>): String {
        var genresString = ""
        for (genreId in genreIdList) {
            genresString += when (genreId) {
                28 -> "Action"
                12 -> "Adventure"
                16 -> "Animation"
                35 -> "Comedy"
                80 -> "Crime"
                99 -> "Documentary"
                18 -> "Drama"
                10751 -> "Family"
                14 -> "Fantasy"
                36 -> "History"
                27 -> "Horror"
                10402 -> "Music"
                9648 -> "Mystery"
                10749 -> "Romance"
                878 -> "Science Fiction"
                10770 -> "TV Movie"
                53 -> "Mystery"
                10752 -> "War"
                37 -> "Western"
                10759 -> "Action & Adventure"
                10765 -> "Sci-Fi & Fantasy"
                10762 -> "Kids"
                10763 -> "News"
                10764 -> "Reality"
                10766 -> "Soap"
                10767 -> "Talk"
                10768 -> "War & Politics"
                else -> "WTFGenre: $genreId" //throw JsonDataException("unknown genre id: $genreId")
            }
            genresString += ", "
        }
//        Log.i("Str:", genresString)
        genresString = genresString.dropLast(2)
//        Log.i("Str:", genresString)
        return genresString
    }
}

class NullToEmptyStringAdapter {
    @ToJson
    fun toJson(@NullToEmptyStrAnnotation value: String?): String? {
        return value
    }

    @FromJson
    @NullToEmptyStrAnnotation
    fun fromJson(data: String?): String? {
        return data ?: ""
    }
}

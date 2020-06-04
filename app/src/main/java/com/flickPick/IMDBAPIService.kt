package com.flickPick

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://api.themoviedb.org/3/"

//private const val BASE_PIC_URL = "http://image.tmdb.org/t/p/w342/2CAL2433ZeIihfX1Hb2139CX0pW.jpg"

private val moshi = Moshi.Builder()
    .add(GenreAdapter())
    .add(DateAdapter())
    .add(NullToEmptyStringAdapter())
    .add(TypeAdapter())
    .add(GenreAGAINAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()


//val adapter: JsonAdapter<FilmProperty> = moshi.adapter(FilmProperty::class.java)
//val movie = adapter.fromJson(moviesJson))

private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL).build()

private val apiKey = "ee3eb8d2fcb78819e8da76ea3cc4c49f"

interface IMDBAPIService {
    @GET("movie/top_rated")
    fun getTopRatedFilmsAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("movie/popular")
    fun getPopularFilmsAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("movie/now_playing")
    fun getNowPlayingFilmsAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("movie/upcoming")
    fun getUpcomingFilmsAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("tv/top_rated")
    fun getTopRatedSeriesAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("tv/popular")
    fun getPopularSeriesAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET("tv/on_the_air")
    fun getOnAirSeriesAsync(@Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

    @GET
    fun getMovieOrSeriesByIdAsync(@Url url: String, @Query("api_key") apiKey: String):
            Deferred<FilmOrSeries>

    @GET("search/multi")
    fun getSearchResultsAsync(@Query("query") encodedName: String, @Query("api_key") apiKey: String, @Query("page") pageNum: Int):
            Deferred<Json>

}



object IMDBAPI {
//    val retrofitService: IMDBAPIService = retrofit.create(IMDBAPIService::class.java)
    val retrofitService: IMDBAPIService by lazy {
        retrofit.create(IMDBAPIService::class.java)
    }
}
package com.flickPick.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FilmDatabaseDao {
    @Insert
    fun insert(profile: UserProfile)

    @Update
    fun update(profile: UserProfile)

//    @Query("update userProfilesTable set Interested = :profile where Id = 1")
//    fun test(profile: List<Int>)

    @Query("select * from userProfilesTable where name = :name")
    fun getUserByName(name: String): UserProfile//LiveData<UserProfile>

//    @Query("select specialFilms from userProfilesTable where name = :name")
//    @TypeConverters(SpecialFilmsConverter::class)
//    fun getSpecialFilmsByName(name: String): ArrayList<Int>//Map<String, List<Int>>//LiveData<MutableMap<String, ArrayList<Int>>>

//    @Query("select :category from userProfilesTable where name = :name")
//    fun getCertainFilmsByName(category: String, name: String): List<Pair<String, Int>>//Set<Int>//LiveData<List<Int>>

//    @Query("select * from userProfilesTable where name = :name")
//    fun getCertainFilmsTest(name: String): UserProfile
//
//    @Query("select 'Interested' from userProfilesTable where name = :name")
//    fun getCertainFilmTTT(name: String): List<Int>

//    @Query("update ")
    @Query("delete from userProfilesTable")
    fun clearTable()
    @Query("select * from userProfilesTable")
    fun getAllTable(): List<UserProfile>
}
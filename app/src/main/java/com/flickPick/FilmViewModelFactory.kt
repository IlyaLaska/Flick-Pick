package com.flickPick

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flickPick.db.FilmDatabaseDao

class FilmViewModelFactory (private val dataSource: FilmDatabaseDao,
                            private val application: Application): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FilmViewModel::class.java)) {
            return FilmViewModel(dataSource, application) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}
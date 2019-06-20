package com.obcompany.advancedandroid.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.obcompany.advancedandroid.app.ui.MainViewModel
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.repository.UserRepository

class MainViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

    companion object{
        @JvmStatic
        fun provide(context: Context): MainViewModelFactory{
            val database = AppDatabase.getInstance(context)
            val repository = UserRepository(database.userDao())
            return MainViewModelFactory(repository)
        }
    }
}
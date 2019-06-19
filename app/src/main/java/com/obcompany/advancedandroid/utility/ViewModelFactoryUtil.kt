package com.obcompany.advancedandroid.utility

import android.content.Context
import com.obcompany.advancedandroid.app.viewmodel.MainViewModelFactory
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.repository.UserRepository


object ViewModelFactoryUtil {
    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        val database = AppDatabase.getInstance(context)
        val repository = UserRepository(database.userDao())
        return MainViewModelFactory(repository)
    }
}
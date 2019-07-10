package com.obcompany.advancedandroid.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.obcompany.advancedandroid.app.ui.MainViewModel
import com.obcompany.advancedandroid.app.ui.post.PostViewModel
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.repository.UserRepository
import com.obcompany.advancedandroid.utility.InjectionUtil

class PostViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(repository) as T
    }

    companion object{
        @JvmStatic
        fun provide(context: Context): PostViewModelFactory{
            val repository = InjectionUtil.provideUserRepository(context)
            return PostViewModelFactory(repository)
        }
    }
}
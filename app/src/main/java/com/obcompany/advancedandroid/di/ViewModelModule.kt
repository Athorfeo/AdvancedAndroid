package com.obcompany.advancedandroid.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.obcompany.advancedandroid.app.ui.MainViewModel
import com.obcompany.advancedandroid.app.ui.post.PostViewModel
import com.obcompany.advancedandroid.app.viewmodel.AppViewModelFactory
import com.obcompany.advancedandroid.utility.base.BaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    /*@Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun bindSearchViewModel(postViewModel: PostViewModel): ViewModel*/

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}

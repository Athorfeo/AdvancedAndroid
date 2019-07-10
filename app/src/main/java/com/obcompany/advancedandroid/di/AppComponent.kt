package com.obcompany.advancedandroid.di

import android.app.Application
import com.obcompany.advancedandroid.app.ui.MainActivity
import com.obcompany.advancedandroid.utility.base.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}
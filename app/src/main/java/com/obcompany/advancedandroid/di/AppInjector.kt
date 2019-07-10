package com.obcompany.advancedandroid.di

import com.obcompany.advancedandroid.app.ui.MainActivity

object AppInjector {
    fun init(app: MainActivity){
        DaggerAppComponent
            .builder()
            .application(app.application)
            .build()
            .inject(app)
    }
}
package com.obcompany.advancedandroid.utility

import android.content.Context
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.repository.UserRepository

class InjectionUtil {
    companion object{
        @JvmStatic
        fun provideDatabase(context: Context): AppDatabase{
            return AppDatabase.getInstance(context)
        }

        @JvmStatic
        fun provideUserRepository(context: Context): UserRepository{
            val database = provideDatabase(context)
            return UserRepository(database.userDao())
        }
    }
}
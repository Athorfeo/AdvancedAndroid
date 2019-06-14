package com.obcompany.advancedandroid.repository

import androidx.lifecycle.LiveData
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.database.dao.UserDao
import com.obcompany.advancedandroid.repository.bound.NetworkBoundResource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import retrofit2.Response

class UserRepository(private val userDao: UserDao): BaseRepository(){
    fun getUsers(): LiveData<Resource<MutableList<User>>>{
        return object : NetworkBoundResource<MutableList<User>>() {
            override fun notifyDisposable(disposable: Disposable) {
                addDisposable(disposable)
            }

            override fun getFromDb(): Flowable<MutableList<User>> {
                return userDao.getUsers()
            }

            override fun getService(): Single<Response<MutableList<User>>> {
                return api.getUsers()
            }
        }.asLiveData()
    }

}
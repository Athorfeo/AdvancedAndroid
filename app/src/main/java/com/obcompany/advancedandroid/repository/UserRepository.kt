package com.obcompany.advancedandroid.repository

import androidx.lifecycle.LiveData
import com.obcompany.advancedandroid.app.model.Post
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.database.dao.UserDao
import com.obcompany.advancedandroid.repository.bound.DatabaseNetworkBoundResource
import com.obcompany.advancedandroid.repository.bound.RestNetworkBoundResource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import retrofit2.Response

class UserRepository(private val userDao: UserDao): BaseRepository(){
    //Database + Api
    /*fun getUsers(): LiveData<Resource<MutableList<User>>>{
        return object : DatabaseNetworkBoundResource<MutableList<User>>() {
            override fun notifyDisposable(disposable: Disposable) {
                addDisposable(disposable)
            }

            override fun shouldFetch(data: MutableList<User>?): Boolean {
                return data?.size ?: 0 > 0
            }

            override fun getFromDb(): Flowable<MutableList<User>> {
                return userDao.getUsers()
            }

            override fun getService(): Single<Response<MutableList<User>>> {
                return api.getUsers()
            }

            override fun getSaveData(users: MutableList<User>): Completable {
                val usersData = users as List<User>
                return userDao.insertAll(*usersData.toTypedArray())
            }
        }.asLiveData()
    }*/

    //Rest
    fun getUsers(): LiveData<Resource<MutableList<User>>>{
        return object : RestNetworkBoundResource<MutableList<User>>() {
            override fun getService(): Single<Response<MutableList<User>>> {
                return api.getUsers()
            }
        }.asLiveData()
    }

    fun getPosts(userId: Int): LiveData<Resource<MutableList<Post>>>{
        return object : RestNetworkBoundResource<MutableList<Post>>() {
            override fun getService(): Single<Response<MutableList<Post>>> {
                return api.getPosts(userId)
            }
        }.asLiveData()
    }
}
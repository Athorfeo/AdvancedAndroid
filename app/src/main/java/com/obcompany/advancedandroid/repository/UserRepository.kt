package com.obcompany.advancedandroid.repository

import androidx.lifecycle.LiveData
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.utility.SimpleNetworkBoundResource
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import retrofit2.Response

class UserRepository: BaseRepository(){
    fun getUsers(): LiveData<Resource<MutableList<User>>>{
        return object : SimpleNetworkBoundResource<MutableList<User>>() {
            override fun notifyDisposable(disposable: Disposable) {
                addDisposable(disposable)
            }

            override fun callService(): Observable<Response<MutableList<User>>> {
                return api.getUsers()
            }
        }.asLiveData()
    }

}
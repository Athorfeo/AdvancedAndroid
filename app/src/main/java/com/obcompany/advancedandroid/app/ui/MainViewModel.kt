package com.obcompany.advancedandroid.app.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.database.AppDatabase
import com.obcompany.advancedandroid.repository.UserRepository
import com.obcompany.advancedandroid.utility.Constants
import com.obcompany.advancedandroid.utility.Status
import com.obcompany.advancedandroid.utility.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainViewModel (private val repository: UserRepository, private val database: AppDatabase) : BaseViewModel(){
    private val _users = MediatorLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>> = _users

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun getUsers(){
        _users.removeSource(users)
        val liveData = repository.getUsers()

        _users.addSource(liveData){ data ->
            processResource(data)
        }
    }

    private fun processResource(resource: Resource<MutableList<User>>){
        when (resource.status) {
            Status.LOADING -> {
                setLoading(true)
            }
            Status.SUCCESS -> {
                setLoading(false)
                resource.data?.let {
                    if(it.isNotEmpty()){
                        _users.value = it
                        //saveUsers()
                    }
                }
            }
            Status.ERROR -> {
                setLoading(false)
            }
        }
    }

    fun searchUser(search: String): MutableList<User>{
        val searchedList = mutableListOf<User>()
        users.value?.let{
            for (user in it){
                if(user.name.toLowerCase().contains(search.toLowerCase())){
                    searchedList.add(user)
                }
            }
        }

        return searchedList
    }
}
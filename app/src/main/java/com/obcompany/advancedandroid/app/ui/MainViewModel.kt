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
    val isEmpty = MutableLiveData<Boolean>().apply {value = false}
    private val _users = MediatorLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>> = _users

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun getUsers(){
        userFetch()
        //Verificamos si existe registros en la db.
        /*val disposable = database.userDao().getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    if (data.isNotEmpty()) {
                        _users.removeSource(users)
                        val mutableLiveData = MutableLiveData<Resource<MutableList<User>>>().apply { value =  Resource.success(data)}
                        val liveData = mutableLiveData as LiveData<Resource<MutableList<User>>>

                        _users.addSource(liveData){ resource ->
                            _users.value = resource.data
                        }
                    }else{
                        //Si no hay datos se llama al servicio
                        userFetch()
                    }
                },
                {
                    //Si hay error se llama al servicio.
                   userFetch()
                }
            )

        repository.addDisposable(disposable)*/
    }

    private fun userFetch(){
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

    private fun saveUsers(){
        val usersData = users.value as List<User>

        val disposable = database.userDao().insertAll(*usersData.toTypedArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i(Constants.LOG_I, "Insert Ok")
                },
                {
                    Log.i(Constants.LOG_I, "Insert Error")
                }
            )

        repository.addDisposable(disposable)
    }
}
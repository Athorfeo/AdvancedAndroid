package com.obcompany.advancedandroid.repository.bound

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.utility.Constants
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * NetworkBoundResource
 *
 * Servicio: debe ser de tipo Observable, Flowable o Single.
 * Database[GET]: debe ser de tipo Observable, Flowable o Single.
 * Database[Insert]: debe ser de tipo Completable.
 *
 * */
abstract class NetworkBoundResource<T>{
    protected val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)
    }

    protected fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected fun executeService(service: Single<Response<T>>): LiveData<Response<T>>{
        return LiveDataReactiveStreams.fromPublisher(
            service
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
                .doOnComplete { Log.i(Constants.LOG_I, "executeService()") }
        )
    }

    protected fun executeLoadDb(loadFromDb: Flowable<T>): LiveData<T>{
        return LiveDataReactiveStreams.fromPublisher(
            loadFromDb
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally{ Log.i(Constants.LOG_I, "executeLoadDb() - doOnComplete") }
        )
    }

    @SuppressLint("CheckResult")
    protected fun saveData(save: Completable): Disposable{
        return save
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(Constants.LOG_I, "saveData() - Success") },
                { Log.i(Constants.LOG_I, "saveData() - Error") }
            )
    }

    protected abstract fun bindData(apiResponse: ApiResponse<T>?)
    fun asLiveData() = result as LiveData<Resource<T>>

    /**
     * Service
     * */
    protected abstract fun getService(): Single<Response<T>>


}
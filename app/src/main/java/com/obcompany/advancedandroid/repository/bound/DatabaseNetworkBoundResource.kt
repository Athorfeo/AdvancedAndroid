package com.obcompany.advancedandroid.repository.bound

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.obcompany.advancedandroid.api.response.ApiEmptyResponse
import com.obcompany.advancedandroid.api.response.ApiErrorResponse
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.api.response.ApiSuccessResponse
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.utility.Constants
import com.obcompany.advancedandroid.utility.RxUtil
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
abstract class DatabaseNetworkBoundResource<T>: NetworkBoundResource<T>(){
    init {
        @Suppress("LeakingThis")
        val dbSource = executeLoadDb(getFromDb())

        result.addSource(dbSource){data ->
            result.removeSource(dbSource)
            if(!shouldFetch(data)){
                execute(dbSource)
            }else{
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun execute(dbSource: LiveData<T>){
        val responseSource = executeService(getService())

        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        result.addSource(responseSource) { response ->
            result.removeSource(responseSource)
            result.removeSource(dbSource)

            val apiResponse = ApiResponse.create(response)
            bindData(apiResponse)
        }

    }

    override fun bindData(apiResponse: ApiResponse<T>?){
        when (apiResponse) {
            is ApiSuccessResponse -> {
                notifyDisposable(saveData(getSaveData(apiResponse.body)))

                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.success(newData))
                }
            }
            is ApiEmptyResponse -> {
                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.success(newData))
                }
            }
            is ApiErrorResponse -> {
                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.error("Error API", newData))
                }
            }
        }
    }

    protected abstract fun shouldFetch(data: T?): Boolean
    protected abstract fun notifyDisposable(disposable: Disposable)

    /**
     * Database
     * */
    protected abstract fun getFromDb(): Flowable<T>
    protected abstract fun getSaveData(users: T): Completable


}
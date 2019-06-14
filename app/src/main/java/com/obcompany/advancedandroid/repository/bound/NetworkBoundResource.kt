package com.obcompany.advancedandroid.repository.bound

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.obcompany.advancedandroid.api.response.ApiEmptyResponse
import com.obcompany.advancedandroid.api.response.ApiErrorResponse
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.api.response.ApiSuccessResponse
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.utility.RxUtil
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
abstract class NetworkBoundResource<T> {
    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)

        @Suppress("LeakingThis")
        val dbSource = executeLoadDb()

        result.addSource(dbSource){data ->
            result.removeSource(dbSource)
            execute(dbSource)
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun executeService(): LiveData<Response<T>>{
        return LiveDataReactiveStreams.fromPublisher(
            getService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
        )
    }

    private fun executeLoadDb(): LiveData<T>{
        return LiveDataReactiveStreams.fromPublisher(
            getFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        )
    }

    private fun execute(dbSource: LiveData<T>){
        val responseSource = executeService()

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

    private fun bindData(apiResponse: ApiResponse<T>?): Boolean{
        var isSuccess = false
        when (apiResponse) {
            is ApiSuccessResponse -> {
                //DEBERIA GUARDAR EN DB
                setValue(Resource.success(apiResponse.body))
                isSuccess = true
            }
            is ApiEmptyResponse -> {
                setValue(Resource.error("Error", null))
                isSuccess = false
            }
            is ApiErrorResponse -> {
                setValue(Resource.error("Error", null))
                isSuccess = false
            }
        }
        return isSuccess
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    protected open fun processResponse(response: ApiSuccessResponse<T>) = response.body
    protected abstract fun notifyDisposable(disposable: Disposable)

    /**
     * Service
     * */
    protected abstract fun getService(): Single<Response<T>>

    /**
     * Database
     * */
    protected abstract fun getFromDb(): Flowable<T>

}
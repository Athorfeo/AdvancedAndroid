package com.obcompany.advancedandroid.repository.bound

import androidx.lifecycle.LiveData
import com.obcompany.advancedandroid.api.response.ApiEmptyResponse
import com.obcompany.advancedandroid.api.response.ApiErrorResponse
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.api.response.ApiSuccessResponse
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.database.DbResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

/**
 * NetworkBoundResource
 *
 * Servicio: debe ser de tipo Observable, Flowable o Single.
 * Database - GET: debe ser de tipo Observable, Flowable o Single.
 * Database - Insert: debe ser de tipo Completable.
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
                    setValue(Resource.success(newData.data))
                }
            }
        }
    }

    private fun execute(dbSource: LiveData<DbResponse<T>>){
        val responseSource = executeService(getService())

        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData.data))
        }

        result.addSource(responseSource) { apiResponse ->
            result.removeSource(responseSource)
            result.removeSource(dbSource)
            bindData(apiResponse)
        }

    }

    override fun bindData(apiResponse: ApiResponse<T>?){
        when (apiResponse) {
            is ApiSuccessResponse -> {
                notifyDisposable(saveData(getSaveData(apiResponse.body)))

                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.success(newData.data))
                }
            }
            is ApiEmptyResponse -> {
                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.success(newData.data))
                }
            }
            is ApiErrorResponse -> {
                result.addSource(executeLoadDb(getFromDb())){newData ->
                    setValue(Resource.error(newData.data, apiResponse.code,apiResponse.message ?: "Hubo un problema. Se están mostrando los datos almacenados en el dipositivo"))
                }
            }
        }
    }

    protected abstract fun shouldFetch(data: DbResponse<T>?): Boolean
    protected abstract fun notifyDisposable(disposable: Disposable)

    /**
     * Database
     * */
    protected abstract fun getFromDb(): Flowable<T>
    protected abstract fun getSaveData(users: T): Completable


}
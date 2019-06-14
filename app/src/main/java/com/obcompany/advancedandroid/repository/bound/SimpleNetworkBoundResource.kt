package com.obcompany.advancedandroid.repository.bound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.obcompany.advancedandroid.api.response.ApiEmptyResponse
import com.obcompany.advancedandroid.api.response.ApiErrorResponse
import com.obcompany.advancedandroid.api.response.ApiResponse
import com.obcompany.advancedandroid.api.response.ApiSuccessResponse
import com.obcompany.advancedandroid.app.model.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

abstract class SimpleNetworkBoundResource<T> {
    //private val result = MediatorLiveData<Resource<T>>()
    private val result = MutableLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)
        execute()
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun execute() {
        val disposable = callService()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    bindData(ApiResponse.create(result))
                },
                {
                    bindData(ApiEmptyResponse())
                }
            )
        notifyDisposable(disposable)
    }

    private fun bindData(apiResponse: ApiResponse<T>?){
        when (apiResponse) {
            is ApiSuccessResponse -> {
                setValue(Resource.success(apiResponse.body))
            }
            is ApiEmptyResponse -> {
                setValue(Resource.error("Error", null))
            }
            is ApiErrorResponse -> {
                setValue(Resource.error("Error", null))
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    protected open fun processResponse(response: ApiSuccessResponse<T>) = response.body
    //protected abstract fun callService(): LiveData<ApiResponse<T>>
    protected abstract fun callService(): Observable<Response<T>>
    protected abstract fun notifyDisposable(disposable: Disposable)

}
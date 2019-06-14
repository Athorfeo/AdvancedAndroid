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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

abstract class NetworkBoundResource<T> {
    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)

        /*val apiResponse = callService()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFlowable()*/

        val apiResponse = RxUtil.execute(callService())

        result.addSource(LiveDataReactiveStreams.fromPublisher(apiResponse)){
            bindData(ApiResponse.create(it))
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun bindData(apiResponse: ApiResponse<T>?): Boolean{
        var isSuccess = false
        when (apiResponse) {
            is ApiSuccessResponse -> {
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
    //protected abstract fun callService(): LiveData<ApiResponse<T>>
    protected abstract fun callService(): Single<Response<T>>
    protected abstract fun notifyDisposable(disposable: Disposable)

}
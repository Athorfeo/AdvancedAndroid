package com.obcompany.advancedandroid.utility

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RxUtil {
    companion object{
        @JvmStatic
        fun <T> execute(rxComponent: Flowable<Response<T>>): Flowable<Response<T>>{
            return rxComponent
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        @JvmStatic
        fun <T> execute(rxComponent: Single<Response<T>>): Flowable<Response<T>>{
            return execute(rxComponent.toFlowable())
        }
    }
}
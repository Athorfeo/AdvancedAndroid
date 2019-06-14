package com.obcompany.advancedandroid.utility

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RxUtil {
    companion object{
        /*
         * Execute Main
         * Ejecuta el observable rx
         */
        @JvmStatic
        fun <T> execute(rxComponent: Flowable<Response<T>>): Flowable<Response<T>>{
            return rxComponent
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /*
         * Convierte Observable a Flowable
         */
        @JvmStatic
        fun <T> execute(rxComponent: Observable<Response<T>>): Flowable<Response<T>>{
            return execute(rxComponent.toFlowable(BackpressureStrategy.BUFFER))
        }

        /*
         * Convierte Single a Flowable
         */
        @JvmStatic
        fun <T> execute(rxComponent: Single<Response<T>>): Flowable<Response<T>>{
            return execute(rxComponent.toFlowable())
        }

        /*
         * Convierte Maybe a Flowable
         */
        @JvmStatic
        fun <T> execute(rxComponent: Maybe<Response<T>>): Flowable<Response<T>>{
            return execute(rxComponent.toFlowable())
        }

        /*
         * Solo ejecuta el Competable.
         */
        @JvmStatic
        fun execute(rxComponent: Completable): Completable{
            return rxComponent
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
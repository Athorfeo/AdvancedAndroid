package com.obcompany.advancedandroid.api

import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.utility.Constants
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("/users")
    fun getUsers() : Observable<Response<MutableList<User>>>

    companion object {
        fun create(): API{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
            return retrofit.create(API::class.java)
        }
    }

}
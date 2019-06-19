package com.obcompany.advancedandroid.app.ui.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.obcompany.advancedandroid.app.model.Post
import com.obcompany.advancedandroid.app.model.Resource
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.repository.UserRepository
import com.obcompany.advancedandroid.utility.Status
import com.obcompany.advancedandroid.utility.base.BaseViewModel


class PostViewModel (private val repository: UserRepository) : BaseViewModel(){
    private val _posts = MediatorLiveData<MutableList<Post>>()
    val posts: LiveData<MutableList<Post>> = _posts

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun getPosts(userId: Int){
        _posts.removeSource(posts)
        val liveData = repository.getPosts(userId)

        _posts.addSource(liveData){ data ->
            processResource(data)
        }
    }

    private fun processResource(resource: Resource<MutableList<Post>>){
        when (resource.status) {
            Status.LOADING -> {
                setLoading(true)
            }
            Status.SUCCESS -> {
                setLoading(false)
                resource.data?.let {
                    if(it.isNotEmpty()){
                        _posts.value = it
                        //saveUsers()
                    }
                }
            }
            Status.ERROR -> {
                setLoading(false)
            }
        }
    }
}
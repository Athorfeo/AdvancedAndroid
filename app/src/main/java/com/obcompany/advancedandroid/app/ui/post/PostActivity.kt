package com.obcompany.advancedandroid.app.ui.post

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.utility.Constants
import com.obcompany.advancedandroid.utility.ViewModelFactoryUtil
import com.obcompany.advancedandroid.utility.base.BaseActivity
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : BaseActivity(){
    private lateinit var model: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = getString(R.string.title_post_activity)

        model = ViewModelProviders.of(this, ViewModelFactoryUtil.providePostViewModelFactory(this)).get(PostViewModel::class.java)
        if(intent.extras != null) {
            val user = intent.getSerializableExtra(Constants.EXTRA_USER) as User
            val postAdapter = PostAdapter()
            init(user, postAdapter)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun init(user: User, postAdapter: PostAdapter){
        name.text = user.name
        phone.text = user.phone
        email.text = user.email
        subscribeUi(postAdapter)
        model.getPosts(user.id)
    }

    private fun subscribeUi(postAdapter: PostAdapter){
        model.isLoading.observe(this, Observer {
            if(it){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
        })

        model.posts.observe(this, Observer { response ->
            if (!response.isNullOrEmpty() || response.size > 0) {
                postAdapter.submitList(response)
                recyclerViewPostsResults.apply {
                    layoutManager = LinearLayoutManager(this@PostActivity)
                    adapter = postAdapter
                }
            }
        })
    }
}
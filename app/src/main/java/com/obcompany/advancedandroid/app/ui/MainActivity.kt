package com.obcompany.advancedandroid.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.app.ui.post.PostActivity
import com.obcompany.advancedandroid.utility.Constants
import com.obcompany.advancedandroid.utility.ViewModelFactoryUtil
import com.obcompany.advancedandroid.utility.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), UserAdapter.UserAdapterOnClickListener {
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this, ViewModelFactoryUtil.provideMainViewModelFactory(this)).get(MainViewModel::class.java)
        val usersAdapter = UserAdapter(this)
        init(usersAdapter)
    }

    override fun onItemClick(user: User, view: View) {
        when(view.id){
            R.id.btn_view_post -> {
                val intent = Intent(this, PostActivity::class.java).apply {
                    putExtra(Constants.EXTRA_USER, user)
                }
                startActivity(intent)
    }
        }
    }

    private fun init(userAdapter: UserAdapter){
        subscribeUi(userAdapter)
        model.getUsers()
    }

    private fun subscribeUi(userAdapter: UserAdapter){
        editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchedList = model.searchUser(s.toString())

                if(searchedList.count() > 0){
                    userAdapter.submitList(searchedList)
                    recyclerViewSearchResults.visibility = View.VISIBLE
                    textViewEmptyList.visibility = View.GONE
                }else{
                    textViewEmptyList.visibility = View.VISIBLE
                    recyclerViewSearchResults.visibility = View.GONE
                }
            }
        })

        model.isLoading.observe(this, Observer {
            if(it){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
        })

        model.users.observe(this, Observer { response ->
            if (!response.isNullOrEmpty() || response.size > 0) {
                userAdapter.submitList(response)
                recyclerViewSearchResults.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = userAdapter
                    visibility = View.VISIBLE
                }
                textViewEmptyList.visibility = View.GONE
            }
        })
    }
}

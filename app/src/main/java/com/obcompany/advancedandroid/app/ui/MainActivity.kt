package com.obcompany.advancedandroid.app.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.User
import com.obcompany.advancedandroid.utility.ViewModelFactoryUtil
import com.obcompany.advancedandroid.utility.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), UserAdapter.UserAdapterOnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var usersAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this, ViewModelFactoryUtil.provideMainViewModelFactory(this)).get(MainViewModel::class.java)

        init()
    }

    override fun onItemClick(user: User, view: View) {
        when(view.id){
            R.id.btn_view_post -> {
            }
        }
    }

    private fun init(){
        subscribeUi()
        model.getUsers()
    }

    private fun subscribeUi(){
        editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchedList = model.searchUser(s.toString())

                if(searchedList.count() > 0){
                    updateData(searchedList)
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
                usersAdapter = UserAdapter(this, this, response)
                recyclerViewSearchResults.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = usersAdapter
                }
            }
        })
    }

    private fun updateData(list: MutableList<User>){
        usersAdapter.updateItems(list)
    }

}

package com.obcompany.advancedandroid.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.User
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserAdapter (private val onItemClickListener: UserAdapterOnClickListener): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var list: MutableList<User>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let {
            with(holder){
                bind(createOnClickListener(it), it)
            }
        }
    }

    override fun getItemCount() = list.size

    private fun createOnClickListener(user: User): View.OnClickListener {
        return View.OnClickListener {
            onItemClickListener.onItemClick(user, it)
        }
    }

    fun submitList(list: MutableList<User>){
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        //val tvAnimalType = view.tv_animal_type
        fun bind(onClickListener: View.OnClickListener, user: User){
            with(view) {
                name.text = user.name
                phone.text = user.phone
                email.text = user.email
                btn_view_post.setOnClickListener(onClickListener)
            }
        }
    }

    interface UserAdapterOnClickListener{
        fun onItemClick(user: User, view: View)
    }
}
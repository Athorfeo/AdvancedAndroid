package com.obcompany.advancedandroid.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.User
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserAdapter (private val context: Context, private val onItemClickListener: UserAdapterOnClickListener, var items: MutableList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let {
            with(holder){
                bind(createOnClickListener(it), it)
            }
        }
    }

    override fun getItemCount() = items.size

    private fun createOnClickListener(user: User): View.OnClickListener {
        return View.OnClickListener {
            onItemClickListener.onItemClick(user, it)
        }
    }

    fun updateItems(list: MutableList<User>){
        items = list
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
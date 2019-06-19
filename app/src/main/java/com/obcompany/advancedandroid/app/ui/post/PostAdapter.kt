package com.obcompany.advancedandroid.app.ui.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obcompany.advancedandroid.R
import com.obcompany.advancedandroid.app.model.Post
import kotlinx.android.synthetic.main.post_list_item.view.*
import kotlinx.android.synthetic.main.user_list_item.view.*

class PostAdapter: RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private lateinit var list: MutableList<Post>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let {
            with(holder){
                bind(it)
            }
        }
    }

    override fun getItemCount() = list.size

    fun submitList(list: MutableList<Post>){
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post){
            with(view) {
                title.text = post.title
                body.text = post.body
            }
        }
    }
}
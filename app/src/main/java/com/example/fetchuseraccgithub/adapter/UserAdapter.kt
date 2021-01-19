package com.example.fetchuseraccgithub.adapter

import android.content.ClipData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fetchuseraccgithub.R
import com.example.fetchuseraccgithub.model.ResponseUser
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter (val data: ResponseUser): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.tv_user.text = data.login
        Glide.with(viewHolder.itemView).load(data.avatarUrl)
            .into(viewHolder.itemView.img_user)
    }

    override fun getLayout(): Int {
        return R.layout.item_user
    }
}
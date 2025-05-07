package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobiai.R
import com.mobiai.app.model.User
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemListUserBinding

class UserAdapter(val context : Context, val listener : OnUserClickListener) : BaseAdapter<User, ItemListUserBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemListUserBinding {
        return ItemListUserBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemListUserBinding, item: User, position: Int) {

        binding.tvName.text = item.name
        if (item.urlImage?.isNotEmpty() == true){
            Glide.with(context).load(item.urlImage).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)
        }
        else{
            Glide.with(context).load(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)

        }

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
          //  notifyDataSetChanged()
        }
    }


    interface OnUserClickListener {
        fun onClickItemListener(user: User?)
    }
}
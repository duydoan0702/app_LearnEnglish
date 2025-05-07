package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobiai.R
import com.mobiai.app.model.User
import com.mobiai.app.model.Vocabulary
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemListUserBinding
import com.mobiai.databinding.ItemStructureBinding
import com.mobiai.databinding.ItemWordTypeBinding

class WordTypeAdapter(val context : Context) : BaseAdapter<Vocabulary.Word_Types, ItemWordTypeBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemWordTypeBinding {
        return ItemWordTypeBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemWordTypeBinding, item:Vocabulary.Word_Types , position: Int) {
        binding.txtName.text = item.nameWord
        binding.txtDescription.text = item.describe
        binding.txtExample.text = item.example
    }

}
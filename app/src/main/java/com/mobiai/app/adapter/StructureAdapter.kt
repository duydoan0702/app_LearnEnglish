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

class StructureAdapter(val context : Context) : BaseAdapter<Vocabulary.Sentence_Structure, ItemStructureBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStructureBinding {
        return ItemStructureBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemStructureBinding, item:Vocabulary.Sentence_Structure , position: Int) {
        binding.txtName.text = item.nameStructure
        binding.txtStructure.text = item.structure
        binding.txtExplain.text = item.explain
        binding.txtExample.text = item.example
    }

}
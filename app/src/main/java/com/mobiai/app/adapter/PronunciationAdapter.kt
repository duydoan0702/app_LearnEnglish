package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.model.Pronunciation
import com.mobiai.app.ui.fragment.announcer
import com.mobiai.app.utils.Announcer
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemPronunciationBinding

    class PronunciationAdapter(val context: Context) : BaseAdapter<Pronunciation,ItemPronunciationBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemPronunciationBinding {
        return ItemPronunciationBinding.inflate(inflater,parent,false)
    }
    override fun bind(binding: ItemPronunciationBinding, item: Pronunciation, position: Int) {
        binding.title.text = item.title
        binding.description.text = item.content
        binding.root.setOnClickListener {
            if (item.textRead?.isNotEmpty() == true){
                announcer?.readText("${item.textRead}")
            }
            else{
                announcer?.readText("${binding.title.text.substring(1)}")
            }
        }
    }
}
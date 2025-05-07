package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.app.model.Results
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.databinding.ItemResultsBinding

class LessonResultAdapter(val context : Context, val listener : OnResultClickListener) : BaseAdapter<Results, ItemResultsBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemResultsBinding {
        return ItemResultsBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemResultsBinding, item: Results, position: Int) {

        binding.txtTitle.text = item.lessonCode
        binding.txtNumber.text = "${item.numberCorrect}/10"

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
        }
    }


    interface OnResultClickListener {
        fun onClickItemListener(results: Results)
    }
}
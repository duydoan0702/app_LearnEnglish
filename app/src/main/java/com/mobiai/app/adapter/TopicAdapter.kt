package com.mobiai.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaygoo.widget.RangeSeekBar
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Lessons
import com.mobiai.app.model.Results
import com.mobiai.app.model.Topic
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.ItemStudyBinding

class TopicAdapter(val context : Context, val listener : OnLessonClickListener) : BaseAdapter<Topic, ItemStudyBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStudyBinding {
        return ItemStudyBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemStudyBinding, item: Topic, position: Int) {
        binding.txtTitle.text = "${item.topicCode}: ${item.name}"
        binding.txtNumberLesson.text = item.numberLesson.toString()
        if (item.urlImg.isEmpty()){
            binding.image.setImageDrawable(context.resources.getDrawable(R.drawable.img_part1))
        }
        else{
            Glide.with(context).load(item.urlImg).diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true).into(binding.image)
        }
        getData(binding.txtNumberDone,binding.sbPercent,item.topicCode)
       // binding.sbPercent.setProgress(item.numberLesson.toFloat())

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
            notifyItemChanged(position)
        }
    }

    private fun getData(view1: TextView,view: RangeSeekBar, topicCode:String){
        var numberDone = 0
        val db = FirebaseDatabase.getInstance()
        val ref1 = db.getReference(App.LESSON)
        val ref2 = db.getReference(App.RESULTS)
            //lesson
        ref1.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val lessons = userSnapshot.getValue(Lessons::class.java)
                    if (lessons != null) {
                        if (lessons.topicCode == topicCode){
                            ref2.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (userSnapshot1 in dataSnapshot.children) {
                                        val results = userSnapshot1.getValue(Results::class.java)
                                        if (results != null) {
                                            if (results.userName == SharedPreferenceUtils.emailLogin){
                                                if (results.numberCorrect > 6 && results.lessonCode == lessons.lessonCode){
                                                    numberDone++
                                                    Log.d("TAG", "onDataChange: ${results.userName}")
                                                }
                                            }
                                        }
                                    }
                                    view1.text = numberDone.toString()
                                    view.setProgress(numberDone.toFloat())
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w("TAG", "Failed to read value.", error.toException())
                                }
                            })
                            if (numberDone!=0)
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    interface OnLessonClickListener {
        fun onClickItemListener(lesson: Topic?)
    }
}
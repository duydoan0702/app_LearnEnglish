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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaygoo.widget.RangeSeekBar
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Lessons
import com.mobiai.app.model.Level
import com.mobiai.app.model.Results
import com.mobiai.app.model.Topic
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeInvisible
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.ItemLevelBinding

class LevelAdapter(val context : Context, val listener : OnLevelClickListener) : BaseAdapter<Level, ItemLevelBinding>() {

    private var levelCodePercent1 =0
    private var levelCodePercent2 =0
    private var levelCodePercent3 =0
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemLevelBinding {
        return ItemLevelBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemLevelBinding, item: Level, position: Int) {
        if (item.lock){
            binding.imgLock.makeInvisible()
        }
        when (item.levelCode) {
            "level1" -> {
                Glide.with(context).load(R.drawable.ic_lv1).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.image)
            }
            "level2" -> {
                Glide.with(context).load(R.drawable.ic_lv2).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.image)
            }
            else -> {
                Glide.with(context).load(R.drawable.ic_lv3).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.image)
            }
        }

        getData(binding.sbPercent,item.levelCode)
        binding.txtTitle.text = item.levelCode + ": "+item.name

        binding.root.setOnClickListener {
            if (item.lock){
                listener.onClickItemListener(item)
            }
            else{
                context.showToast("hoàn thành các bài học trước đó để làm bài tiếp theo ")
            }
        }
    }

    private fun updateIsLock(levelCode:String){
        if (levelCode=="level1" && levelCodePercent1>16){
            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(App.LEVEL)
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { levels ->
                        val level = levels.getValue(Level::class.java)
                        if (level != null && level.levelCode=="level2" && !level.lock) {
                            val itemNew = Level(level.levelCode,level.name,level.numberTopic,level.urlImg,true)
                            levels.key?.let { ref.child(it).setValue(itemNew) }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        if (levelCode=="level2" && levelCodePercent2>=17){
            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(App.LEVEL)
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { levels ->
                        val level = levels.getValue(Level::class.java)
                        if (level != null && level.levelCode=="level3" && !level.lock) {
                            val itemNew = Level(level.levelCode,level.name,level.numberTopic,level.urlImg,true)
                            levels.key?.let { ref.child(it).setValue(itemNew) }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }
    private fun getData(view: RangeSeekBar, levelCode: String) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.TOPIC)
        val ref1 = db.getReference(App.LESSON)
        val ref2 = db.getReference(App.RESULTS)

        val topicsList = mutableListOf<Topic>()
        val lessonsList = mutableListOf<Lessons>()
        val resultsList = mutableListOf<Results>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(topicSnapshot: DataSnapshot) {
                topicsList.clear()
                topicSnapshot.children.forEach { topic ->
                    val topics = topic.getValue(Topic::class.java)
                    if (topics != null && topics.levelCode == levelCode) {
                        topicsList.add(topics)
                    }
                }
                fetchLessons(ref1, topicsList, lessonsList, ref2, view,levelCode)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchLessons(
        ref: DatabaseReference,
        topicsList: List<Topic>,
        lessonsList: MutableList<Lessons>,
        ref2: DatabaseReference,
        view: RangeSeekBar,levelCode: String
    ) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(lessonSnapshot: DataSnapshot) {
                lessonsList.clear()
                lessonSnapshot.children.forEach { lesson ->
                    val lessons = lesson.getValue(Lessons::class.java)
                    lessons?.let { lessonsList.add(it) }
                }
                fetchResults(topicsList, lessonsList, ref2, view,levelCode)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchResults(
        topicsList: List<Topic>,
        lessonsList: List<Lessons>,
        ref: DatabaseReference,
        view: RangeSeekBar,levelCode: String
    ) {
        var numberDone = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(resultsSnapshot: DataSnapshot) {
                resultsSnapshot.children.forEach { result ->
                    val results = result.getValue(Results::class.java)
                    if (results != null) {
                        topicsList.forEach { topic ->
                            lessonsList.forEach { lesson ->
                                if (results.userName == SharedPreferenceUtils.emailLogin &&
                                    results.numberCorrect > 6 &&
                                    results.lessonCode == lesson.lessonCode &&
                                    lesson.topicCode == topic.topicCode
                                ) {
                                    numberDone++
                                }
                            }
                        }
                    }
                }
                view.setProgress(numberDone.toFloat())

                if (levelCode == "level1"){
                    levelCodePercent1 = numberDone
                }
                else if (levelCode == "level2" && levelCodePercent1>16){
                    levelCodePercent2 = numberDone
                }
                updateIsLock(levelCode)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    interface OnLevelClickListener {
        fun onClickItemListener(level: Level)
    }
}
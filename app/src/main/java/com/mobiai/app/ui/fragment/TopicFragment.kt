package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.TopicAdapter
import com.mobiai.app.model.Topic
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentTopicBinding

class TopicFragment : BaseFragment<FragmentTopicBinding>() {

    companion object {
        val LEVEL_CODE = "LEVEL_CODE"
        val TITLE_LEVEL = "TITLE_LEVEL"
        val POSITION_SKILL = "POSITION_SKILL"
        fun instance(levelCode: String, title: String, positionSkill: Int): TopicFragment {
            Bundle().apply {
                putString(LEVEL_CODE, levelCode)
                putString(TITLE_LEVEL, title)
                putInt(POSITION_SKILL, positionSkill)
                return newInstance(TopicFragment::class.java, this)
            }
        }
    }

    private lateinit var topicAdapter: TopicAdapter
    private var listLesson: ArrayList<Topic> = arrayListOf()
    private var levelCode: String = ""
    private var titleLevel: String = ""
    private var positionSkill = -1
    override fun initView() {
        arguments.let {
            levelCode = it!!.getString(LEVEL_CODE)!!
            titleLevel = it.getString(TITLE_LEVEL)!!
            positionSkill = it.getInt(POSITION_SKILL)
        }
        binding.txtHelloHeader.text = titleLevel
        binding.ivBack.setOnClickListener {
            closeFragment(this)
        }
        initAdapter()
        initData()
    }

    private fun initAdapter() {
        topicAdapter = TopicAdapter(requireContext(), object : TopicAdapter.OnLessonClickListener {
            override fun onClickItemListener(lesson: Topic?) {
                lesson?.let { LessonFragment.instance(it.topicCode, "${it.topicCode}: ${it.name}") }
                    ?.let { addFragment(it) }
            }
        })
        binding.rcvLesson.adapter = topicAdapter
    }

    private fun initData() {
        binding.frLoading.visible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.TOPIC)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val topic = userSnapshot.getValue(Topic::class.java)
                    if (topic != null && topic.levelCode == levelCode) {
                        val topicNew = Topic(
                            topic.topicCode,
                            topic.name,
                            topic.numberLesson,
                            topic.levelCode,
                            topic.urlImg
                        )
                        listLesson.add(topicNew)
                    }
                }
                listLesson.sortBy { it.topicCode }
                topicAdapter.setItems(listLesson)
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 100)

            }

            override fun onCancelled(error: DatabaseError) {
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 100)
            }
        })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTopicBinding {
        return FragmentTopicBinding.inflate(inflater, container, false)
    }
}
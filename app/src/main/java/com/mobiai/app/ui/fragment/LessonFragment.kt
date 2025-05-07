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
import com.google.gson.Gson
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Lessons
import com.mobiai.app.model.Question
import com.mobiai.app.model.Results
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.extensions.GetListDataFromFirebase
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonBinding

class LessonFragment : BaseFragment<FragmentLessonBinding>() {

    companion object {
        val NUMBER_TOPIC = "NUMBER_TOPIC"
        val TITLE = "TITLE"
        fun instance(numberTopic: String, title: String): LessonFragment {
            Bundle().apply {
                putString(NUMBER_TOPIC, numberTopic)
                putString(TITLE, title)
                return newInstance(LessonFragment::class.java, this)
            }
        }
    }

    private var numberTopic: String? = null
    private var titleHeader: String? = null
    private var listDataWithTopic = arrayListOf<Question>()
    private var listDataLessonWithTopic = arrayListOf<Lessons>()
    private val level1List = mutableListOf<Question>()


    override fun initView() {
        arguments.let {
            if (it != null) {
                numberTopic = it.getString(NUMBER_TOPIC)
                titleHeader = it.getString(TITLE)
            }
        }
        Log.d("TAG", "initView==>: $numberTopic")
        binding.txtHelloHeader.text = "$titleHeader"

        disableAllItem()
        getDataLesson()
        getData(object : GetListDataFromFirebase<Question> {
            override fun getListDataSuccess(list: List<Question>) {
                for (i in list) {
                    level1List.add(i)
                }
            }

            override fun getDataFail(err: String) {
                requireContext().showToast(err)
            }

        })
        showItemClick()

        binding.ivBack.setOnSafeClickListener(300) {
            handlerBackPressed()
        }

        binding.frStudy1.setOnSafeClickListener {

            addFragmentLesson(0)
        }

        binding.frStudy2.setOnSafeClickListener {
//            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[1].lessonCode))
//                ,listDataLessonWithTopic[1].lessonCode))
            addFragmentLesson(1)
        }

        binding.frStudy3.setOnSafeClickListener {
//            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[2].lessonCode)),
//                listDataLessonWithTopic[2].lessonCode))
            addFragmentLesson(2)


        }

        binding.frStudy4.setOnSafeClickListener {
//            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[3].lessonCode))
//                ,listDataLessonWithTopic[3].lessonCode))
            addFragmentLesson(3)

        }


        binding.frStudy5.setOnSafeClickListener {
//            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[4].lessonCode))
//                ,listDataLessonWithTopic[4].lessonCode))
            addFragmentLesson(4)

        }

        binding.frStudy6.setOnSafeClickListener {
//            addFragment(QuestionFragment.instance(getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[4].lessonCode)),
//                listDataLessonWithTopic[4].lessonCode))
            addFragmentLesson(4)

        }
    }

    private fun addFragmentLesson(index: Int) {
        binding.frLoading.visible()
        addFragment(
            QuestionFragment.instance(
                getListRandomForLevel(getNumberLevel(listDataLessonWithTopic[index].lessonCode)),
                listDataLessonWithTopic[index].lessonCode
            )
        )
        Handler().postDelayed({
            binding.frLoading.makeGone()
        }, 200)
    }

    private fun disableAllItem() {
        binding.frStudy2.isEnabled = false
        binding.frStudy3.isEnabled = false
        binding.frStudy4.isEnabled = false
        binding.frStudy5.isEnabled = false
        binding.frStudy6.isEnabled = false
    }

    private fun showItemClick() {
        val db = FirebaseDatabase.getInstance()
        val ref2 = db.getReference(App.RESULTS)
        // results
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val results = userSnapshot.getValue(Results::class.java)
                    if (results != null) {
                        if (results.numberCorrect > 6) {
                            if (results.userName == SharedPreferenceUtils.emailLogin) {
                                for (item in listDataLessonWithTopic) {
                                    if (results.lessonCode == item.lessonCode) {
                                        enableWithItem(getNumberLevel(item.lessonCode) + 1)
                                        Log.d("========", "onDataChange: ${item.lessonCode}")

                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    var enable1 = false
    var enable2 = false
    var enable3 = false
    var enable4 = false
    var enable5 = false
    var enable6 = false
    private fun enableWithItem(lessonCode: Int) {
        if (isAdded)
            when (lessonCode) {
                getNumberLevel(listDataLessonWithTopic[0].lessonCode) -> {
                    binding.frStudy1.isEnabled = true
                    enable1 = true
                }

                getNumberLevel(listDataLessonWithTopic[1].lessonCode) -> {
                    binding.frStudy2.isEnabled = true
                    binding.ivStudy2.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_5_on))
                    binding.bgLessonNext2.makeVisible()
                    enable2 = true

                }

                getNumberLevel(listDataLessonWithTopic[2].lessonCode) -> {
                    binding.frStudy2.isEnabled = true
                    binding.frStudy3.isEnabled = true
                    binding.ivStudy3.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_3_on))
                    binding.bgLessonNext3.makeVisible()
                    enable3 = true
                }

                getNumberLevel(listDataLessonWithTopic[3].lessonCode) -> {
                    binding.frStudy2.isEnabled = true
                    binding.frStudy3.isEnabled = true
                    binding.frStudy4.isEnabled = true
                    binding.ivStudy4.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_4_on))
                    binding.bgLessonNext4.makeVisible()
                    enable4 = true
                }

                getNumberLevel(listDataLessonWithTopic[4].lessonCode) -> {
                    binding.frStudy2.isEnabled = true
                    binding.frStudy3.isEnabled = true
                    binding.frStudy4.isEnabled = true
                    binding.frStudy5.isEnabled = true
                    binding.ivStudy5.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_5_on))
                    binding.bgLessonNext5.makeVisible()
                    enable5 = true
                }

                getNumberLevel(listDataLessonWithTopic[4].lessonCode) -> {
                    binding.frStudy2.isEnabled = true
                    binding.frStudy3.isEnabled = true
                    binding.frStudy4.isEnabled = true
                    binding.frStudy5.isEnabled = true
                    binding.frStudy6.isEnabled = true
                    binding.ivStudy6.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_study_6_on))
                    binding.bgLessonNext6.makeVisible()
                    enable6 = true
                }
            }

    }

    private fun getNumberLevel(inputString: String): Int {
        // Sử dụng regex để lấy ra số từ chuỗi
        val regex = Regex("\\d+")
        val matchResult = regex.find(inputString)

        // Kiểm tra xem có kết quả phù hợp nào không
        if (matchResult != null) {
            return matchResult.value.toInt()
        } else {
            return -1
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    private fun getDataLesson() {
        binding.frLoading.makeVisible()
        val db = FirebaseDatabase.getInstance()
        val ref1 = db.getReference(App.LESSON)
        //lesson
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val lesson = userSnapshot.getValue(Lessons::class.java)
                    if (lesson != null) {
                        if (lesson.topicCode == numberTopic) {
                            listDataLessonWithTopic.add(lesson)
                        }
                    }
                }
                Log.w("TAG", "listDataLesson: $listDataLessonWithTopic")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun getData(getListDataFromFirebase: GetListDataFromFirebase<Question>) {
        binding.frLoading.makeVisible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.QUESTION)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val question = userSnapshot.getValue(Question::class.java)

                    if (question != null && question.topicCode == numberTopic && question.imaLoai == 1) {
                        listDataWithTopic.add(question)
                    }
                }
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 1000)
                getListDataFromFirebase.getListDataSuccess(listDataWithTopic)
            }

            override fun onCancelled(error: DatabaseError) {
                getListDataFromFirebase.getDataFail(error.message)
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 1000)
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun getListRandomForLevel(level: Int): String {
        val listQuestion: ArrayList<Question> = arrayListOf()
        listQuestion.addAll(level1List.shuffled().take(10))
        val gson = Gson()
        return gson.toJson(listQuestion)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLessonBinding {
        return FragmentLessonBinding.inflate(inflater, container, false)
    }
}
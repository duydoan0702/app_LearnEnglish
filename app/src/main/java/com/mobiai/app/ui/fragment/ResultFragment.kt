package com.mobiai.app.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.BuildConfig
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.AnsweredQuestions
import com.mobiai.app.model.Results
import com.mobiai.app.model.User
import com.mobiai.app.utils.DynamicUnitUtils
import com.mobiai.app.utils.photoTemp
import com.mobiai.app.utils.saveImageToFile
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentResultBinding
import java.io.File


class ResultFragment : BaseFragment<FragmentResultBinding>() {
    companion object {
        const val NUMBER_CORRECT = "NUMBER_CORRECT"
        fun instance(numberCorrect: Int): ResultFragment {
            Bundle().apply {
                putInt(NUMBER_CORRECT, numberCorrect)
                return newInstance(ResultFragment::class.java, this)
            }
        }
    }

    var numberCorrect = 0
    var ruby = 0
    var xp = 0
    private val REQUEST_SCREENSHOT = 123
    private var bitmap: Bitmap? = null

    //    private var listDataReview = arrayListOf<Question>()
//    private var listDataReview = arrayListOf<Question>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        arguments.let {
            if (it != null) {
                numberCorrect = it.getInt(NUMBER_CORRECT)
                ruby = 50 * numberCorrect
                xp = 100 * numberCorrect
            }
            binding.txtRuby.text = "$ruby"
            binding.txtExperience.text = "$xp"
            updateResultToFirebase()
            upRubyAndXp()
        }
        binding.ivClose.setOnClickListener {
            replaceFragment(BottomNavigationFragment.instance())
        }
        binding.btnContinue.setOnClickListener {
            replaceFragment(BottomNavigationFragment.instance())
        }
        binding.txtResult.text =
            requireContext().getString(R.string.you_have_just_completed_the_excellent_challenge) + "($numberCorrect/10)"
        binding.btnShare.setOnClickListener {
            bitmap?.recycle()
            bitmap = createBitmapFromView()
            val pathImage = bitmap!!.saveImageToFile(requireContext(), photoTemp)
            if (pathImage != null) {
                shareImage(pathImage)
            }
        }
    }

    private fun shareImage(filePath: String) {
        val imageFile = File(filePath)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".fileprovider",
            imageFile
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "share_image_using"))
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater, container, false)
    }

    private fun updateResultToFirebase() {
        val codeResult = "${SharedPreferenceUtils.lessonCode}_${SharedPreferenceUtils.emailLogin}"
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.RESULTS)
        var i = 0
        var isExist = false
        val resultUpdate = Results(
            codeResult, SharedPreferenceUtils.emailLogin.toString(),
            SharedPreferenceUtils.lessonCode.toString(), numberCorrect
        )
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (userSnapshot in dataSnapshot.children) {
                    val result = userSnapshot.getValue(AnsweredQuestions::class.java)
                    if (result != null) {
                        if (result.codeResult == resultUpdate.codeResult && i < 1) {
                            userSnapshot.key?.let { ref.child(it).setValue(resultUpdate) }
                            i++
                            isExist = true
                            break
                        }
                    }
                }
                if (!isExist) {
                    ref.push().setValue(resultUpdate)
                    isExist = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun upRubyAndXp() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.USER)
        var i = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin && i < 1) {
                            val userUpdate = User(
                                SharedPreferenceUtils.emailLogin!!,
                                user.name,
                                user.pass,
                                user.urlImage,
                                user.ruby + ruby,
                                user.totalXp + xp
                            )
                            userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                            i++
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    fun createBitmapFromView(
        width: Int = 0,
        height: Int = 0,
//            callback: (bitmap: Bitmap) -> Unit
    ): Bitmap {
        if (width > 0 && height > 0) {
            binding.root.measure(
                View.MeasureSpec.makeMeasureSpec(
                    DynamicUnitUtils
                        .convertDpToPixels(width.toFloat()), View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    DynamicUnitUtils
                        .convertDpToPixels(height.toFloat()), View.MeasureSpec.EXACTLY
                )
            )
        }
//    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            binding.root.measuredWidth,
            binding.root.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background = binding.root.background
        background?.draw(canvas)
        binding.root.draw(canvas)
//        callback(bitmap)
        return bitmap
    }

}
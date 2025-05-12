package com.mobiai.app.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.R
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentSettingBinding


class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    companion object {
        fun instance(): SettingFragment {
            return newInstance(SettingFragment::class.java)
        }
    }

    override fun initView() {
//        binding.sbPercent.setProgress(
//            SharedPreferenceUtils.targetDailyCount.toFloat(),
//            SharedPreferenceUtils.targetDaily.toFloat()
//        )
        onClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onClick() {
        binding.btnReminder.setOnSafeClickListener {
            addFragment(ReminderFragment.instance())
        }
        binding.btnProfile.setOnSafeClickListener {
            addFragment(ProfileFragment.instance())
        }
        binding.btnChangePassword.setOnSafeClickListener {
            addFragment(ChangePasswordFragment.instance())
        }
        binding.btnLogout.setOnSafeClickListener {
            signOut()
        }
        binding.ctnVocabulary.setOnSafeClickListener {
            addFragment(VocabularyFragment.instance())
        }
        binding.btnFeedback.setOnSafeClickListener {
            feedback()
        }
        binding.btnQuestionLearned.setOnSafeClickListener {
            addFragment(LessonResultFragment.instance())
        }
        binding.btnDailyGoals.setOnSafeClickListener {
            addFragment(DailyGoalsFragment.instance(SharedPreferenceUtils.targetDaily))
        }
    }

    private fun feedback() {
        try {
            val uriText = String.format(
                "mailto:%s?subject=%s&body=%s",
                "duydoan0702@gmail.com",
                "Feedback from " + getString(R.string.app_name),
                ""
            )
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriText))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        // Sign out khỏi Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut().addOnCompleteListener {
            // Xoá dữ liệu local sau khi đã sign out cả Firebase và Google
            SharedPreferenceUtils.emailLogin = null
            SharedPreferenceUtils.keyUserLogin = null

            replaceFragment(SignInFragment.instance())
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }
}
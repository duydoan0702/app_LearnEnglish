package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ConfirmOtpFragmentBinding
import papaya.`in`.sendmail.SendMail
import kotlin.random.Random
import kotlin.random.nextInt

class ConfirmOtpFragment : BaseFragment<ConfirmOtpFragmentBinding>() {
    companion object {
        val USER_NAME = "USER_NAME"
        val USER_EMAIL = "USER_EMAIL"
        val USER_PASS = "USER_PASS"
        fun instance(name: String, email: String, pass: String): ConfirmOtpFragment {
            Bundle().apply {
                putString(USER_NAME, name)
                putString(USER_EMAIL, email)
                putString(USER_PASS, pass)
                return newInstance(ConfirmOtpFragment::class.java, this)
            }
        }
    }

    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.USER)
    private lateinit var auth: FirebaseAuth

    private var userName = ""
    private var userEmail = ""
    private var userPass = ""
    var random: Int = 0

    override fun initView() {
        arguments?.let {
            userName = it.getString(USER_NAME).toString()
            userEmail = it.getString(USER_EMAIL).toString()
            userPass = it.getString(USER_PASS).toString()
        }
        auth = FirebaseAuth.getInstance()

        random()

        binding.showEmail.text = userEmail

        binding.tvResend.setOnClickListener {
            random()
        }

        binding.ivClose.setOnClickListener {
            closeFragment(this)
        }

        binding.otp6.doOnTextChanged { _, _, _, _ ->
            binding.button.setOnClickListener {
                binding.layoutLoading.makeVisible()
                var otp = binding.otp6.text.toString().trim()
                if (binding.otp6.text.toString().isEmpty()) {
                    requireContext().showToast("Enter OTP")
                } else if (otp != random.toString()) {
                    requireContext().showToast("Wrong OTP")
                } else {
                    auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            createUserRealtime(userName, userEmail, userPass)
                        } else {
                            requireContext().showToast(it.exception?.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun createUserRealtime(name: String, email: String, password: String) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.USER)
        val user = User(email = email, name = name, pass = password)
        val keyUser = System.currentTimeMillis().toString()
        ref.child(keyUser).setValue(user).addOnCompleteListener {
            SharedPreferenceUtils.keyUserLogin = keyUser
            SharedPreferenceUtils.emailLogin = email
            replaceFragment(BottomNavigationFragment.instance())
        }
        binding.layoutLoading.makeGone()

    }

    private fun random() {
        random = Random.nextInt(100000..999999)
        var mail = SendMail(
            "doanduy0702@gmail.com", "tcxubgevdpcmyzzt", userEmail, "Login Signup app's OTP",
            "Your OTP for account authentication is : $random"
        )
        mail.execute()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ConfirmOtpFragmentBinding {
        return ConfirmOtpFragmentBinding.inflate(inflater, container, false)
    }
}
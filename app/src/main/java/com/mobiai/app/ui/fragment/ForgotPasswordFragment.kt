package com.mobiai.app.ui.fragment

import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ForgotPasswordFragmentBinding

class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding>() {

    companion object {
        fun instance(): ForgotPasswordFragment {
            return newInstance(ForgotPasswordFragment::class.java)
        }
    }

    override fun initView() {
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnSendEmail.setOnClickListener {
            if (isValidSignInDetails()) {
                val user = FirebaseAuth.getInstance()
                user.sendPasswordResetEmail(binding.inputEmail.text.toString().trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "send success", Toast.LENGTH_SHORT)
                                .show()
                            closeFragment(this)
                        }
                    }
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString().trim())
                .matches()
        ) {
            showToast("Enter valid email")
            false
        } else {
            true
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ForgotPasswordFragmentBinding {
        return ForgotPasswordFragmentBinding.inflate(inflater, container, false)
    }
}
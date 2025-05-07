package com.mobiai.app.ui.fragment

import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App.Companion.USER
import com.mobiai.app.model.User
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.SignUpFragmentBinding

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    companion object {
        fun instance(): SignUpFragment {
            return newInstance(SignUpFragment::class.java)
        }
    }

    private var isShowPass = false
    private var isShowPassEnter = false

    override fun initView() {
        binding.tvSignIn.setOnClickListener {
            replaceFragment(SignInFragment.instance())
        }

        binding.btnSignUp.setOnClickListener {
            if (isValidSignInDetails()) {
                //create auth
                createUser()
            }
        }

        binding.icEyePass.setOnClickListener {
            val cursorPosition: Int = binding.inputPass.selectionEnd // Lưu vị trí con trỏ hiện tại
            if (!isShowPass) {
                binding.icEyePass.setImageResource(R.drawable.ic_eye_on)
                binding.inputPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyePass.setImageResource(R.drawable.ic_eye_off)
                binding.inputPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputPass.setSelection(cursorPosition)
            isShowPass = !isShowPass
        }

        binding.icEyeEnterPass.setOnClickListener {
            val cursorPosition: Int =
                binding.inputEnterPass.selectionEnd // Lưu vị trí con trỏ hiện tại
            if (!isShowPassEnter) {
                binding.icEyeEnterPass.setImageResource(R.drawable.ic_eye_on)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyeEnterPass.setImageResource(R.drawable.ic_eye_off)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputEnterPass.setSelection(cursorPosition)
            isShowPassEnter = !isShowPassEnter
        }

    }

    private fun getUserRealtime(): Boolean {
        var isExist = true
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(USER)
        val email = binding.inputEmail.text.toString().trim()

        // Read from the database
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == email) {
                            isExist = false
                        }
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                isExist = false
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
        return isExist
    }

    private fun createUser() {
        binding.layoutLoading.makeVisible()
        val name = binding.inputFistName.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val pass = binding.inputPass.text.toString().trim()
        if (getUserRealtime()) {
            replaceFragment(ConfirmOtpFragment.instance(name, email, pass))
            /*auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    task ->
                run {
                    if (task.isSuccessful) {
                        createUserRealtime(email,pass)
                    }
                }
            }*/
        } else {
            showToast("your email exists !")
            binding.layoutLoading.makeGone()
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
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Valid password")
            false
        } else if (binding.inputPass.text.toString().trim().length < 8) {
            showToast("Password must be 8 characters or more")
            false
        } else if (binding.inputEnterPass.text.toString().trim().length < 8) {
            showToast("Enter Password must be 8 characters or more")
            false
        } else if (binding.inputEnterPass.text.toString().trim().isEmpty()) {
            showToast("Valid Enter password ")
            false
        } else if (binding.inputEnterPass.text.toString()
                .trim() != binding.inputPass.text.toString().trim()
        ) {
            showToast("Confirm the password needs to match your password")
            false
        } else if (binding.inputFistName.text.toString().trim().isEmpty()) {
            showToast("Valid fistName")
            false
        } else {
            true
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container, false)
    }
}
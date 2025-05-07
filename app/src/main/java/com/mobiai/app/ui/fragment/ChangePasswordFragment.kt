package com.mobiai.app.ui.fragment

import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.ChangePasswordFragmentBinding


class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding>() {

    companion object {
        fun instance(): ChangePasswordFragment {
            return newInstance(ChangePasswordFragment::class.java)
        }
    }

    val TAG = ChangePasswordFragment::class.java.name
    private lateinit var auth: FirebaseAuth

    private var isShowPassNew = false
    private var isShowPassEnter = false
    override fun initView() {
        auth = FirebaseAuth.getInstance()

        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            changePass()
        }

        binding.icEyeOffNewPass.setOnClickListener {
            val cursorPosition: Int =
                binding.inputNewPass.selectionEnd // Lưu vị trí con trỏ hiện tại
            if (!isShowPassNew) {
                binding.icEyeOffNewPass.setImageResource(R.drawable.ic_eye_on)
                binding.inputNewPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyeOffNewPass.setImageResource(R.drawable.ic_eye_off)
                binding.inputNewPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputNewPass.setSelection(cursorPosition)
            isShowPassNew = !isShowPassNew

        }

        binding.icEyeOffEnterPass.setOnClickListener {
            val cursorPosition: Int =
                binding.inputEnterPass.selectionEnd // Lưu vị trí con trỏ hiện tại

            if (!isShowPassEnter) {
                binding.icEyeOffEnterPass.setImageResource(R.drawable.ic_eye_on)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyeOffEnterPass.setImageResource(R.drawable.ic_eye_off)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputEnterPass.setSelection(cursorPosition)
            isShowPassEnter = !isShowPassEnter

        }

    }

    private fun changePass() {
        binding.frLoading.visible()
        val passwordOld = binding.inputOldPass.text.toString().trim()
        val passwordNew = binding.inputNewPass.text.toString().trim()
        if (isValidSignInDetails()) {
            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(App.USER)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        // Lấy dữ liệu từ mỗi child node và chuyển đổi thành đối tượng User
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.email == SharedPreferenceUtils.emailLogin && user.pass == passwordOld) {
                                auth.currentUser?.updatePassword(passwordNew)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val userUpdate = User(
                                                SharedPreferenceUtils.emailLogin!!,
                                                user.name,
                                                passwordNew
                                            )
                                            userSnapshot.key?.let {
                                                ref.child(it).setValue(userUpdate)
                                            }
                                            showToast("change password success!")
                                            handlerBackPressed()
                                        } else {
                                            showToast("change password not success!")
                                        }
                                    }
                                binding.frLoading.gone()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException())
                    showToast("Failed to read value!")
                    binding.frLoading.gone()
                }
            })
        } else {
            binding.frLoading.gone()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputOldPass.text.toString().trim().isEmpty()) {
            showToast("Enter Old Pass")
            false
        } else if (binding.inputNewPass.text.toString().trim().isEmpty()) {
            showToast("Enter New password")
            false
        } else if (binding.inputEnterPass.text.toString().trim().isEmpty()) {
            showToast("Enter New password")
            false
        } else if (binding.inputEnterPass.text.toString().trim().length < 8) {
            showToast("Enter password length size must > 8")
            false
        } else if (binding.inputNewPass.text.toString().trim().length < 8) {
            showToast("New password length size must > 8")
            false

        } else if (binding.inputNewPass.text.toString()
                .trim() != binding.inputEnterPass.text.toString().trim()
        ) {
            showToast("Enter not same New password")
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
    ): ChangePasswordFragmentBinding {
        return ChangePasswordFragmentBinding.inflate(inflater, container, false)
    }
}
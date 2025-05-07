package com.mobiai.app.ui.fragment

import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.LoginFragmentBinding


class SignInFragment : BaseFragment<LoginFragmentBinding>() {

    companion object {
        fun instance(): SignInFragment {
            return newInstance(SignInFragment::class.java)
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var isShowPass = false
    override fun initView() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnSignInForGoogle.setOnSafeClickListener {
            googleSignIn()
        }

        binding.tvSignUp.setOnClickListener {
            replaceFragment(SignUpFragment.instance())
        }

        binding.tvForgotPass.setOnClickListener {
            addFragment(ForgotPasswordFragment.instance())
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

        binding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
                signInAuth()
            }
        }
    }

    private fun googleSignIn() {
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, 1511)
    }

    private fun signInAuth() {
        binding.layoutLoading.makeVisible()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPass.text.toString().trim()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        signIn(email, password)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    requireContext().showToast("Login Authentication failed.!!!")

                    binding.layoutLoading.makeGone()
                }
            }
            .addOnCanceledListener {
                binding.layoutLoading.makeGone()
            }
    }

    private fun signIn(email: String, password: String) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.USER)
        // vult@its-global.vn
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == email) {
                            if (user.pass != password) {
                                val userUpdate = User(user.email, user.name, password)
                                userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                            }
                            SharedPreferenceUtils.keyUserLogin = userSnapshot.key
                            SharedPreferenceUtils.emailLogin = user.email
                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    addFragment(BottomNavigationFragment.instance())
                                    binding.layoutLoading.makeGone()
                                },1000)

                        }
                    }
                }

                if (SharedPreferenceUtils.emailLogin == null) {
                    requireContext().showToast("Login Fail!!!")
                    binding.layoutLoading.makeGone()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.layoutLoading.makeGone()
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

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
        } else if (binding.inputPass.text.toString().trim().length < 8) {
            showToast("Password must >= 8 size")
            false
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Enter password")
            false
        } else {
            true
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }
}
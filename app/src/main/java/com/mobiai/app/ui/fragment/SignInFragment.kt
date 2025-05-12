package com.mobiai.app.ui.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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
            val cursorPosition: Int = binding.inputPass.selectionEnd
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
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1511)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1511) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
                requireContext().showToast("Google sign-in failed.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val ref = database.getReference(App.USER)
                        val userUpdate = User(user.email ?: "", user.displayName ?: "", "")
                        ref.child(user.uid).setValue(userUpdate)

                        SharedPreferenceUtils.keyUserLogin = user.uid
                        SharedPreferenceUtils.emailLogin = user.email

                        addFragment(BottomNavigationFragment.instance())
                    }
                } else {
                    requireContext().showToast("Firebase authentication failed.")
                }
            }
    }

    // ✅ Đăng nhập bằng Email/Password Firebase
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
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null && user.email == email) {
                        if (user.pass != password) {
                            val userUpdate = User(user.email, user.name, password)
                            userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                        }
                        SharedPreferenceUtils.keyUserLogin = userSnapshot.key
                        SharedPreferenceUtils.emailLogin = user.email

                        Handler(Looper.getMainLooper()).postDelayed({
                            addFragment(BottomNavigationFragment.instance())
                            binding.layoutLoading.makeGone()
                        }, 1000)
                        return
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

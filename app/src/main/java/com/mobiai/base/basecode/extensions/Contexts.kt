package com.mobiai.base.basecode.extensions

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.model.User
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.extensions.LogD


fun Context.getInfoUser(getDataFromFirebase: GetDataFromFirebase) {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.USER)
    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (userSnapshot in dataSnapshot.children) {
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    if (user.email == SharedPreferenceUtils.emailLogin) {
                         getDataFromFirebase.getDataSuccess(user)
                        break
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            getDataFromFirebase.getDataFail(error.message)
        }
    })
}

fun Context.getListUser(getDataFromFirebase: GetDataFromFirebase) {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.USER)
    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (userSnapshot in dataSnapshot.children) {
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    getDataFromFirebase.getDataSuccess(user)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            getDataFromFirebase.getDataFail(error.message)
        }
    })
}

fun Context.showToast(string: String){
    Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
}

interface GetDataFromFirebase{
    fun getDataSuccess(user:User)
    fun getDataFail(err: String)
}


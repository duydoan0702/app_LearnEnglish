package com.mobiai.base.basecode.extensions

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.model.Gift
import com.mobiai.app.model.User
import com.mobiai.app.ui.fragment.GiftFragment
import com.mobiai.base.basecode.storage.SharedPreferenceUtils

fun Any.LogD(string: String){
    Log.d("abcd : ${this::class.java.simpleName}","$string")
}

fun Any.getInfoList(nameTable: String,getListDataFromFirebase: GetListDataFromFirebase<Any>) {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(nameTable)
    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val list = arrayListOf<Any>()
            if(dataSnapshot.exists())
            for (i in dataSnapshot.children) {
                list.add(i)
            }
                getListDataFromFirebase.getListDataSuccess(list)
            }

        override fun onCancelled(error: DatabaseError) {
                getListDataFromFirebase.getDataFail(error.message)
        }

    })
}
interface GetListDataFromFirebase<T:Any>{
    fun getListDataSuccess(list: List<T>)
    fun getDataFail(err: String)
}
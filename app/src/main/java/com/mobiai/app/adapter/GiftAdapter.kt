package com.mobiai.app.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.Gift
import com.mobiai.app.model.User
import com.mobiai.app.ui.dialog.SuccessGiftDialog
import com.mobiai.app.utils.Announcer
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.extensions.GetDataFromFirebase
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.ItemGiftBinding


class GiftAdapter(val context: Context) : BaseAdapter<Gift,ItemGiftBinding>() {
    private var successGiftDialog: SuccessGiftDialog? = null

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemGiftBinding {
        return ItemGiftBinding.inflate(inflater,parent,false)
    }
    override fun bind(binding: ItemGiftBinding, item: Gift, position: Int) {
        val announcer = Announcer(context)
        announcer.initTTS(context)
        binding.txtTitle.text = item.title
        binding.txtDescription.text = if(item.description.length < 80) item.description else "${item.description.take(80)}..."
        binding.txtRuby.text = item.ruby.toString()
        Glide.with(context).load(item.imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.image)

        binding.btnReceive.setOnClickListener {
            successGiftDialog = SuccessGiftDialog(context)
            successGiftDialog!!.show()

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.giftUrl))
            context.startActivity(browserIntent)

            listItem.remove(item)
            notifyDataSetChanged()
            update(ruby = item.ruby, object : GetDataFromFirebase{
                override fun getDataSuccess(user: User) {
                }

                override fun getDataFail(err: String) {
                    context.showToast(err)
                }

            })

        }
        getRubyCurrent(item,binding)
    }
    fun getRubyCurrent(item: Gift,binding: ItemGiftBinding){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(App.USER)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin) {
                            if(user.ruby < item.ruby){
                                binding.btnReceive.background = ContextCompat.getDrawable(context, R.drawable.bg_disable_receive_gift)
                                binding.btnReceive.setOnClickListener(null)
                            }
                            else
                                binding.btnReceive.background = ContextCompat.getDrawable(context, R.drawable.bg_enable_receive_gift)
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
    fun update(ruby: Int, getDataFromFirebase: GetDataFromFirebase){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.USER)
        var i = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    var user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == SharedPreferenceUtils.emailLogin && i <1) {
                            val newRuby = user.ruby - ruby
                            val userUpdate = User(user.email,user.name,user.pass,user.urlImage,newRuby,user.totalXp)
                            getDataFromFirebase.getDataSuccess(userUpdate)
                            userSnapshot.key?.let { ref.child(it).setValue(userUpdate) }
                            i++
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
}
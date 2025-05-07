package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.GiftAdapter
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.model.Gift
import com.mobiai.app.model.User
import com.mobiai.app.utils.gone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.extensions.GetDataFromFirebase
import com.mobiai.base.basecode.extensions.getInfoUser
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentGiftBinding


class GiftFragment : BaseFragment<FragmentGiftBinding>() {
    companion object {
        fun instance(): GiftFragment {
            return newInstance(GiftFragment::class.java)
        }

        const val IMAGEURL = "imageUrl"
        const val DESCRIPTION = "description"
        const val TITLE = "title"
        const val RUBY = "ruby"
        const val GIFT_URL = "gifturl"
    }

    private var giftAdapter: GiftAdapter? = null
    var listGift: ArrayList<Gift>? = null
    val database = FirebaseDatabase.getInstance()

    override fun initView() {
        initData()
        requireContext().getInfoUser(object : GetDataFromFirebase {
            override fun getDataSuccess(user: User) {
                binding.txtRuby.text = user.ruby.toString()
                binding.txtExperience.text = user.totalXp.toString()
            }

            override fun getDataFail(err: String) {
                requireContext().showToast(err)
            }
        })
    }

    fun initData() {
        binding.layoutLoading.visible()
        listGift = arrayListOf()
        giftAdapter = GiftAdapter(requireContext())
        val myRef = database.getReference(App.GIFT)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listGift?.clear()
                    for (i in dataSnapshot.children) {
                        var gift = Gift()
                        gift.imageUrl = i.child("$IMAGEURL").value.toString()
                        gift.description = i.child("$DESCRIPTION").value.toString()
                        gift.title = i.child("$TITLE").value.toString()
                        gift.ruby = i.child("$RUBY").value.toString().toInt()
                        gift.giftUrl = i.child("$GIFT_URL").value.toString()

                        listGift?.add(gift)
                    }
                    giftAdapter?.setItems(listGift!!)
                    binding.recyclerViewGift.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewGift.addItemDecoration(ItemSpacingDecoration(7, 8))
                    binding.recyclerViewGift.adapter = giftAdapter
                    binding.layoutLoading.gone()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Err: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater, container, false)
    }
}
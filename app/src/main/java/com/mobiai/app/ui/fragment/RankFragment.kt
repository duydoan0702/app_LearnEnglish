package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.adapter.RankAdapter
import com.mobiai.app.model.User
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentRankBinding

class RankFragment : BaseFragment<FragmentRankBinding>() {
    companion object {
        fun instance(): RankFragment {
            return newInstance(RankFragment::class.java)
        }
    }

    private var rankAdapter: RankAdapter? = null
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.USER)
    override fun initView() {
        getRank()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRankBinding {
        return FragmentRankBinding.inflate(inflater, container, false)
    }

    private fun getRank() {

        var listRankUser: ArrayList<User> = arrayListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listRankUser.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        listRankUser.add(user)
                    }
                }
                binding.recyclerViewRank.addItemDecoration(ItemSpacingDecoration(7, 8))
                listRankUser.sortByDescending { it.totalXp }
                rankAdapter = RankAdapter(requireContext())
                rankAdapter?.setItems(listRankUser)
                binding.recyclerViewRank.adapter = rankAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                requireContext().showToast(error.message)
            }
        })
    }
}
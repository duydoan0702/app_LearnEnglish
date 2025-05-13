package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import android.view.View

import com.mobiai.R

class GrammarFragment : Fragment() {

    companion object {
        fun instance(): GrammarFragment {
            return GrammarFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false)
    }
}

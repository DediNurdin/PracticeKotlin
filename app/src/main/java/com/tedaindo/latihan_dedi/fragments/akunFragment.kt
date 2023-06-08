package com.tedaindo.latihan_dedi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tedaindo.latihan_dedi.R

class akunFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_akun, container, false)
        initUI(view)
        setupActions()

        return view
    }

    private fun setupActions() {
    }

    private fun initUI(view: View?) {

    }

}
package com.tedaindo.latihan_dedi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedaindo.latihan_dedi.R

class SocialMediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_social_media, container, false)
        initUI(view)
        setupActions()

        return view
    }

    private fun setupActions() {
    }

    private fun initUI(view: View?) {

    }
}
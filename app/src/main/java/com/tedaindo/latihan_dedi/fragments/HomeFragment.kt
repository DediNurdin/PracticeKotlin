package com.tedaindo.latihan_dedi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tedaindo.latihan_dedi.R
import com.tedaindo.latihan_dedi.activities.LatihanDuaActivity
import com.tedaindo.latihan_dedi.activities.LatihanEmpatActivity
import com.tedaindo.latihan_dedi.activities.LatihanTigaActivity
import com.tedaindo.latihan_dedi.activities.MainActivity
import com.tedaindo.latihan_dedi.helpers.Utils

class HomeFragment : Fragment() {
    private lateinit var btnLat1: Button
    private lateinit var btnLat2: Button
    private lateinit var btnLat3: Button
    private lateinit var btnLat4: Button
    private lateinit var tvHari: TextView
    private lateinit var tcJam: TextClock

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initUI(view)
        setupActions()

        return view
    }

    private fun setupActions() {
        btnLat1.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        btnLat2.setOnClickListener {
            val intent = Intent(requireContext(), LatihanDuaActivity::class.java)
            startActivity(intent)
        }
        btnLat3.setOnClickListener {
            val intent = Intent(requireContext(), LatihanTigaActivity::class.java)
            startActivity(intent)
        }
        btnLat4.setOnClickListener {
            val intent = Intent(requireContext(), LatihanEmpatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initUI(view: View) {
        btnLat1 = view.findViewById(R.id.latihan_1)
        btnLat2 = view.findViewById(R.id.latihan_2)
        btnLat3 = view.findViewById(R.id.latihan_3)
        btnLat4 = view.findViewById(R.id.latihan_4)
        tvHari = view.findViewById(R.id.tv_hari)
        tcJam = view.findViewById(R.id.tc_jam)
        tvHari.text = Utils.getCurrentDateDashboard()
        tcJam.format24Hour

    }
}
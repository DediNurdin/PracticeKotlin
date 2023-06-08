package com.tedaindo.latihan_dedi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tedaindo.latihan_dedi.R
import com.tedaindo.latihan_dedi.adapters.TabInformationAdapter

class LainFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lain, container, false)
        initUI(view)
        setupActions()

        return view
    }

    private fun setupActions() {
    }

    private fun initUI(view: View?) {
        view?.let {
            tabLayout = it.findViewById(R.id.tl_fg_information)
            viewPager = it.findViewById(R.id.vp_fg_information)

            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_tautan)).setIcon(R.drawable.ic_awesome))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_video)).setIcon(R.drawable.ic_video))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_website)).setIcon(R.drawable.ic_web))

            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            val adapter =
                TabInformationAdapter(requireContext(), childFragmentManager, tabLayout.tabCount)
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
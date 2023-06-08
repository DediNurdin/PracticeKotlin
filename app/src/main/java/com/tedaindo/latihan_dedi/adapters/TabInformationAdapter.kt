package com.tedaindo.latihan_dedi.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tedaindo.latihan_dedi.fragments.SocialMediaFragment
import com.tedaindo.latihan_dedi.fragments.WebFragment
import com.tedaindo.latihan_dedi.fragments.YoutubeFragment

@Suppress("DEPRECATION")
internal class TabInformationAdapter(
    var context: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SocialMediaFragment()
            }
            1 -> {
                YoutubeFragment()
            }
            2 -> {
                WebFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}

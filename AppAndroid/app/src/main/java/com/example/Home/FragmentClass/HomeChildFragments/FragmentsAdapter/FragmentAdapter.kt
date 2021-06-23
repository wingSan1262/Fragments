package com.example.Home.FragmentClass.HomeChildFragments.FragmentsAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.example.Home.FragmentClass.HomeChildFragments.HomeElectricityFragment
import com.example.Home.FragmentClass.HomeChildFragments.HomeVideoFragment
import com.example.Home.FragmentClass.HomeChildFragments.HomeWaterFragment


class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return HomeElectricityFragment() //ChildFragment1 at position 0
            1 -> return HomeVideoFragment() //ChildFragment2 at position 1
            2 -> return HomeWaterFragment() //ChildFragment3 at position 2
            else -> return HomeElectricityFragment()
        }
    }

    override fun getCount(): Int {
        return 3 //three fragments
    }
}

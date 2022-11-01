package com.hibisco.kitsune.feature.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hibisco.kitsune.feature.ui.faq.view.FaqFragment
import com.hibisco.kitsune.feature.ui.history.view.HistoryFragment
import com.hibisco.kitsune.feature.ui.map.view.FragmentMapKitsune
import com.hibisco.kitsune.feature.ui.profile.view.ProfileFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentMapKitsune()
            1 -> HistoryFragment()
            2 -> FaqFragment()
            else -> ProfileFragment()
        }
    }
}
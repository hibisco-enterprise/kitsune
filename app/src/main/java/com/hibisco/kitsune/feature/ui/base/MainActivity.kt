package com.hibisco.kitsune.feature.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.hibisco.kitsune.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.setUserInputEnabled(false)
        setupTabbar()

        val shouldOpenModal: Boolean = intent.getBooleanExtra("shouldOpenModal", false)
        if (shouldOpenModal) {
            openModal()
        }
    }

    private fun openModal() {
        Toast.makeText(baseContext, "Agendamento concluido!", Toast.LENGTH_LONG).show()
    }

    private fun setupTabbar() {
        val adapter = TabPageAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))

                if (position == 0) {
                    binding.viewPager.setUserInputEnabled(false)
                } else {
                    binding.viewPager.setUserInputEnabled(true)
                }
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position

                if (tab.position == 0) {
                    binding.viewPager.setUserInputEnabled(false)
                } else {
                    binding.viewPager.setUserInputEnabled(true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    override fun onBackPressed() {
        Log.d("CDA", "onBackPressed Called")
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }

}
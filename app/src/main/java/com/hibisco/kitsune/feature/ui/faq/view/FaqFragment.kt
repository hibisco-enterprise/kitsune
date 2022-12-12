package com.hibisco.kitsune.feature.ui.faq.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hibisco.kitsune.databinding.ActivityFaqBinding

class FaqFragment : Fragment() {

    private var _binding: ActivityFaqBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityFaqBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.question1.setOnClickListener {
            toggleAnswer(binding.answer1)
        }
        binding.question2.setOnClickListener {
            toggleAnswer(binding.answer2)
        }
        binding.question3.setOnClickListener {
            toggleAnswer(binding.answer3)
        }
        binding.question4.setOnClickListener {
            toggleAnswer(binding.answer4)
        }
        binding.question5.setOnClickListener {
            toggleAnswer(binding.answer5)
        }
        binding.question6.setOnClickListener {
            toggleAnswer(binding.answer6)
        }
        binding.question7.setOnClickListener {
            toggleAnswer(binding.answer7)
        }
        binding.question8.setOnClickListener {
            toggleAnswer(binding.answer8)
        }
        binding.question9.setOnClickListener {
            toggleAnswer(binding.answer9)
        }
        binding.question10.setOnClickListener {
            toggleAnswer(binding.answer10)
        }

        return binding.root
    }

    private fun toggleAnswer(answer: View){
        answer.visibility = if (answer.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}
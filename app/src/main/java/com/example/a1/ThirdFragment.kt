package com.example.a1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.a1.databinding.FragmentThirdBinding

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)

        // Observe the history list
        viewModel.userInputList.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.tvThirdDisplay.text = "No history yet"
            } else {
                // Display the entire history
                val historyText = buildString {
                    append("History (${historyList.size} items):\n\n")
                    historyList.forEachIndexed { index, item ->
                        append("${index + 1}. $item\n")
                    }
                }
                binding.tvThirdDisplay.text = historyText
            }
        }

        binding.btnGoToFirst.setOnClickListener {
            findNavController().navigate(
                R.id.action_thirdFragment_to_firstFragment
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
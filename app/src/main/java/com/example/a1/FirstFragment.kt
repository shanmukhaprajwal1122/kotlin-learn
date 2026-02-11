package com.example.a1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.a1.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.btnGoToSecond.setOnClickListener {
            val userInput = binding.edtInput.text.toString()

            if (userInput.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addUserInput(userInput)
            binding.edtInput.text.clear()

            Toast.makeText(requireContext(), "Added: $userInput", Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_firstFragment_to_secondFragment
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
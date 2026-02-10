package com.example.a1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a1.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val receivedInput = arguments?.getString(ARG_INPUT) ?: "No data received"

        binding.txtDisplay.text = receivedInput

        binding.btnGoToThird.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ThirdFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_INPUT = "user_input"

        fun newInstance(userInput: String): SecondFragment {
            val fragment = SecondFragment()
            val args = Bundle().apply {
                putString(ARG_INPUT, userInput)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
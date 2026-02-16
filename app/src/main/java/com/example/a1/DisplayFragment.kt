package com.example.a1

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1.databinding.FragmentDisplayBinding

class DisplayFragment : Fragment() {

    private var _binding: FragmentDisplayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    private val historyAdapter = HistoryAdapter(
        onEditClick = { position, currentText ->
            showEditDialog(position, currentText)
        },
        onDeleteClick = { position ->
            showDeleteConfirmation(position)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayBinding.inflate(inflater, container, false)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        // Observe the history list
        viewModel.userInputList.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.txtEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.txtEmpty.visibility = View.GONE
                // Still use updateList for full list updates
                historyAdapter.updateList(historyList)
            }
        }

        binding.btnClearHistory.setOnClickListener {
            showClearConfirmation()
        }

        binding.btnBackToInput.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun showEditDialog(position: Int, currentText: String) {
        val editText = EditText(requireContext()).apply {
            setText(currentText)
            setSelection(currentText.length)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Item")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                val newText = editText.text.toString()
                if (newText.isNotEmpty()) {
                    viewModel.editUserInput(position, newText)
                    // ✅ Optimized: Update only this item in adapter
                    historyAdapter.updateItem(position, newText)
                    Toast.makeText(requireContext(), "Item updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Text cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteConfirmation(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { dialog, _ ->
                viewModel.deleteUserInput(position)
                // ✅ Optimized: Remove only this item in adapter
                historyAdapter.removeItem(position)
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showClearConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All History")
            .setMessage("Are you sure you want to delete all items?")
            .setPositiveButton("Clear All") { dialog, _ ->
                viewModel.clearHistory()
                // ✅ Optimized: Clear all at once
                historyAdapter.clearAll()
                Toast.makeText(requireContext(), "All items cleared", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
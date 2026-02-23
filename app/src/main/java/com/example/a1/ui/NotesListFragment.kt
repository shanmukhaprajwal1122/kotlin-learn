package com.example.a1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1.NotesListFragmentDirections
import com.example.a1.R
import com.example.a1.data.Note
import com.example.a1.databinding.FragmentNotesListBinding
import com.example.a1.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    private val notesAdapter = NotesAdapter(
        onNoteClick = { note ->
            // Click on card - open for editing
            val action = NotesListFragmentDirections
                .actionNotesListFragmentToNoteEditorFragment(note.id)
            findNavController().navigate(action)
        },
        onEditClick = { note ->
            // Edit button - open for editing
            val action = NotesListFragmentDirections
                .actionNotesListFragmentToNoteEditorFragment(note.id)
            findNavController().navigate(action)
        },
        onDeleteClick = { note ->
            // Delete button - show confirmation
            showDeleteDialog(note)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFAB()
        observeNotes()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }
    }

    private fun setupFAB() {
        binding.fabAddNote.setOnClickListener {
            // Navigate to editor with no noteId (new note)
            val action = NotesListFragmentDirections
                .actionNotesListFragmentToNoteEditorFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeNotes() {
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                notesAdapter.updateNotes(notes)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete_all -> {
                    showDeleteAllDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDeleteDialog(note: Note) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete \"${note.title}\"?")
            .setPositiveButton("Delete") { dialog, _ ->
                viewModel.deleteNote(note.id)
                showSuccessDialog("Note deleted successfully")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteAllDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete All Notes")
            .setMessage("Are you sure you want to delete all notes? This action cannot be undone.")
            .setPositiveButton("Delete All") { dialog, _ ->
                viewModel.clearAllNotes()
                showSuccessDialog("All notes deleted")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


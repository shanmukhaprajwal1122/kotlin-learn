package com.example.a1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.a1.NoteEditorFragmentArgs
import com.example.a1.R
import com.example.a1.data.Note
import com.example.a1.databinding.FragmentNoteEditorBinding
import com.example.a1.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteEditorFragment : Fragment() {

    private var _binding: FragmentNoteEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()
    private val args: NoteEditorFragmentArgs by navArgs()

    private var currentNote: Note? = null
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadNote()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            handleBackPress()
        }
    }

    private fun loadNote() {
        args.noteId?.let { noteId ->
            // Edit mode
            isEditMode = true
            binding.toolbar.title = "Edit Note"

            // Load note asynchronously from database
            viewModel.getNoteById(noteId) { note ->
                currentNote = note
                note?.let {
                    binding.edtTitle.setText(it.title)
                    binding.edtContent.setText(it.content)
                }
            }
        } ?: run {
            // New note mode
            isEditMode = false
            binding.toolbar.title = "New Note"
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.edtTitle.text.toString().trim()
        val content = binding.edtContent.text.toString().trim()

        if (content.isEmpty()) {
            showErrorDialog("Content cannot be empty")
            return
        }

        if (isEditMode && currentNote != null) {
            // Update existing note
            viewModel.updateNote(currentNote!!.id, title, content)
            showSuccessDialog("Note updated successfully") {
                findNavController().popBackStack()
            }
        } else {
            // Create new note
            viewModel.addNote(title, content)
            showSuccessDialog("Note created successfully") {
                findNavController().popBackStack()
            }
        }
    }

    private fun handleBackPress() {
        val title = binding.edtTitle.text.toString().trim()
        val content = binding.edtContent.text.toString().trim()

        // Check if there are unsaved changes
        if (hasUnsavedChanges(title, content)) {
            showDiscardDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun hasUnsavedChanges(title: String, content: String): Boolean {
        return if (isEditMode && currentNote != null) {
            // Check if anything changed
            title != currentNote!!.title || content != currentNote!!.content
        } else {
            // New note - check if user typed anything
            title.isNotEmpty() || content.isNotEmpty()
        }
    }

    private fun showDiscardDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Discard Changes")
            .setMessage("You have unsaved changes. Do you want to discard them?")
            .setPositiveButton("Discard") { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack()
            }
            .setNegativeButton("Keep Editing") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog(message: String, onDismiss: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onDismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


package com.example.textreader.ui.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.textreader.databinding.FragmentNoteEditorBinding
import com.example.textreader.ui.components.TextFormattingBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteEditorFragment : Fragment() {
    private var _binding: FragmentNoteEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditorViewModel by viewModels()
    private var noteId: Long = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFormattingBar()
        setupTextListeners()
        setupClickListeners()
        loadNoteIfEditing()
    }

    private fun setupFormattingBar() {
        binding.formattingBar.onImageInsertListener = {
            // TODO: Implement image insertion
        }

        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let { editable ->
                    val selectionStart = binding.etContent.selectionStart
                    val selectionEnd = binding.etContent.selectionEnd

                    if (selectionStart != selectionEnd) {
                        binding.formattingBar.applyFormatting(editable, selectionStart, selectionEnd)
                    }
                }
            }
        })
    }

    private fun setupTextListeners() {
        binding.etContent.setOnClickListener {
            updateFormattingForSelection()
        }

        binding.etContent.setOnKeyListener { _, _, _ ->
            updateFormattingForSelection()
            false
        }
    }

    private fun updateFormattingForSelection() {
        val selectionStart = binding.etContent.selectionStart
        val selectionEnd = binding.etContent.selectionEnd

        if (selectionStart != selectionEnd) {
            val editable = binding.etContent.text
            val spans = editable.getSpans(selectionStart, selectionEnd, Any::class.java)

            val formatting = TextFormattingBar.TextFormatting(
                isBold = spans.any { it is StyleSpan && it.style == android.graphics.Typeface.BOLD },
                isItalic = spans.any { it is StyleSpan && it.style == android.graphics.Typeface.ITALIC },
                isUnderline = spans.any { it is UnderlineSpan },
                isStrikethrough = spans.any { it is StrikethroughSpan }
            )

            binding.formattingBar.setFormatting(formatting)
        } else {
            binding.formattingBar.clearFormatting()
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveNote()
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShare.setOnClickListener {
            // TODO: Implement share functionality
        }
    }

    private fun loadNoteIfEditing() {
        arguments?.getLong("noteId", -1)?.takeIf { it != -1L }?.let { id ->
            noteId = id
            viewModel.getNoteById(id)?.let { note ->
                binding.etTitle.setText(note.title)
                binding.etContent.setText(note.content)
                // TODO: Load image if exists
            }
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            if (noteId == -1L) {
                viewModel.createNote(title, content)
            } else {
                viewModel.updateNote(noteId, title, content)
            }
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
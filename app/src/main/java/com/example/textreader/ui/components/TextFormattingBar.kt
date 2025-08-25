package com.example.textreader.ui.components

import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.textreader.databinding.ComponentTextFormattingBarBinding

class TextFormattingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ComponentTextFormattingBarBinding

    var onImageInsertListener: (() -> Unit)? = null
    private var isBold = false
    private var isItalic = false
    private var isUnderline = false
    private var isStrikethrough = false

    init {
        binding = ComponentTextFormattingBarBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBold.setOnClickListener { toggleBold() }
        binding.btnItalic.setOnClickListener { toggleItalic() }
        binding.btnUnderline.setOnClickListener { toggleUnderline() }
        binding.btnStrikethrough.setOnClickListener { toggleStrikethrough() }
        binding.btnImage.setOnClickListener { onImageInsertListener?.invoke() }
    }

    fun applyFormatting(editable: Editable, start: Int, end: Int) {
        if (isBold) {
            editable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (isItalic) {
            editable.setSpan(StyleSpan(android.graphics.Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (isUnderline) {
            editable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (isStrikethrough) {
            editable.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    fun clearFormatting() {
        isBold = false
        isItalic = false
        isUnderline = false
        isStrikethrough = false
        updateButtonStates()
    }

    private fun toggleBold() {
        isBold = !isBold
        updateButtonStates()
    }

    private fun toggleItalic() {
        isItalic = !isItalic
        updateButtonStates()
    }

    private fun toggleUnderline() {
        isUnderline = !isUnderline
        updateButtonStates()
    }

    private fun toggleStrikethrough() {
        isStrikethrough = !isStrikethrough
        updateButtonStates()
    }

    private fun updateButtonStates() {
        binding.btnBold.isSelected = isBold
        binding.btnItalic.isSelected = isItalic
        binding.btnUnderline.isSelected = isUnderline
        binding.btnStrikethrough.isSelected = isStrikethrough

        // Изменение цвета для выбранных кнопок
        val selectedColor = context.getColor(android.R.color.holo_blue_light)
        val defaultColor = context.getColor(android.R.color.transparent)

        binding.btnBold.setBackgroundColor(if (isBold) selectedColor else defaultColor)
        binding.btnItalic.setBackgroundColor(if (isItalic) selectedColor else defaultColor)
        binding.btnUnderline.setBackgroundColor(if (isUnderline) selectedColor else defaultColor)
        binding.btnStrikethrough.setBackgroundColor(if (isStrikethrough) selectedColor else defaultColor)
    }

    fun getCurrentFormatting(): TextFormatting {
        return TextFormatting(isBold, isItalic, isUnderline, isStrikethrough)
    }

    fun setFormatting(formatting: TextFormatting) {
        isBold = formatting.isBold
        isItalic = formatting.isItalic
        isUnderline = formatting.isUnderline
        isStrikethrough = formatting.isStrikethrough
        updateButtonStates()
    }

    data class TextFormatting(
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        val isUnderline: Boolean = false,
        val isStrikethrough: Boolean = false
    )
}
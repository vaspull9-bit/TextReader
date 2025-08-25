package com.example.textreader.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDate(date: Date): String {
        val pattern = "dd.MM.yyyy HH:mm"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDateForExport(date: Date): String {
        val pattern = "yyyy-MM-dd_HH-mm-ss"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun getRelativeTimeString(date: Date): String {
        val now = System.currentTimeMillis()
        val difference = now - date.time

        return when {
            difference < 60000 -> "только что"
            difference < 3600000 -> "${difference / 60000} мин назад"
            difference < 86400000 -> "${difference / 3600000} ч назад"
            difference < 604800000 -> "${difference / 86400000} дн назад"
            else -> formatDate(date)
        }
    }
}
package com.example.textreader.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.textreader.data.Note
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream

object ExportUtils {

    fun exportToTxt(context: Context, note: Note): Uri? {
        return try {
            val fileName = "note_${note.id}_${System.currentTimeMillis()}.txt"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            FileOutputStream(file).use { fos ->
                fos.write("${note.title}\n\n${note.content}".toByteArray())
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка экспорта в TXT", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun exportToPdf(context: Context, note: Note): Uri? {
        return try {
            val fileName = "note_${note.id}_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            PdfWriter(file).use { writer ->
                val pdf = PdfDocument(writer)
                val document = Document(pdf)

                document.add(Paragraph(note.title).setBold().setFontSize(18f))
                document.add(Paragraph(note.content).setFontSize(12f))

                document.close()
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка экспорта в PDF", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun exportToExcel(context: Context, note: Note): Uri? {
        return try {
            val fileName = "note_${note.id}_${System.currentTimeMillis()}.xlsx"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Заметка")

            val titleRow = sheet.createRow(0)
            titleRow.createCell(0).setCellValue(note.title)

            val contentRow = sheet.createRow(1)
            contentRow.createCell(0).setCellValue(note.content)

            FileOutputStream(file).use { fos ->
                workbook.write(fos)
            }

            workbook.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка экспорта в Excel", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun exportToWord(context: Context, note: Note): Uri? {
        return try {
            val fileName = "note_${note.id}_${System.currentTimeMillis()}.docx"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            val document = XWPFDocument()
            val titleParagraph = document.createParagraph()
            titleParagraph.createRun().setText(note.title).setBold(true)

            val contentParagraph = document.createParagraph()
            contentParagraph.createRun().setText(note.content)

            FileOutputStream(file).use { fos ->
                document.write(fos)
            }

            document.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка экспорта в Word", Toast.LENGTH_SHORT).show()
            null
        }
    }
}
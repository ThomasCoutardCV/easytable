package org.vandeseer.checkandvisit.pagination

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.IOException
import java.text.MessageFormat

object Pagination {
    @Throws(IOException::class)
    fun addPageNumbers(document: PDDocument, numberingFormat: String?, offset_X: Int, offset_Y: Int) {
        var pageCounter = 1
        for (page in document.pages) {
            val contentStream = PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, false)
            contentStream.setFont(PDType1Font.TIMES_ITALIC, 25f)
            contentStream.beginText()
            val pageSize = page.cropBox
            val x = pageSize.lowerLeftX
            val y = pageSize.lowerLeftY
            contentStream.newLineAtOffset(x + pageSize.width - offset_X, y + offset_Y)
            val text = MessageFormat.format(numberingFormat, pageCounter)
            contentStream.showText(text)
            contentStream.endText()
            contentStream.close()
            ++pageCounter
        }
    }
}
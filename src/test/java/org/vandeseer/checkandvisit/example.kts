package org.vandeseer.checkandvisit

import org.apache.pdfbox.io.IOUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.settings.HorizontalAlignment
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import org.vandeseer.easytable.structure.cell.ImageCell
import org.vandeseer.easytable.structure.cell.TextCell
import java.awt.Color
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.vandeseer.easytable.RepeatedHeaderTableDrawer

import org.vandeseer.easytable.settings.VerticalAlignment
import org.vandeseer.easytable.structure.Table.TableBuilder
import org.vandeseer.easytable.structure.cell.AbstractCell
import rst.pdfbox.layout.text.Alignment

import java.util.Objects

val document = PDDocument()

fun createPictureCell(colSpan : Int): AbstractCell {
    // Load custom image
    val pictureBytes = IOUtils.toByteArray(Objects.requireNonNull(this.javaClass.classLoader.getResourceAsStream("tux.png")))
    val picture = PDImageXObject.createFromByteArray(document, pictureBytes, "picture")
    return ImageCell.builder().image(picture).colSpan(colSpan)
        .verticalAlignment(VerticalAlignment.MIDDLE)
        .horizontalAlignment(HorizontalAlignment.CENTER)
        .build()
}

fun createBadgeCell(): AbstractCell? {
    // Load custom image
    val badgeBytes = IOUtils.toByteArray(Objects.requireNonNull(this.javaClass.classLoader.getResourceAsStream("badge.png")))
    val badge = PDImageXObject.createFromByteArray(document, badgeBytes, "badge")
    return ImageCell.builder().image(badge).build()
}

fun createPocLabelRow(): Row {
    return Row.builder().textColor(Color.WHITE).borderWidth(0.2F).backgroundColor(cvColor)
        .add(TextCell.builder().text("Élément").build())
        .add(TextCell.builder().text("État").build())
        .add(TextCell.builder().text("Fonctionnement").build())
        .add(TextCell.builder().text("Propreté").build())
        .add(TextCell.builder().text("Nb").build())
        .add(TextCell.builder().text("Commentaire / Dégradations constatées").build())
        .build()
}

fun createPocCategoryRow(text: String, colSpan: Int): Row {
    return Row.builder().textColor(cvColor).borderWidth(0.2F).backgroundColor(grayColor)
        .add(TextCell.builder().text(text).colSpan(colSpan).build())
        .build()
}

fun createPocItemRow(): Row {
    return Row.builder().textColor(cvColor).borderWidth(0.2F).backgroundColor(Color.WHITE).verticalAlignment(VerticalAlignment.MIDDLE)
        .add(TextCell.builder().text("Plafond").build())
        .add(createBadgeCell())
        .add(TextCell.builder().text("F").horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("").horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("1").horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("Le plafond est presque comme neuf. Cependant, une tâche de frottement est visible dans l'angle gauche.").build())
        .build()
}

fun TableBuilder.addPhotos(columns: Int, pictureNb: Int): TableBuilder {
    val picturePercentSize = 25F
    val portraitColSpan = 3
    val nbPictureInRow = columns / portraitColSpan
    //val maxPicture = (100 / picturePercentSize).toInt()

    val row = Row.builder().textColor(cvColor).borderWidth(0.2F).backgroundColor(Color.WHITE)
    for (i in 1..nbPictureInRow) {
        row.add(createPictureCell(colSpan = portraitColSpan))
    }
    row.build()
    this.addRow(row.build())

    return this
}

fun TableBuilder.addPhotosLandscape(columns: Int, pictureNb: Int): TableBuilder {
    val picturePercentSize = 25F
    val landscapeColSpan = 4
    val nbPictureInRow = columns / landscapeColSpan
    //val maxPicture = (100 / picturePercentSize).toInt()

    val row = Row.builder().textColor(cvColor).borderWidth(0.2F).backgroundColor(Color.WHITE)
    for (i in 1..nbPictureInRow) {
        row.add(createPictureCell(colSpan = landscapeColSpan))
    }
    row.build()
    this.addRow(row.build())

    return this
}

val MARGIN_PERCENT = 5F
val margin = PDRectangle.A4.width * (MARGIN_PERCENT / 100)
fun calculateWitdhPercent(percent: Float) = (PDRectangle.A4.width - margin * 2) * (percent / 100)

val cvColor = Color(4, 19, 39)
val grayColor = Color(230, 230, 230)

document.use { document ->
    val page = PDPage(PDRectangle.A4)
    document.addPage(page)
    PDPageContentStream(document, page).use { contentStream ->

        // Build the table
        val contentTable: Table = Table.builder()
            .addColumnsOfWidth(calculateWitdhPercent(15F), calculateWitdhPercent(7.5F), calculateWitdhPercent(20F), calculateWitdhPercent(10F), calculateWitdhPercent(7.5F), calculateWitdhPercent(40F))
            .padding(8F)
            .fontSize(9)
            .addRow(createPocLabelRow())
            .addRow(createPocCategoryRow("Bati", 6))
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .build()

        val pictureTable: Table = Table.builder()
            .addColumnsOfWidth(
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F),
                calculateWitdhPercent(8.3333F))
            .padding(8F)
            .fontSize(9)
            .addRow(createPocCategoryRow("Photos", 12))
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotosLandscape(columns = 12, pictureNb = 3)
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotosLandscape(columns = 12, pictureNb = 3)
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotos(columns = 12, pictureNb = 4)
            .addPhotos(columns = 12, pictureNb = 4)
            .build()

        // Set up the drawer
        val contentDrawer: TableDrawer = RepeatedHeaderTableDrawer.builder()
            .contentStream(contentStream)
            .page(page)
            .startX(margin)
            .startY(page.mediaBox.upperRightY - 20f)
            .endY(50F)
            .numberOfRowsToRepeat(1)
            .table(contentTable)
            .build()


        val pictureDrawer: TableDrawer = TableDrawer.builder()
            .contentStream(contentStream)
            .startX(margin)
            .startY(page.mediaBox.upperRightY - contentTable.height - 20f)
            .endY(50F)
            .table(pictureTable)
            .build()

        RepeatedHeaderTableDrawer.builder()
            .table(contentTable)
            .startX(margin)
            .startY(page.mediaBox.upperRightY - 20f)
            .endY(50f) // note: if not set, table is drawn over the end of the page
            .build()
            .draw(
                { document },
                { PDPage(PDRectangle.A4) }, 20f
            )

        //contentDrawer.draw()
        //pictureDrawer.draw()
    }
    document.save("example.pdf")
}



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
import org.vandeseer.checkandvisit.pagination.Pagination
import org.vandeseer.easytable.RepeatedHeaderTableDrawer

import org.vandeseer.easytable.settings.VerticalAlignment
import org.vandeseer.easytable.structure.Table.TableBuilder
import org.vandeseer.easytable.structure.cell.AbstractCell
import org.vandeseer.easytable.structure.cell.PictureCell

import java.util.Objects

val document = PDDocument()

fun createPictureCell(colSpan : Int): AbstractCell {
    // Load custom image
    val pictureBytes = IOUtils.toByteArray(Objects.requireNonNull(this.javaClass.classLoader.getResourceAsStream("tux.png")))
    val picture = PDImageXObject.createFromByteArray(document, pictureBytes, "picture")
    return PictureCell.builder().image(picture).text("Plafond n°1").textColor(Color.WHITE).backgroundTextColor(cvColor).colSpan(colSpan)
        .verticalAlignment(VerticalAlignment.MIDDLE)
        .horizontalAlignment(HorizontalAlignment.CENTER)
        .build()
}

fun createBadgeCell(colSpan: Int): AbstractCell? {
    // Load custom image
    val badgeBytes = IOUtils.toByteArray(Objects.requireNonNull(this.javaClass.classLoader.getResourceAsStream("badge.png")))
    val badge = PDImageXObject.createFromByteArray(document, badgeBytes, "badge")
    return ImageCell.builder().image(badge).colSpan(colSpan).build()
}

fun createPocLabelRow(): Row {
    return Row.builder().textColor(Color.WHITE).borderWidth(0.2F).backgroundColor(cvColor)
        .add(TextCell.builder().text("Élément").colSpan(15).build())
        .add(TextCell.builder().text("État").colSpan(8).build())
        .add(TextCell.builder().text("Fonctionnement").colSpan(20).build())
        .add(TextCell.builder().text("Propreté").colSpan(10).build())
        .add(TextCell.builder().text("Nb").colSpan(8).build())
        .add(TextCell.builder().text("Commentaire / Dégradations constatées").colSpan(39).build())
        .build()
}

fun createPocRoomRow(text: String, colSpan: Int): Row {
    return Row.builder().textColor(cvColor).fontSize(24)
        .add(TextCell.builder().text(text).colSpan(colSpan).build())
        .build()
}

fun createPocCategoryRow(text: String, colSpan: Int): Row {
    return Row.builder().textColor(cvColor).fontSize(18).borderWidth(0.2F).backgroundColor(grayColor)
        .add(TextCell.builder().text(text).colSpan(colSpan).build())
        .build()
}

//calculateWitdhPercent(15F), calculateWitdhPercent(7.5F), calculateWitdhPercent(20F), calculateWitdhPercent(10F), calculateWitdhPercent(7.5F), calculateWitdhPercent(40F))


fun createPocItemRow(): Row {
    return Row.builder().textColor(cvColor).borderWidth(0.2F).backgroundColor(Color.WHITE).verticalAlignment(VerticalAlignment.MIDDLE)
        .add(TextCell.builder().text("Plafond").colSpan(15).build())
        .add(createBadgeCell(8))
        .add(TextCell.builder().text("F").colSpan(20).horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("").colSpan(10).horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("1").colSpan(8).horizontalAlignment(HorizontalAlignment.CENTER).build())
        .add(TextCell.builder().text("Le plafond est presque comme neuf. Cependant, une tâche de frottement est visible dans l'angle gauche.").colSpan(39).build())
        .build()
}

fun TableBuilder.addPhotos(columns: Int, pictureNb: Int): TableBuilder {
    val picturePercentSize = 25F
    val portraitColSpan = 25
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
val grid = FloatArray(100) { 1F }

document.use { document ->
    val page = PDPage(PDRectangle.A4)
    document.addPage(page)
    PDPageContentStream(document, page).use { contentStream ->
        //calculateWitdhPercent(15F), calculateWitdhPercent(7.5F), calculateWitdhPercent(20F), calculateWitdhPercent(10F), calculateWitdhPercent(7.5F), calculateWitdhPercent(40F))

        // Build the table
        val contentTable: Table = Table.builder()
            .addPercentGridColumns(PDRectangle.A4.width - margin * 2)
           // .addColumnsOfWidth(1, 10F, 10F)
            //.addColumnsOfWidth(grid.forEach { column -> addColumnOfWitdh(calculateWitdhPercent(column)) })
            .padding(8F)
            .fontSize(9)
            .addRow(createPocRoomRow("Cuisine", 100))
            .addRow(createPocLabelRow())
            .addRow(createPocCategoryRow("Bati", 100))
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
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addRow(createPocItemRow())
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .addPhotos(columns = 100, pictureNb = 4)
            .build()

        /*val pictureTable: Table = Table.builder()
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
            .build()*/

        // Set up the drawer
        /*val contentDrawer: TableDrawer = RepeatedHeaderTableDrawer.builder()
            .contentStream(contentStream)
            .page(page)
            .startX(margin)
            .startY(page.mediaBox.upperRightY - 20f)
            .endY(50F)
            .numberOfRowsToRepeat(2)
            .table(contentTable)
            .build()*/


        /*val pictureDrawer: TableDrawer = TableDrawer.builder()
            .contentStream(contentStream)
            .startX(margin)
            .startY(page.mediaBox.upperRightY - contentTable.height - 20f)
            .endY(50F)
            .table(pictureTable)
            .build()*/

        RepeatedHeaderTableDrawer.builder()
            .table(contentTable)
            .startX(margin)
            .numberOfRowsToRepeat(2)
            .startY(page.mediaBox.upperRightY - 20F)
            .endY(20F) // note: if not set, table is drawn over the end of the page
            .build()
            .draw(
                { document },
                { PDPage(PDRectangle.A4) }, 20F
            )

        //contentDrawer.draw()
        //pictureDrawer.draw()
    }
    println("Document pages : ${document.pages.count}")
    Pagination.addPageNumbers(document, "{0}/${document.pages.count}", 60, 18)
    document.save("example.pdf")
    document.close()
}

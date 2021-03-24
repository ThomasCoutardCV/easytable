package org.vandeseer.integrationtest.custom;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedLine;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class CustomCellDrawerTest {

    public static final String FILE_NAME = "coucou.pdf";

    private static final TextCellDrawer CUSTOM_DRAWER = new TextCellDrawer() {
        @Override
        public void drawBorders(DrawingContext drawingContext) {
            super.drawBorders(drawingContext);

            final Point2D.Float start = drawingContext.getStartingPoint();
            final PDPageContentStream contentStream = drawingContext.getContentStream();

            final float cellWidth = cell.getWidth();

            final float rowHeight = cell.getRow().getHeight();
            final float height = Math.max(cell.getHeight(), rowHeight);
            final float sY = rowHeight < cell.getHeight()
                    ? start.y + rowHeight - cell.getHeight()
                    : start.y;


            if (isLeftBorderCell(cell)) {
                try {
                    DrawingUtil.drawLine(contentStream, PositionedLine.builder()
                            .startX(start.x)
                            .startY(sY)
                            .endX(start.x)
                            .endY(sY + height)
                            .width(cell.getBorderWidthLeft())
                            .color(Color.BLACK)
                            .resetColor(cell.getBorderColor())
                            .borderStyle(cell.getBorderStyleLeft())
                            .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isRightBorderCell(cell)) {
                try {
                    DrawingUtil.drawLine(contentStream, PositionedLine.builder()
                            .startX(start.x + cellWidth)
                            .startY(sY)
                            .endX(start.x + cellWidth)
                            .endY(sY + height)
                            .width(cell.getBorderWidthRight())
                            .color(Color.BLACK)
                            .resetColor(cell.getBorderColor())
                            .borderStyle(cell.getBorderStyleRight())
                            .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isBottomBorderCell(cell)) {
                try {
                    DrawingUtil.drawLine(contentStream, PositionedLine.builder()
                            .startX(start.x)
                            .startY(sY)
                            .endX(start.x + cellWidth)
                            .endY(sY)
                            .width(cell.getBorderWidthBottom())
                            .color(Color.BLACK)
                            .resetColor(cell.getBorderColor())
                            .borderStyle(cell.getBorderStyleBottom())
                            .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isTopBorderCell(cell)) {
                try {
                    DrawingUtil.drawLine(contentStream, PositionedLine.builder()
                            .startX(start.x)
                            .startY(start.y + rowHeight)
                            .endX(start.x + cellWidth)
                            .endY(start.y + rowHeight)
                            .width(cell.getBorderWidthTop())
                            .color(Color.BLACK)
                            .resetColor(cell.getBorderColor())
                            .borderStyle(cell.getBorderStyleTop())
                            .build()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean isRightBorderCell(AbstractCell cell) {
            return cell.getRow().getCells().get(cell.getRow().getCells().size() -1).equals(cell);
        }

        private boolean isBottomBorderCell(AbstractCell cell) {
            return cell.getRow().getTable().getRows().get(cell.getRow().getTable().getRows().size() - 1).equals(cell.getRow());
        }

        private boolean isTopBorderCell(AbstractCell cell) {
            return cell.getRow().getTable().getRows().get(0).equals(cell.getRow());
        }

        private boolean isLeftBorderCell(AbstractCell cell) {
            return cell.getRow().getCells().get(0).equals(cell);
        }

    };

    @Test
    public void testCustomCellDrawer() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                TableDrawer.builder()
                        .contentStream(contentStream)
                        .table(createSimpleTable())
                        .startX(50)
                        .startY(page.getMediaBox().getHeight() - 50)
                        .build()
                        .draw();

            }

            document.save(TestUtils.TARGET_FOLDER + "/" + FILE_NAME);
        }

    }

    private static Table createSimpleTable() {
        return Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .borderColor(Color.LIGHT_GRAY)
                .borderWidth(1)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("One").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Two").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Three").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Four").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("One").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Two").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Three").build())
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).text("Four").build())
                        .build())
                .build();
    }

}
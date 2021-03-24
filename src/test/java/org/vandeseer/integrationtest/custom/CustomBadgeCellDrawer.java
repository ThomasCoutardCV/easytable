package org.vandeseer.integrationtest.custom;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedRectangle;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.awt.geom.Point2D;

public class CustomBadgeCellDrawer {

    public static final float COLUMN_WIDTH = 50f;

    @SuperBuilder
    @Getter
    public static class BadgeCell extends AbstractCell {

        private static final float RECTANGLE_HEIGHT = 20f;

        private float someValue;
        private Color someColor;

        private float someOtherValue;
        private Color someOtherColor;

        @Override
        public float getMinHeight() {
            return RECTANGLE_HEIGHT + getVerticalPadding();
        }

        @Override
        protected Drawer createDefaultDrawer() {

            return new BadgeCellAbstractCellDrawer(this);
        }

        private class BadgeCellAbstractCellDrawer extends AbstractCellDrawer<BadgeCell> {

            public BadgeCellAbstractCellDrawer(BadgeCell badgeCell) {
                this.cell = badgeCell;
            }

            @SneakyThrows
            @Override
            public void drawContent(DrawingContext drawingContext) {
                final PDPageContentStream contentStream = drawingContext.getContentStream();
                final Point2D.Float start = drawingContext.getStartingPoint();

                final float rowHeight = cell.getRow().getHeight();
                final float y = rowHeight < cell.getHeight()
                        ? start.y + rowHeight - cell.getHeight()
                        : start.y;

                // Actual
                DrawingUtil.drawRectangle(contentStream,
                        PositionedRectangle.builder()
                                .x(start.x + cell.getPaddingLeft())
                                .y(y + cell.getPaddingBottom())
                                .width(cell.getWidth() - cell.getHorizontalPadding())
                                .height(RECTANGLE_HEIGHT * someValue)
                                .color(cell.getSomeColor()).build()
                );

                // Unused
                DrawingUtil.drawRectangle(contentStream,
                        PositionedRectangle.builder()
                                .x(start.x + cell.getPaddingLeft())
                                .y(y + cell.getPaddingBottom() + RECTANGLE_HEIGHT * someValue)
                                .width(cell.getWidth() - cell.getHorizontalPadding())
                                .height(RECTANGLE_HEIGHT * someOtherValue)
                                .color(cell.getSomeOtherColor()).build()
                );
            }

            @Override
            protected float calculateInnerHeight() {
                return RECTANGLE_HEIGHT;
            }

        }
    }
}

package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.cell.PictureCell;

import java.awt.geom.Point2D;
import java.io.IOException;

@NoArgsConstructor
public class PictureCellDrawer extends AbstractCellDrawer<PictureCell> {

    public PictureCellDrawer(PictureCell cell) {
        this.cell = cell;
    }

    @Override
    @SneakyThrows
    public void drawContent(DrawingContext drawingContext) {
        final PDPageContentStream contentStream = drawingContext.getContentStream();
        final float moveX = drawingContext.getStartingPoint().x;

        final Point2D.Float size = cell.getFitSize();
        final Point2D.Float drawAt = new Point2D.Float();

        // Handle horizontal alignment by adjusting the xOffset
        float xOffset = moveX + cell.getPaddingLeft();
        if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
            xOffset = moveX + (cell.getWidth() - (size.x + cell.getPaddingRight()));

        } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
            final float diff = (cell.getWidth() - size.x) / 2;
            xOffset = moveX + diff;

        }

        drawAt.x = xOffset;
        drawAt.y = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment() - size.y;
        contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);
        drawText(drawingContext, PositionedStyledText.builder().
                x(drawAt.x + 2.5F)
                .y(drawAt.y + size.y - 2.5F)
                .text(cell.getText())
                .link("https://www.google.com")
                .font(cell.getFont())
                .backgroundColor(cell.getBackgroundTextColor())
                .fontSize(cell.getFontSize())
                .color(cell.getTextColor())
                .build());
    }

    protected void drawText(DrawingContext drawingContext, PositionedStyledText positionedStyledText) throws IOException {
        DrawingUtil.drawTextBackgrounded(
                drawingContext.getContentStream(),
                positionedStyledText
        );
    }

    @Override
    protected float calculateInnerHeight() {
        return (float) cell.getFitSize().getY();
    }

}


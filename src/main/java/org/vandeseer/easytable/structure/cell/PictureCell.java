package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.ImageCellDrawer;
import org.vandeseer.easytable.drawing.cell.PictureCellDrawer;

import java.awt.*;
import java.awt.geom.Point2D;

@Getter
@SuperBuilder(toBuilder = true)
public class PictureCell extends AbstractTextCell {

    @Builder.Default
    private final float scale = 1.0f;

    @NonNull
    private PDImageXObject image;

    @NonNull
    protected String text;

    @NonNull
    protected Color backgroundTextColor;

    private float maxHeight;

    @Override
    public float getMinHeight() {
        return (getFitSize().y + getVerticalPadding()) > super.getMinHeight()
                ? (getFitSize().y + getVerticalPadding())
                : super.getMinHeight();
    }

    @Override
    protected Drawer createDefaultDrawer() {
        return new PictureCellDrawer(this);
    }

    public Point2D.Float getFitSize() {
        final Point2D.Float sizes = new Point2D.Float();
        float scaledWidth = image.getWidth() * getScale();
        float scaledHeight = image.getHeight() * getScale();
        final float resultingWidth = getWidth() - getHorizontalPadding();

        // maybe reduce the image to fit in column
        if (scaledWidth > resultingWidth) {
            scaledHeight = (resultingWidth / scaledWidth) * scaledHeight;
            scaledWidth = resultingWidth;
        }

        if (maxHeight > 0.0f && scaledHeight > maxHeight) {
            scaledWidth = (maxHeight / scaledHeight) * scaledWidth;
            scaledHeight = maxHeight;
        }

        sizes.x = scaledWidth;
        sizes.y = scaledHeight;

        return sizes;
    }

}

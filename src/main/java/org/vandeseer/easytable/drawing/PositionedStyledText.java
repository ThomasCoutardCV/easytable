package org.vandeseer.easytable.drawing;

import lombok.Builder;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@Builder(toBuilder = true)
@Getter
public class PositionedStyledText {

    private final float x;
    private final float y;
    private final String text;
    @Nullable
    private final String link;
    private final PDFont font;
    private final int fontSize;
    private final Color color;
    private final Color backgroundColor;
}

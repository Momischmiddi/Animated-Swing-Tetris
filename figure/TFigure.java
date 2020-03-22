package figure;

import java.util.Arrays;

public class TFigure extends Figure {
    
    private static final float R = 0.2f;
    private static final float G = 0.5f;
    private static final float B = 1.0f;
    
    public TFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(160, 0, R, G, B, false),
                new Block(120, 40, R, G, B, true),
                new Block(160, 40, R, G, B, false),
                new Block(160, 80, R, G, B, false)
            })
        );
    }
    
}

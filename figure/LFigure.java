package figure;

import java.util.Arrays;

public class LFigure extends Figure {
    
    private static final float R = 0.2f;
    private static final float G = 0.7f;
    private static final float B = 0.1f;
    
    public LFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(160, 0, R, G, B, false),
                new Block(160, 40, R, G, B, true),
                new Block(160, 80, R, G, B, false),
                new Block(200, 80, R, G, B, false)
            })
        );
    }
    
}

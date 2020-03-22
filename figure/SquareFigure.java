package figure;

import java.util.Arrays;

public class SquareFigure extends Figure {
    
    private static final float R = 1.0f;
    private static final float G = 1.0f;
    private static final float B = 0.0f;
    
    public SquareFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(160, 0, R, G, B, true),
                new Block(200, 0, R, G, B, false),
                new Block(160, 40, R, G, B, false),
                new Block(200, 40, R, G, B, false)
            })
        );
    }
    
}

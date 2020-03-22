package figure;

import java.util.Arrays;

public class SnakeRightFigure extends Figure {
    
    private static final float R = 0.98f;
    private static final float G = 0.44f;
    private static final float B = 0.3f;
    
    public SnakeRightFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(160, 0, R, G, B, false),
                new Block(200, 0, R, G, B, true),
                new Block(160, 40, R, G, B, false),
                new Block(120, 40, R, G, B, false)
            })
        );
    }
    
}

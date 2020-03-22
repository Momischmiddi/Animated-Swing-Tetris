package figure;

import java.util.Arrays;

public class SnakeLeftFigure extends Figure {
        
    private static final float R = 0.4f;
    private static final float G = 0.4f;
    private static final float B = 0.4f;
    
    public SnakeLeftFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(120, 0, R, G, B, false),
                new Block(160, 0, R, G, B, false),
                new Block(160, 40, R, G, B, true),
                new Block(200, 40, R, G, B, false)
            })
        );
    }
    
}

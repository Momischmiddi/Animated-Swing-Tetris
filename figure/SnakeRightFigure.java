package figure;

import java.util.Arrays;
import threads.Shared;

public class SnakeRightFigure extends Figure {
    
    private static final float R = 0.98f;
    private static final float G = 0.44f;
    private static final float B = 0.3f;
    
    public SnakeRightFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(Shared.blockSize*4, 0, R, G, B, false),
                new Block(Shared.blockSize*5, 0, R, G, B, true),
                new Block(Shared.blockSize*4, Shared.blockSize, R, G, B, false),
                new Block(Shared.blockSize*3, Shared.blockSize, R, G, B, false)
            })
        );
    }
    
}

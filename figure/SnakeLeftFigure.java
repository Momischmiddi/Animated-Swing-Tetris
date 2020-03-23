package figure;

import java.util.Arrays;
import threads.Shared;

public class SnakeLeftFigure extends Figure {
        
    private static final float R = 0.4f;
    private static final float G = 0.4f;
    private static final float B = 0.4f;
    
    public SnakeLeftFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(Shared.blockSize*3, 0, R, G, B, false),
                new Block(Shared.blockSize*4, 0, R, G, B, false),
                new Block(Shared.blockSize*4, Shared.blockSize, R, G, B, true),
                new Block(Shared.blockSize*5, Shared.blockSize, R, G, B, false)
            })
        );
    }
    
}

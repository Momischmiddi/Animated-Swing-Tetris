package figure;

import java.util.Arrays;
import threads.Shared;

public class SquareFigure extends Figure {
    
    private static final float R = 1.0f;
    private static final float G = 1.0f;
    private static final float B = 0.0f;
    
    public SquareFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(Shared.blockSize*4, 0, R, G, B, true),
                new Block(Shared.blockSize*5, 0, R, G, B, false),
                new Block(Shared.blockSize*4, Shared.blockSize, R, G, B, false),
                new Block(Shared.blockSize*5, Shared.blockSize, R, G, B, false)
            })
        );
    }
    
}

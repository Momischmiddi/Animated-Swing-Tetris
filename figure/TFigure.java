package figure;

import java.util.Arrays;
import threads.Shared;

public class TFigure extends Figure {
    
    private static final float R = 0.2f;
    private static final float G = 0.5f;
    private static final float B = 1.0f;
    
    public TFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(Shared.blockSize*4, 0, R, G, B, false),
                new Block(Shared.blockSize*3, Shared.blockSize, R, G, B, true),
                new Block(Shared.blockSize*4, Shared.blockSize, R, G, B, false),
                new Block(Shared.blockSize*4, Shared.blockSize*2, R, G, B, false)
            })
        );
    }
    
}

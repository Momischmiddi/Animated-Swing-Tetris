package figure;

import java.util.Arrays;
import threads.Shared;

public class IFigure extends Figure {
    
    private static final float R = 1.f;
    private static final float G = 0.1f;
    private static final float B = 0.4f;
    
    public IFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(Shared.blockSize*4, 0, R, G, B, false),
                new Block(Shared.blockSize*4, Shared.blockSize, R, G, B, true),
                new Block(Shared.blockSize*4, Shared.blockSize*2, R, G, B, false),
                new Block(Shared.blockSize*4, Shared.blockSize*3, R, G, B, false)
            })
        );
    }
    
}

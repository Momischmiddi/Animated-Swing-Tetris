package figure;

import java.util.Arrays;

public class IFigure extends Figure {
    
    private static final float R = 1.f;
    private static final float G = 0.1f;
    private static final float B = 0.4f;
    
    public IFigure() {
        super(
            Arrays.asList(new Block[]{
                new Block(160, 0, R, G, B, false),
                new Block(160, 40, R, G, B, true),
                new Block(160, 80, R, G, B, false),
                new Block(160, 120, R, G, B, false)
            })
        );
    }
    
}

package figure;

import threads.Shared;
import java.awt.Color;

public class Block {

    private final float R;
    private final float G;
    private final float B;
            
    private int y;
    private int x;
    private final boolean isCenter;
    
    public Block(int x, int y, float R, float G, float B, boolean isCenter) {
        this.R = R;
        this.G = G;
        this.B = B;
        
        this.x = x;
        this.y = y;
        this.isCenter = isCenter;
    }
    
    public Block cloneBlock() {
        return new Block(getX(), getY(), R, G, B, isCenter());
    }
    
    public Color getMovingColor() {
        return new Color(R, G, B, 0.6f);
    }
    
    public Color getFixColor() {
        return new Color(R, G, B, 1.0f);
    }
    
    public float getR() {
        return this.R;
    }
    
    public float getG() {
        return this.G;
    }
    
    public float getB() {
        return this.B;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public boolean isCenter() {
        return this.isCenter;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    
}

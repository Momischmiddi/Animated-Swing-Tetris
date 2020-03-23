package figure;

import threads.Shared;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Figure {

    private final List<Block> figBlocks;

    public enum Shift {
        LEFT,
        RIGHT,
        DOWN
    }
        
    public Figure(List<Block> blocks) {
        List<Block> figBlocks = new ArrayList<>();
        
        for(Block block : blocks) {
            block.setX(block.getX());
            block.setY(block.getY());
            figBlocks.add(block);
        }
        
        this.figBlocks = figBlocks;
    }
    
    public void setActive() {
        Shared.setMovingBlocks(figBlocks);
    }
    
        
    public static boolean isVerticalOutOfBounds(List<Block> movingBlocks) {
        for(Block block : movingBlocks) {
            if(block.getY() >= (Shared.Y_SIZE-1)*Shared.blockSize) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isHorizontalOutOfBounds(List<Block> movingBlocks) {
        for(Block block : movingBlocks) {
            if(block.getX() < 0 || block.getX() > (Shared.X_SIZE-1)*Shared.blockSize) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean collidesWithBlocks(List<Block> fixBlocks, List<Block> movingBlocks) {
        for(Block block : fixBlocks) {
            for(Block movingBlock : movingBlocks) {
                Rectangle fixRect = new Rectangle(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
                
                if(fixRect.contains(new Point(movingBlock.getX(), movingBlock.getY()+Shared.blockSize))) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean shift(Shift shift) {
        List<Block> shiftedBlocks = new ArrayList<>();
        List<Block> movingBlocks = Shared.getMovingBlocks();
        
        for(Block block : movingBlocks) {
            int shiftedX = -1;
            if(shift == Shift.LEFT) {
                shiftedX = block.getX() - Shared.blockSize;
            } else if(shift == Shift.RIGHT) {
                shiftedX = block.getX() + Shared.blockSize;
            }
            
            shiftedBlocks.add(new Block(shiftedX, block.getY(), block.getR(), block.getG(), block.getB(), block.isCenter()));
        }
                        
        if(!isHorizontalOutOfBounds(shiftedBlocks) && !isVerticalOutOfBounds(shiftedBlocks) && !collidesWithBlocks(Shared.getFixBlocks(), shiftedBlocks)) {
            Shared.setMovingBlocks(shiftedBlocks);
            return true;
        }

        return false;
    }

    public boolean rotate() {
        List<Block> rotatedBlocks = new ArrayList<>();
        
        Block oldCenter = null;
        Block rotatedCenter = null;

        for(Block block : Shared.getMovingBlocks()) {
            rotatedCenter = new Block(-block.getY(), block.getX(), block.getR(), block.getG(), block.getB(), block.isCenter());
            rotatedBlocks.add(rotatedCenter);

            if(block.isCenter()) {
                oldCenter = block;
            }
        }

        for(Block block : rotatedBlocks) {
            int xOffset = oldCenter.getX() - rotatedCenter.getX();
            int yOffset = oldCenter.getY() - rotatedCenter.getY();

            block.setX(block.getX() + xOffset);
            block.setY(block.getY() + yOffset);
        }
                
        if(!isHorizontalOutOfBounds(rotatedBlocks) && !isVerticalOutOfBounds(rotatedBlocks) && !collidesWithBlocks(Shared.getFixBlocks(), rotatedBlocks)) {
            Shared.setMovingBlocks(rotatedBlocks);
            return true;
        }
        
        return false;
    }
    
    public List<Block> getBlocks() {
        return figBlocks;
    }
}

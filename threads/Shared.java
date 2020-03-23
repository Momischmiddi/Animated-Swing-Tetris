package threads;

import figure.Block;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class Shared {    
    private static final Object listLock = new Object();
    
    private static final Object fixBlockLock = new Object();
    private static List<Block> fixBlocks = new ArrayList<>();
    
    private static final Object movingBlockLock = new Object();
    private static List<Block> movingBlocks = new ArrayList<>();
    
    private static final Object gameOverLock = new Object();
    private static boolean isGameOver = false;
    
    private static final Object figureLock = new Object();
    
    private static int speed = 2;
    public static final int STATUS_X_SIZE = 15;
    public static final int X_SIZE = 10;
    public static final int Y_SIZE = 20;
    public static int blockSize;
    
    public static final Font statusFont = new Font("Comic Sans MS", Font.BOLD, 16);
    
    public static void reset() {
        fixBlocks.clear();
        movingBlocks.clear();
        isGameOver = false;
    }
    
    public static List<Block> getFixBlocks() {
        List<Block> result = new ArrayList<>();
        
        synchronized(fixBlockLock) {
            synchronized(listLock) {
                for(Block block : fixBlocks) {
                    result.add(block.cloneBlock());
                }
            }
        }
        
        return result;
    }
    
    public static void setFixBlocks(List<Block> blocks) {
        synchronized(fixBlockLock) {
            synchronized(listLock) {
                fixBlocks = blocks;
            }
        }
    }
    
    public static List<Block> getMovingBlocks() {
        List<Block> result = new ArrayList<>();
        
        synchronized(movingBlockLock) {
            synchronized(listLock) {
                for(Block block : movingBlocks) {
                    result.add(block.cloneBlock());
                }
            }
        }
                
        return result;
    }
    
    public static void setMovingBlocks(List<Block> blocks) {
        synchronized(movingBlockLock) {
            synchronized(listLock) {
                movingBlocks = blocks;
            }
        }
    }

    public static void addMovingToFix() {
        synchronized(listLock) {
            fixBlocks.addAll(movingBlocks);
            movingBlocks.clear();
        }
    }

    public static void setGameOver(boolean isGameOver) {
        synchronized(gameOverLock) {
            Shared.isGameOver = isGameOver;
        }
    }
    
    public static boolean isGameOver() {
        boolean result = false;
        
        synchronized(gameOverLock) {
            result = Shared.isGameOver;
        }
        
        return result;
    }

    public static void setSpeed(int speed) {
        Shared.speed = speed;
    }
    
    public static int getSpeed() {
        return speed;
    }
}

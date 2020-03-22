package threads;

import containers.GamePanel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndingThread extends Thread {

    private final GamePanel gamePanel;
    private final List<Point> allPoints = new ArrayList<>();
    
    public EndingThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initAllPoints();
        start();
    }
    
    @Override
    public void run() {
        System.out.println("Endingpaint-Thread started.");
        
        for(int i=0 ; i<10*20 ; i++) {                
            try {
                Thread.sleep(8);
                gamePanel.addToRemove(allPoints.get(i));
                gamePanel.repaint();
            } catch(Exception e){
                e.printStackTrace();
            }    
        }
        
        gamePanel.getDifficultyPanel().enableDifficulty(true);
        
        System.out.println("Endingpaint-Thread terminating.");
    }

    private void initAllPoints() {
        for(int x=0 ; x<10 ; x++) {
            for(int y=0 ; y<20 ; y++) {
                allPoints.add(new Point(x, y));
            }
        }
                
        Collections.shuffle(allPoints);
    }
    
}

package threads;

import containers.GamePanel;
import resources.AudioHelpers;

public class TimeThread extends Thread {

    private final GamePanel gamePanel;
    
    public TimeThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        
        start();
    }
    
    @Override
    public void run() {
        System.out.println("Levelup-Thread started.");
        try {
            while(!Shared.isGameOver()) {
                Thread.sleep(30000);
                AudioHelpers.playSound(getClass(), "levelup.wav", 1.0);
                gamePanel.getDifficultyPanel().incLevel();
            }
        } catch(Exception e){
            if(!Shared.isGameOver()) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Levelup-Thread terminated.");
    }
    
}

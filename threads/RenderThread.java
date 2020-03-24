package threads;

import containers.GamePanel;

public class RenderThread extends Thread {

    private final int TIME_PER_SQUARE_IN_MS = 1000;
    private final long SLEEP_MS = TIME_PER_SQUARE_IN_MS / Shared.blockSize;
    private final GamePanel gamePanel;

    public RenderThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        start();
    }
    
    int ctr = 0;
    
    @Override
    public void run() {
        System.out.println("Rendering-Thread started.");
                
        try {
            while(!Shared.isGameOver() && !Shared.gameWon) {
                Thread.sleep(SLEEP_MS - Shared.getSpeed());
                this.gamePanel.moveDown();
            }
        } catch(InterruptedException e) {
            if(!Shared.isGameOver()) {
                e.printStackTrace();
            }
        }
        
        if(Shared.gameWon) {
            gamePanel.getBackgroundAudioThread().interrupt();
            gamePanel.showWinningScreen();
        }
        
        System.out.println("Rendering-Thread terminating.");
    }
}

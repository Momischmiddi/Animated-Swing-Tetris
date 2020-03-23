package threads;

import containers.GamePanel;

public class RenderThread extends Thread {

    private final int TIME_PER_SQUARE_IN_MS = 500;
    private final long SLEEP_MS = TIME_PER_SQUARE_IN_MS / Shared.blockSize;
    private final GamePanel gamePanel;

    public RenderThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        start();
    }
    
    @Override
    public void run() {
        System.out.println("Rendering-Thread started.");
        
        while(!Shared.isGameOver()) {
            try {
                Thread.sleep(SLEEP_MS / Shared.getSpeed());
                this.gamePanel.moveDown();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Rendering-Thread terminating.");
    }
}

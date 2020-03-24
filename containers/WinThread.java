package containers;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import resources.AudioHelpers;
import threads.Shared;

public class WinThread extends Thread {

    private final GamePanel gamePanel;
    
    public WinThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        start();
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Win-Thread started.");
            
            long sleepTime = 90;
            
            Clip clip = AudioHelpers.playSound(getClass(), "win.wav", 1.0);
            
            try {
                int curX;
                int curY;
                
                for(int cycle=0 ; cycle<5 ; cycle++) {
                    curX = cycle;
                    curY = cycle;
                    for(int i=0 ; i<9-cycle*2 ; i++) {
                        try {
                            Thread.sleep(sleepTime);
                            gamePanel.addSnakeToRemove(new Point(curX, curY));
                            gamePanel.repaint();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        
                        curX++;
                    }
                    
                    for(int i=0 ; i<19-cycle*2 ; i++) {
                        try {
                            Thread.sleep(sleepTime);
                            gamePanel.addSnakeToRemove(new Point(curX, curY));
                            gamePanel.repaint();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        
                        curY++;
                    }
                    
                    for(int i=0 ; i<9-cycle*2 ; i++) {
                        try {
                            Thread.sleep(sleepTime);
                            gamePanel.addSnakeToRemove(new Point(curX, curY));
                            gamePanel.repaint();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        
                        curX--;
                    }
                    
                    for(int i=0 ; i<19-cycle*2 ; i++) {
                        try {
                            Thread.sleep(sleepTime);
                            gamePanel.addSnakeToRemove(new Point(curX, curY));
                            gamePanel.repaint();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        
                        curY--;
                    }
                }
                
                Shared.gameWon = false;
                Shared.setGameOver(false);
                gamePanel.getMainFrame().restartBackgroundThread();
                gamePanel.getStatusPanel().getDifficultyPanel().enableDifficulty(true);
            } catch(Exception e) {
                e.printStackTrace();
            }
            
            System.out.println("Win-Thread terminating.");
        } catch(UnsupportedAudioFileException ex) {
            Logger.getLogger(WinThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WinThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(WinThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

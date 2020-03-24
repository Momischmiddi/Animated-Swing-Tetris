package threads;

import javax.sound.sampled.Clip;
import resources.AudioHelpers;

public class BackgroundAudioThread extends Thread {
    
    public BackgroundAudioThread() {
        start();
    }
    
    @Override
    public void run() {
        System.out.println("Audio-Thread started.");
        
        while(!Shared.gameWon) {
            Clip clip = null;
            try {
                clip = AudioHelpers.playSound(getClass(), "backgroundsound.wav", 0.3);
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } catch(Exception e){
                if(!Shared.gameWon) {
                    e.printStackTrace();
                } else {
                    clip.stop();
                }
            }
        }
        
        System.out.println("Audio-Thread terminated.");
    }
}

package threads;

import resources.AudioHelpers;

public class BackgroundAudioThread extends Thread {
    
    public BackgroundAudioThread() {
        start();
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(AudioHelpers.playSound(getClass(), "backgroundsound.wav", 0.3));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

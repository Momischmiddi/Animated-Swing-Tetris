package resources;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioHelpers {
    
    public static long playSound(Class clazz, String fileName, double gain) throws UnsupportedAudioFileException, IOException, LineUnavailableException {        
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(clazz.getClassLoader().getResource("resources/" + fileName));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);

        clip.start();

        return clip.getMicrosecondLength();
    }
}

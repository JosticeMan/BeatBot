package screens.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
 
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mainGame.MainGUI;
 
public class ShipRumble 
{
 
    // to store current position
    Long currentFrame;
    Clip clip;
     
    // current status of clip
    String status;
     
    AudioInputStream audioInputStream;
    String filePath;
    boolean loop;
    // constructor to initialize streams and clip
    public ShipRumble(String s, boolean b)
        throws UnsupportedAudioFileException,
        IOException, LineUnavailableException 
    {
        // create AudioInputStream object
    	filePath = s;
    	loop = b;
        audioInputStream = 
                AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
         
        // create clip reference
        clip = AudioSystem.getClip();
         
        // open audioInputStream to the clip
        clip.open(audioInputStream);
        
        if(loop) {
        	clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
         
    }
    public void updateVolume() {
        int index = MainGUI.getVolume();

        ArrayList <Float> volumeL = new ArrayList<Float>();
        volumeL.add(-80f);
        volumeL.add(6f);
        volumeL.add(-3f);
        volumeL.add(-10f);
        float finalValue = volumeL.get(index);
 
        if(clip!=null) {
            if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(finalValue);
            }
        } 
    }
    
     
    // Work as the user enters his choice
     
    // Method to play the audio
    public void play() 
    {
        //start the clip
        clip.start();
         
        status = "play";
    }
     
    // Method to pause the audio
    public void pause() 
    {
        if (status.equals("paused")) 
        {
            //System.out.println("audio is already paused");
            return;
        }
        this.currentFrame = 
        this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }
     
    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
                                IOException, LineUnavailableException 
    {
        if (status.equals("play")) 
        {
            //System.out.println("Audio is already "+
            //"being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }
     
    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
                                            UnsupportedAudioFileException 
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }
     
    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
    IOException, LineUnavailableException 
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }
     
    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
                                                        LineUnavailableException 
    {
        if (c > 0 && c < clip.getMicrosecondLength()) 
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }
     
    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
                                            LineUnavailableException 
    {
        audioInputStream = AudioSystem.getAudioInputStream(
        new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }



	public void setLoop(boolean b) {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

 
}
package mainGame.components;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import mainGame.MainGUI;
import mainGame.components.interfaces.JustinPlaySongInterface;
import mainGame.screens.GameScreen;
 

public class PlaySong implements JustinPlaySongInterface {
	
    /**
     * Play a given audio file.
     * Tyler
     */
	
    // size of the byte buffer used to read/write the audio stream
    private static final int BUFFER_SIZE = 1024;
    
    private boolean pause;
    private boolean cancel;
     
    public PlaySong() {
        pause = false;
        cancel = false;
    }
    

    Long audioPosition;
    private Clip audioLine;
    
    /**
     * @author Tyler Ovenden
     * plays the song through setting the song's path to a clip
     * thread.sleep used to not have the song play automatically and only be called when used
     * .open and .start are the methods that queue up and play the actual song
     * .stop and .flush stop the song
       @author Justin Yau
     * pauses the song by setting the position of where the song is currently and pausing it
     *when resumed the song will be queued back up to the position
     */
    public void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            audioLine = AudioSystem.getClip();
           // System.out.println("Playback started.");

            try {
            
            	while(GameScreen.game.timePass() <= GameScreen.game.calculateTotalFallTime()) {
    				Thread.sleep(0);
            	}
            	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            audioLine.open(audioStream);
            updateVolume();
            audioLine.start();
            
            while(!cancel) {
            	sleep(0);
            	if(pause) {
            		long clipTime = audioLine.getMicrosecondPosition();
        			audioLine.stop();
            		while(pause) {
            			sleep(0);
            		}
            		audioLine.setMicrosecondPosition(clipTime);
            		if(!cancel) {
                		audioLine.start();
            		}
            	}
            }
             
            audioLine.flush();
            audioLine.close();
            audioStream.close();
            
           // System.out.println("Playback completed.");
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }      
    }

    /*
     *      // Adjust the volume on the output line.
            if (audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            	System.out.println(true);
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-15.0F);
            }
     */
    
	/**
	 * This method makes the program sleep for the given amount of time
	 * 
	 * @param time - Time in ms that you would like to make the program sleep for
	 * 
	 * @author Justin Yau
	 */
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void main(String[] args) {
      
    	String audioFilePath = "resources/maps/DreadnoughtMastermind(xi+nora2r)/DreadnoughtMastermind(xi+nora2r).wav";
        PlaySong player = new PlaySong();
        player.play(audioFilePath);
    }
    /**
     * takes the volume from maingui
     * creates arraylist of the different volumes -80 being mute, 6 being top volume
     * sets the volume of the song and is called by Andrew's method in options
     * Tyler
     */
    public void updateVolume() {
    	int index = MainGUI.getVolume();
    
		ArrayList <Float> volumeL = new ArrayList<Float>();
    	volumeL.add(-80f);
    	volumeL.add(6f);
    	volumeL.add(-3f);
    	volumeL.add(-10f);
    	float finalValue = volumeL.get(index);		 
 
    	if(audioLine!=null) {
    		if(audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
    			FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
    			volume.setValue(finalValue);	
    		}
    	} 
    }
    
	@Override
	public void pauseSong() {
	
		pause = true;
		
	}

	public void resumeSong() {

		pause = false;
		
	}

	public void stopSong() { 
		
		cancel = true;
		
	}
 
}

	
package com.client.sound;

import com.client.features.settings.Preferences;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.io.InputStream;

public class WavPlayer {

    public static final WavPlayer player = new WavPlayer();

    public static void main(String args) throws Exception {
        while (volume > 0) {
            Clip c = player.play(new File(args));
            Thread.sleep(c.getMicrosecondLength() / 1000);
            FloatControl gain = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);

            gain.setValue((MIN_VOLUME_DB * (MAX_VOLUME - volume)) / MAX_VOLUME);
        }
    }

    public static final float MIN_VOLUME_DB = -80f;
    public static final int MAX_VOLUME = 256;

    public static float volume = (int) ((256f*Preferences.getPreferences().musicVolume)/10);

    public Clip play(File f) throws Exception {
        return this.play(AudioSystem.getAudioInputStream(f));
    }

    public Clip play(InputStream in) throws Exception {
        return this.play(AudioSystem.getAudioInputStream(in));
    }

    public Clip play(AudioInputStream stream) throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(stream);

        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        // volume of 256 = 0.0
        // volume of 128 = -40.0
        // volume of 0 = -80
        gain.setValue((MIN_VOLUME_DB * (MAX_VOLUME - volume)) / MAX_VOLUME);

        clip.start();
        return clip;
    }

    public void setVolume(float volume) {
        WavPlayer.volume = (int) volume;
    }

    public void setVolume(int volume) {
        WavPlayer.volume = volume;
    }

    public int getVolume() {
        return (int) volume;
    }

}
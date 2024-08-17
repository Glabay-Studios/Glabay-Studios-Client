package com.client.sound;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;


import com.client.sign.Signlink;

public class Sound {

    // TODO
    // https://pastebin.com/vFkVrAGZ
    // store sound files as they come in (~100 max)

    private static final Sound SINGLETON = new Sound();
    private static final ExecutorService executor = Executors.newFixedThreadPool(14);

    public static Sound getSound() {
        return SINGLETON;
    }

    private static File getSound(int id) {
        return new File(Signlink.getCacheDirectory() + "sounds/" + id + ".wav");
    }

    public Sound() {
    }

    public void playSound(int id, SoundType soundType, double distanceFromOrigin) {
//        if (!executor.isShutdown() && getSound(id).exists()) {
//            executor.submit(() -> {
//                try {
//                    sound(getSound(id), soundType, distanceFromOrigin);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    executor.shutdown();
//                }
//            });
//        }
    }

    public float calculateVolume(SoundType soundType, double distanceFromOrigin) {
        double distanceVolume = (12d - distanceFromOrigin) / 12d; // 0.0-1.0
        double soundVolume = (soundType.getVolume() / 10d);
        //System.out.println("volume calculate: " + distanceVolume + " " + soundVolume);
        return (float) (soundVolume * distanceVolume);     // below 0.5 it gets fuzzy
    }

    private void sound(File soundFile, SoundType soundType, double distanceFromOrigin) throws Exception {
        AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat outFormat = getOutFormat(in.getFormat());
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        if (line != null)
        {
            line.open(outFormat, 2200);
            if (line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {
                int volume = (int) (30d * calculateVolume(soundType, distanceFromOrigin));
                FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                BooleanControl muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                System.out.println("Volume at playtime: " + volume + ", factor: "  + calculateVolume(soundType, distanceFromOrigin));
                if (volume < 5)
                {
                    muteControl.setValue(true);
                }
                else
                {
                    muteControl.setValue(false);
                    gainControl.setValue((float) (Math.log((double) volume / 100.0) / Math.log(10.0) * 20.0));
                }
            }
            line.start();
            stream(AudioSystem.getAudioInputStream(outFormat, in), line);
            line.drain();
            line.stop();
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat)
    {
        int ch = inFormat.getChannels();
        float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, rate, 8, ch, ch, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line) throws IOException
    {
        byte[] buffer = new byte[2200];
        int n = 0;
        while (n != -1)
        {
            line.write(buffer, 0, n);
            n = in.read(buffer, 0, buffer.length);
        }
    }
}

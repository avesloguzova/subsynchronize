package ru.spbau.devdays.subsync;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dmitry Tishchenko on 07.05.15.
 */
public class AudioEncoder {
    static private final String outFile = "tmp.wav";

    static public String getAudioFromResource(String resourcePath) throws IOException {

        File source = new File(resourcePath);
        File target = new File(outFile);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setChannels(1);
        audio.setSamplingRate(16000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(source, target, attrs);
        } catch (EncoderException e) {
            throw new IOException(e);
        }
        return outFile;
    }
}

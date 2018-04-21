package net.rtitov.springcampus.iot;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Microphone {
    private static final long SILENCE_DURATION = 1000;
//    private static final double SILENCE_TRESHOLD = 0.15;

    private final TargetDataLine line;
    private final AudioFormat format;
    private final int sampleRate;

    private double silenceRms = 0.15;

    public Microphone(int sampleRate) {
        this.sampleRate = sampleRate;
        this.format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new RuntimeException("Recording is not supported");
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);

            line.open(format);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public AudioFormat getAudioFormat() {
        return format;
    }

    public void startListening() {
        System.out.println("Start listening ...");
        line.start();
        silenceRms = calculateSilenceRMS();
    }

    public void stopListening() {
        System.out.println("Stop listening ...");
        line.stop();
    }

    public byte[] listenNSeconds(int seconds) {
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        int numBytesRead;
        byte[] data = new byte[line.getBufferSize() / 5];

        for (long startTime = System.currentTimeMillis(); (System.currentTimeMillis() - startTime) < seconds * 1000; ) {
            numBytesRead =  line.read(data, 0, data.length);
            out.write(data, 0, numBytesRead);
        }
        return out.toByteArray();
    }

    public double calculateSilenceRMS() {
        System.out.print("Measuring silence RMS ... ");
        byte[] data = listenNSeconds(3);

        short[] shorts = new short[data.length / 2];

        double rms = getRms(data, shorts) * 3;

        System.out.printf("%.3f\n", rms);
        return rms;
    }


    // https://stackoverflow.com/questions/43822266/detect-silence-getting-user-input-via-voice

    public byte[] getNextPhrase() {
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        int numBytesRead;
        byte[] data = new byte[line.getBufferSize() / 5];
        short[] shorts = new short[data.length / 2];

        long silenceStartTime = 0;
        boolean isSilence = true;
        boolean isSpeechStarted = false;

        System.out.print("Waiting for a phrase ... ");

        while(true) {
            numBytesRead =  line.read(data, 0, data.length);
            out.write(data, 0, numBytesRead);

            double rms = getRms(data, shorts);

//            System.out.println(numBytesRead + ", " + rms);

            if (rms < silenceRms) {
                long now = System.currentTimeMillis();

                if ((now - silenceStartTime > SILENCE_DURATION) && isSilence && isSpeechStarted) {
                    break;
                }
                if (!isSilence) {
                    silenceStartTime = now;
                }
                isSilence = true;
            } else {
                if (!isSpeechStarted) {
                    System.out.println("OK");
                }
                isSilence = false;
                isSpeechStarted = true;
            }
        }
        return out.toByteArray();
    }

    private double getRms(byte[] data, short[] shorts) {
        double rms = 0;
        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

        for (int i = 0; i < shorts.length; i++) {
           double normal = shorts[i] / 32768f;          // from -1.0 to 1.0
           rms += normal * normal;
        }
        return Math.sqrt(rms / shorts.length);
    }

}

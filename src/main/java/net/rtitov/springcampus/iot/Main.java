package net.rtitov.springcampus.iot;

public class Main {

    public static void main(String... args)  {
        Microphone mic = new Microphone(44100);
        GoogleCloudSpeechApi api = new GoogleCloudSpeechApi(args[0],mic.getSampleRate(), "LINEAR16");
        LEDStrip ledStrip = new LEDStrip();

        mic.startListening();

        while (true) {
            System.out.println("=== Ready to accept commands === ");

            byte[] data = mic.getNextPhrase();
            String command = api.recognize(data);

            System.out.println("Command: " + command);

            if (command != null) {
                command = command.toLowerCase();
                if (command.contains("lights on")) {
                    ledStrip.setColor(0xFFFFFF, 0);
                } else if (command.contains("change")) {
                    ledStrip.changeColors();
                    ledStrip.setColor(0xFFFFFF, 0);
                } else if (command.contains("off")) {
                    ledStrip.setColor(0, 0);
                } else if (command.contains("orange")) {
                    ledStrip.setColor(0xFF3000, 0);
                } else if (command.contains("blue")) {
                    ledStrip.setColor(0x0000ff, 0);
                } else if (command.contains("exit")) {
                    ledStrip.setColor(0, 0);
                    break;
                }
            }
        }


    }
}

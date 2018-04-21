package net.rtitov.springcampus.iot;

import com.pi4j.io.gpio.*;

public class LEDStrip {
    // The PWM_DIVIDER constant is used to increase the pulse rate. The PI4J library uses 100 microseconds delay per
    // one pulse, so for 0..255 range the refresh rate will be 1 / 0.0255 = 39 Hz , which is quite noticeable on my LEDs.
    // The reduced 0..85 range gives less color precision but a much less noticable 118 Hz pulse rate.
    private final static int PWM_DIVIDER = 3;

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinPwmOutput greenPin = initPin(gpio, RaspiPin.GPIO_27);
    private final GpioPinPwmOutput redPin = initPin(gpio, RaspiPin.GPIO_28);
    private final GpioPinPwmOutput bluePin = initPin(gpio, RaspiPin.GPIO_29);

    private GpioPinPwmOutput initPin(GpioController gpio, Pin pin) {
        GpioPinPwmOutput outputPin = gpio.provisionSoftPwmOutputPin(pin, 0);

        outputPin.setPwmRange(255 / PWM_DIVIDER);
        outputPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        return outputPin;
    }

    public void setColor(long color) {
        redPin.setPwm((int)(((color >> 16) & 0xFF) / PWM_DIVIDER));
        greenPin.setPwm((int)(((color >> 8) & 0xFF) / PWM_DIVIDER));
        bluePin.setPwm((int)((color & 0xFF) / PWM_DIVIDER));
    }

    public void setColor(long color, int durationMilliseconds) {
        setColor(color);
        try {
            Thread.sleep(durationMilliseconds);
        } catch (InterruptedException e) {
        }
    }

    public void changeColors() {
        long[] rgbComponent = new long[3];

        rgbComponent[0] = 255;

        for (int decColour = 0; decColour < 3; decColour++) {
            int incColour = decColour == 2 ? 0 : decColour + 1;

            // cross-fade two colours.
            for(int i = 0; i < 255; i++) {
                rgbComponent[decColour]--;
                rgbComponent[incColour]++;
                setColor((rgbComponent[0] << 16) | (rgbComponent[1] << 8) | (rgbComponent[2]), 5);
            }
        }
    }

    public static void main(String[] args) {
        LEDStrip ledStrip = new LEDStrip();

        while (true) {
            if (args.length == 0) {
                ledStrip.setColor(0xFF0000, 1000);
                ledStrip.setColor(0x00FF00, 1000);
                ledStrip.setColor(0x0000FF, 1000);
            } else if (args[0].equalsIgnoreCase("change")){
                ledStrip.changeColors();
            } else {
                ledStrip.setColor(Long.parseLong(args[0], 16), 5000);
            }
        }
    }
}


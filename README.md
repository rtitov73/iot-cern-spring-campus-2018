# Project description

This is the source code for the "Making Internet of Things with Raspberry Pi 3" presentation 
given by me at the CERN Spring Campus 2018 at the RTU Univeristy in Latvia, Riga.

The code shows how to use Raspberry PI 3 to build an LED strip controlled by voice commands. 
Google Cloud Speech API is used for voice recognition.

# Hardware

In order to run this example the following hardware has been used:

1. Raspberry PI 3
2. microSD card (to run the Raspbian OS)
3. Any USB microphone
4. A simple non-addressable LED strip, I've used this one: http://www.volumerate.com/product/844233947

A "non-addressable" strip means that all LEDs are switched on simultaneously and one couldn't address individual LEDs on the strip. If you choose to use an addressable LED strip make sure it will work with the Raspberry PI. For example, I cannot make a LED strip based on the popular WS2811 driver chip to work with Raspberry since the driver requires very precise and very short 0.5us impulses which are simply non-achievable with a PI.

# Software

This example uses the Google Cloud Speech API. This API is free from up to 60 minutes 
of voice recognition per month. In order to use this API you'd need to create a Google Cloud account, the easiest is to do it by pressing the "Setup a project" button directly from the Google Cloud Speeech API documentation (https://cloud.google.com/speech/docs/quickstart). Note that at some point you will be required to enter your billing details. Don't worry, the service is free if you use it less than 60 minutes per month and you won't be charged.

You will also need to get the Google API key in order to be able the Google REST API 
for voice recognition. You can create a new key in your Google Cloud Platfrom console 
in API and Services -> Credentials -> API key menu.

You will also need Git in order to be able to check out the project.

Finally, your Raspberry PI must have Java 8 installed. Java 8 is installed by default on the 
official Raspbian distribution (both Open JDK and Oracle flavours). Check that Oracle JDK is 
the default JDK on your PI, if this is not the case, switch to the Oracle one:

```
sudo update-alternatives --config java
```

If you don't see Oracle JDK in the list, follow [this guide](http://dariancabot.com/2016/02/24/raspberry-pi-change-java-jvm-openjdk-oracle) 

# Project structure

* **LEDStrip** - switch the given color on the LED strip using pi4j and soft PWM technique
* **Microphone** - get audio from the microphone (either given number of seconds or a phrase until next silence)
* **GoogleCloudSpeechApi** - uses the Google Cloud Speech REST API to convert voice into text
* **GoogleCloudSpeechApiRequest**, **Payload** and **Config** are simple POJOs corresponding to the Google Cloud Speech API REST API JSON protocol (see the Google documentation)
* **Main** - the main example, listens for phrases, converts them to text and then controls the lights

# Compiling the project

First checkout the latest version of the code:

```
git clone https://github.com/rtitov73/iot-cern-spring-campus-2018.git
git checkout master
```

Then build:

```
gradle build
```

If you are not compiling the project on the Raspberry PI itself, then copy the resulting JAR archive 
from *build/libs* to Raspberry (e.g. using WinSCP).

# Running

#### Controlling the LED strip using soft PWM technique 
 
 ```
java -cp speech-recognition-test-1.0-SNAPSHOT.jar net.rtitov.springcampus.iot.LEDStripSlowPwmRefresh FF4500
```

The parameter is either a hex color code (FF0000 = red, FF4500) or "change" for changing colors

#### Controlling the LED strip using soft PWM technique and no blinking 
 
 ```
java -cp speech-recognition-test-1.0-SNAPSHOT.jar net.rtitov.springcampus.iot.LEDStrip FF4500
```

The parameter is either a hex color code (FF0000 = red, FF4500) or "change" for changing colors


#### Voice recogintion

```
 java -jar speech-recognition-test-1.0-SNAPSHOT.jar your_API_key
```

Command the strip with voice commands such as "lights on", "lights off", "orange", "blue", "change colors", 
see how commands are processed in **Main.java**

# Troubleshooting

If Java couldn't find the microphone may be it is not selected as the default microphone on your 
Raspberry PI. Google for it and modify the ALSA config to make the USB microphone default one.

It could also happen that your microphone doesn't support the 44100 Hz sample rate. 
In this case find out which sample rates your microphone supports and use that sample rate 
(modify the program as necessary). Ideally you should use the sample rate 16000 Hz since 
it provides affordable voice quality with less data sent over Internet.






















package net.rtitov.springcampus.iot;

public class GoogleCloudSpeechApiConfig {
    private String encoding;
    private int sampleRateHertz;
    private String languageCode = "en-US";
    private boolean enableWordTimeOffsets = false;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getSampleRateHertz() {
        return sampleRateHertz;
    }

    public void setSampleRateHertz(int sampleRateHertz) {
        this.sampleRateHertz = sampleRateHertz;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isEnableWordTimeOffsets() {
        return enableWordTimeOffsets;
    }

    public void setEnableWordTimeOffsets(boolean enableWordTimeOffsets) {
        this.enableWordTimeOffsets = enableWordTimeOffsets;
    }
}

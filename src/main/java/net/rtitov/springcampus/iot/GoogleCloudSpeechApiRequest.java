package net.rtitov.springcampus.iot;

import org.apache.commons.codec.binary.Base64;

public class GoogleCloudSpeechApiRequest {
    private GoogleCloudSpeechApiConfig config;
    private GoogleCloudSpeechApiPayload audio;

    public GoogleCloudSpeechApiRequest() {
        config = new GoogleCloudSpeechApiConfig();
        audio = new GoogleCloudSpeechApiPayload();
    }

    public GoogleCloudSpeechApiConfig getConfig() {
        return config;
    }

    public void setConfig(GoogleCloudSpeechApiConfig config) {
        this.config = config;
    }

    public GoogleCloudSpeechApiPayload getAudio() {
        return audio;
    }

    public void setAudio(GoogleCloudSpeechApiPayload audio) {
        this.audio = audio;
    }

    public GoogleCloudSpeechApiRequest withEncoding(String encoding) {
        config.setEncoding(encoding);
        return this;
    }

    public GoogleCloudSpeechApiRequest withLanguage(String languageCode) {
        config.setLanguageCode(languageCode);
        return this;
    }

    public GoogleCloudSpeechApiRequest withSampleRate(int rate) {
        config.setSampleRateHertz(rate);
        return this;
    }

    public GoogleCloudSpeechApiRequest withContent(byte[] data) {
        audio.setContent(new String(Base64.encodeBase64(data)));
        return this;
    }

    public GoogleCloudSpeechApiRequest withUri(String uri) {
        audio.setUri(uri);
        return this;
    }
}

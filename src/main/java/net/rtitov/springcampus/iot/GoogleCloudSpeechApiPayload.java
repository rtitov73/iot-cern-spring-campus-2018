package net.rtitov.springcampus.iot;

public class GoogleCloudSpeechApiPayload {
    private String content;
    private String uri;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

package net.rtitov.springcampus.iot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GoogleCloudSpeechApi {
    private final String authKey;
    private final Gson gson;
    private final int sampleRate;
    private final String encoding;
    private final JsonParser parser = new JsonParser();

    public GoogleCloudSpeechApi(String authKey, int sampleRate, String encoding) {
        this.authKey = authKey;
        this.gson = new Gson();
        this.sampleRate = sampleRate;
        this.encoding = encoding;
    }

    public String recognize(byte[] data) {
        System.out.println("Sending " + data.length + " bytes to Google ...");
        GoogleCloudSpeechApiRequest request = new GoogleCloudSpeechApiRequest()
                .withEncoding(encoding)
                .withSampleRate(sampleRate)
                .withContent(data);

        return sendRequestAndGetFirstTranscript(request);
    }

    private String sendRequestAndGetFirstTranscript(GoogleCloudSpeechApiRequest request) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://speech.googleapis.com/v1/speech:recognize?key=" + authKey);

            httpPost.setHeader("Content-Type", "application/json");

            String data = gson.toJson(request);

            httpPost.setEntity(new StringEntity(data));

            CloseableHttpResponse response = client.execute(httpPost);
            String responseEntity = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Error executing Google code: " + responseEntity);
            }
            return getFirstTranscript(responseEntity);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    private String getFirstTranscript(String responseEntity) {
        JsonArray results = (JsonArray) parser.parse(responseEntity).getAsJsonObject().get("results");

        if ((results != null) && results.size() > 0) {
            JsonArray alternatives = (JsonArray) ((JsonObject) results.get(0)).get("alternatives");

            if ((alternatives != null) && alternatives.size() > 0) {
                return (((JsonObject) alternatives.get(0)).get("transcript").getAsString());
            }
        }
        return "";
    }

}

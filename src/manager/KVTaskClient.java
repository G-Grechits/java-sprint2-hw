package manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient client;
    private String url;
    private String api_token;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        this.api_token = registerKey();
    }

    public String load(String key) {
        String tasks = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + api_token))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                tasks = jsonElement.getAsString();
            }
        } catch (NullPointerException | IOException | InterruptedException exception) {
            System.out.println("Во время выполнения запроса произошла ошибка.");
        }
        return tasks;
    }

    public void save(String key, String json) {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + api_token))
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка.");
        }
    }

    public String registerKey() {
        String key = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                key = jsonElement.getAsString();
            }
        } catch (NullPointerException | IOException | InterruptedException exception) {
            System.out.println("KVServer не смог получить апи-ключ.");
        }
        return key;
    }
}
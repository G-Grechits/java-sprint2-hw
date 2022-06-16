package manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private Gson gson;
    private KVServer kvServer;
    private HttpTaskServer taskServer;
    private HttpClient client;

    Task task1 = new Task("Task1", "Task1 description", 1, Status.NEW,60L,
            LocalDateTime.of(2022, Month.JUNE, 1, 12, 0));
    Task task2 = new Task("Task2", "Task2 description", 2, Status.NEW,80L,
            LocalDateTime.of(2022, Month.JUNE, 2, 14, 0));

    Epic epic1 = new Epic("Epic1", "Epic1 description", 3, Status.NEW);
    Epic epic2 = new Epic("Epic2", "Epic2 description", 4, Status.NEW);

    Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description", 5, Status.NEW, 30L,
            LocalDateTime.of(2022, Month.JUNE, 3, 11, 0), epic1.getId());
    Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description", 6, Status.DONE, 40L,
            LocalDateTime.of(2022, Month.JUNE, 1, 17, 0), epic2.getId());
    Subtask subtask3 = new Subtask("Subtask3", "Subtask3 description", 7, Status.NEW, 30L,
            LocalDateTime.of(2022, Month.JUNE, 2, 16, 0), epic2.getId());

    @BeforeEach
    void setUp() throws IOException {
        gson = HttpTaskServer.getGson();
        kvServer = new KVServer();
        taskServer = new HttpTaskServer();
        client = HttpClient.newHttpClient();

        kvServer.start();
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/task/");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlOfGet = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest requestOfGet = HttpRequest.newBuilder().uri(urlOfGet).GET().build();
        HttpResponse<String> responseOfGet = client.send(requestOfGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseOfGet.statusCode());
        assertTrue(responseOfGet.body().contains(task1.getName()));
        assertTrue(responseOfGet.body().contains(task2.getName()));
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        String json1 = gson.toJson(epic1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        String json2 = gson.toJson(epic2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlOfGet = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest requestOfGet = HttpRequest.newBuilder().uri(urlOfGet).GET().build();
        HttpResponse<String> responseOfGet = client.send(requestOfGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseOfGet.statusCode());
        assertTrue(responseOfGet.body().contains(epic1.getName()));
        assertTrue(responseOfGet.body().contains(epic2.getName()));
    }

    @Test
    public void getAllSubtasks() throws IOException, InterruptedException {
        URI urlOfEpic1 = URI.create("http://localhost:8080/tasks/epic/");
        String jsonOfEpic1 = gson.toJson(epic1);
        HttpRequest.BodyPublisher bodyOfEpic1 = HttpRequest.BodyPublishers.ofString(jsonOfEpic1);
        HttpRequest requestOfEpic1 = HttpRequest.newBuilder().uri(urlOfEpic1).POST(bodyOfEpic1).build();
        client.send(requestOfEpic1, HttpResponse.BodyHandlers.ofString());

        URI urlOfEpic2 = URI.create("http://localhost:8080/tasks/epic/");
        String jsonOfEpic2 = gson.toJson(epic2);
        HttpRequest.BodyPublisher bodyOfEpic2 = HttpRequest.BodyPublishers.ofString(jsonOfEpic2);
        HttpRequest requestOfEpic2 = HttpRequest.newBuilder().uri(urlOfEpic2).POST(bodyOfEpic2).build();
        client.send(requestOfEpic2, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/");
        String json1 = gson.toJson(subtask1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/");
        String json2 = gson.toJson(subtask2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/subtask/");
        String json3 = gson.toJson(subtask3);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI urlOfGet = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest requestOfGet = HttpRequest.newBuilder().uri(urlOfGet).GET().build();
        HttpResponse<String> responseOfGet = client.send(requestOfGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseOfGet.statusCode());
        assertTrue(responseOfGet.body().contains(subtask1.getName()));
        assertTrue(responseOfGet.body().contains(subtask2.getName()));
        assertTrue(responseOfGet.body().contains(subtask3.getName()));
    }

    @Test
    public void getTaskWithWrongBody() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlOfGet = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest requestOfGet = HttpRequest.newBuilder().uri(urlOfGet).GET().build();
        HttpResponse<String> responseOfGet = client.send(requestOfGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseOfGet.statusCode());
        assertFalse(responseOfGet.body().contains("Task2"));
    }

    @Test
    public void getEpicWithWrongBody() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlOfGet = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest requestOfGet = HttpRequest.newBuilder().uri(urlOfGet).GET().build();
        HttpResponse<String> responseOfGet = client.send(requestOfGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseOfGet.statusCode());
        assertFalse(responseOfGet.body().contains("Epic2"));
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(task1.getName()));
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(epic1.getName()));
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        URI urlOfEpic = URI.create("http://localhost:8080/tasks/epic/");
        String jsonOfEpic = gson.toJson(epic1);
        HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(jsonOfEpic);
        HttpRequest requestOfEpic = HttpRequest.newBuilder().uri(urlOfEpic).POST(bodyOfEpic).build();
        client.send(requestOfEpic, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(subtask1.getName()));
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        URI urlOfTask = URI.create("http://localhost:8080/tasks/task/");
        String jsonOfTask = gson.toJson(task1);
        HttpRequest.BodyPublisher bodyOfTask = HttpRequest.BodyPublishers.ofString(jsonOfTask);
        HttpRequest requestOfTask = HttpRequest.newBuilder().uri(urlOfTask).POST(bodyOfTask).build();
        client.send(requestOfTask, HttpResponse.BodyHandlers.ofString());

        URI urlOfEpic = URI.create("http://localhost:8080/tasks/epic/");
        String jsonOfEpic = gson.toJson(epic1);
        HttpRequest.BodyPublisher bodyOfEpic = HttpRequest.BodyPublishers.ofString(jsonOfEpic);
        HttpRequest requestOfEpic = HttpRequest.newBuilder().uri(urlOfEpic).POST(bodyOfEpic).build();
        client.send(requestOfEpic, HttpResponse.BodyHandlers.ofString());

        URI urlOfSubtask = URI.create("http://localhost:8080/tasks/subtask/");
        String jsonOfSubtask = gson.toJson(subtask1);
        HttpRequest.BodyPublisher bodyOfSubtask = HttpRequest.BodyPublishers.ofString(jsonOfSubtask);
        HttpRequest requestOfSubtask = HttpRequest.newBuilder().uri(urlOfSubtask).POST(bodyOfSubtask).build();
        client.send(requestOfSubtask, HttpResponse.BodyHandlers.ofString());

        URI urlOfPrioritized = URI.create("http://localhost:8080/tasks/");
        HttpRequest requestOfPrioritized = HttpRequest.newBuilder().uri(urlOfPrioritized).GET().build();
        HttpResponse<String> responseOfPrioritized = client.send(requestOfPrioritized, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseOfPrioritized.statusCode());
        assertTrue(responseOfPrioritized.body().contains(task1.getName()));
        assertFalse(responseOfPrioritized.body().contains(epic1.getName()));
        assertTrue(responseOfPrioritized.body().contains(subtask1.getName()));
    }
}
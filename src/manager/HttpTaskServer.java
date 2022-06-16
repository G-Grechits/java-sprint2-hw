package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static Gson gson = getGson();
    private static final int PORT = 8080;
    private final HttpServer server;
    private static TaskManager manager;
    private static String response;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", new TaskHandler());
        server.createContext("/tasks/epic/", new EpicHandler());
        server.createContext("/tasks/subtask/", new SubtaskHandler());
        server.createContext("/tasks/history/", new HistoryHandler());
        server.createContext("/tasks/", new PrioritizedTasksHandler());
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Task task = manager.getTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson(task).getBytes(DEFAULT_CHARSET));
                        }
                    } else if (path.endsWith("task") || path.endsWith("task/")) {
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(gson.toJson(manager.getAllTasks()).getBytes(DEFAULT_CHARSET));
                            }
                    } else {
                        response = "Некорректный запрос.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    if (task != null) {
                        boolean isNew = manager.getAllTasks().stream()
                                .noneMatch((eachTask -> eachTask.getId() == task.getId()));
                        if (isNew) {
                            manager.createTask(task);
                            response = "Задача '" + gson.fromJson(body, Task.class).getName() + "' создана.";
                        } else {
                            manager.updateTask(task);
                            response = "Задача '" + gson.fromJson(body, Task.class).getName() + "' обновлена.";
                        }
                        exchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        response = "Некорректное тело запроса.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "DELETE":
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        if (manager.getTaskById(id) != null) {
                            manager.removeTaskById(id);
                            response = "Задача с id=" + id + " удалена.";
                            exchange.sendResponseHeaders(201, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        } else {
                            response = "Задача с id=" + id + " не существует.";
                            exchange.sendResponseHeaders(404, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }
                    } else if (path.endsWith("task") || path.endsWith("task/")) {
                        manager.removeAllTasks();
                        response = "Все задачи удалены.";
                        exchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                default:
                    response = "Выберите запрос GET, POST или DELETE.";
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
            }
        }
    }

    static class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Epic epic = manager.getEpicById(id);
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson(epic).getBytes(DEFAULT_CHARSET));
                        }
                    } else if (path.endsWith("epic") || path.endsWith("epic/")) {
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson(manager.getAllEpics()).getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        response = "Некорректный запрос.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic != null) {
                        boolean isNew = manager.getAllEpics().stream()
                                .noneMatch((eachEpic -> eachEpic.getId() == epic.getId()));
                        if (isNew) {
                            manager.createEpic(epic);
                            response = "Эпик '" + gson.fromJson(body, Epic.class).getName() + "' создан.";
                        } else {
                            manager.updateEpic(epic);
                            response = "Эпик '" + gson.fromJson(body, Epic.class).getName() + "' обновлён.";
                        }
                        exchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        response = "Некорректное тело запроса.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "DELETE":
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        if (manager.getEpicById(id) != null) {
                            manager.removeEpicById(id);
                            response = "Эпик с id=" + id + " удалён.";
                            exchange.sendResponseHeaders(201, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        } else {
                            response = "Эпик с id=" + id + " не существует.";
                            exchange.sendResponseHeaders(404, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }
                    } else if (path.endsWith("epic") || path.endsWith("epic/")) {
                        manager.removeAllEpics();
                        response = "Все эпики удалены.";
                        exchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                default:
                    response = "Выберите запрос GET, POST или DELETE.";
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    if (query != null && !path.contains("epic") && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Subtask subtask = manager.getSubtaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson(subtask).getBytes(DEFAULT_CHARSET));
                        }
                    } else if (path.endsWith("subtask") || path.endsWith("subtask/")) {
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(gson.toJson(manager.getAllSubtasks()).getBytes(DEFAULT_CHARSET));
                        }
                    } else if (query != null && path.contains("epic")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        if (manager.getEpicById(id) == null) {
                            response = "Эпик с id=" + id + " не существует.";
                            exchange.sendResponseHeaders(500, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        } else {
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(gson.toJson(manager.getSubtasksByEpic(id)).getBytes(DEFAULT_CHARSET));
                            }
                        }
                    } else {
                        response = "Некорректный запрос.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask != null) {
                        if (manager.getEpicById(subtask.getEpicId()) != null) {
                            boolean isNew = manager.getAllSubtasks().stream()
                                    .noneMatch((eachSubtask -> eachSubtask.getId() == subtask.getId()));
                            if (isNew) {
                                manager.createSubtask(subtask);
                                response = "Подзадача '" + gson.fromJson(body, Subtask.class).getName() + "' создана.";
                            } else {
                                manager.updateSubtask(subtask);
                                response = "Подзадача '" + gson.fromJson(body, Subtask.class).getName() + "' обновлена.";
                            }
                            exchange.sendResponseHeaders(201, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        } else {
                            response = "Эпик с id=" + subtask.getEpicId() + " не существует.";
                            exchange.sendResponseHeaders(500, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }
                    } else {
                        response = "Некорректное тело запроса.";
                        exchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                case "DELETE":
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        if (manager.getSubtaskById(id) != null) {
                            manager.removeSubtaskById(id);
                            response = "Подзадача с id=" + id + " удалена.";
                            exchange.sendResponseHeaders(201, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        } else {
                            response = "Подзадача с id=" + id + " не существует.";
                            exchange.sendResponseHeaders(404, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }
                    } else if (path.endsWith("subtask") || path.endsWith("subtask/")) {
                        manager.removeAllSubtasks();
                        response = "Все подзадачи удалены.";
                        exchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                    break;
                default:
                    response = "Выберите запрос GET, POST или DELETE.";
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
            }
        }
    }

    static class HistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                if (path.endsWith("history") || path.endsWith("history/")) {
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(manager.getHistory()).getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    response = "Некорректный запрос.";
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
            } else {
                response = "Выберите запрос GET.";
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        }
    }

    static class PrioritizedTasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                if (path.endsWith("tasks") || path.endsWith("tasks/")) {
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(gson.toJson(manager.getPrioritizedTasks()).getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    response = "Некорректный запрос.";
                    exchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
            } else {
                response = "Выберите запрос GET.";
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        }
    }

    public void stop() {
        server.stop(1);
    }
}
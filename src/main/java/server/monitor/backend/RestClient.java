package server.monitor.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestClient {
    public Printer printer;
    public TaskManager taskManager;
    public RestClient(Printer printer, TaskManager taskManager) {
        this.taskManager = taskManager;
        this.printer = printer;
    }

    @PostMapping("/set/players")
    public TasksResponse players(@RequestBody Map<UUID, Player> body) {
        System.out.println("REQUEST RECEIVED, players: " + body.size());
        Players.convert(body);
        printer.printPlayers();
        return new TasksResponse(taskManager.getTasks());
    }
    @PostMapping("/set/task")
    public void taskRecieve(@RequestBody TaskRecieved body) {
        System.out.println("TASKS RECEIVED, players: " + body.toString());
        taskManager.addTask(body);
        printer.printPlayers();
    }
    @PostMapping("/command/status")
    public void taskStatus(@RequestBody TaskStatus body) {
        System.out.println("TASKSTATUS RECEIVED, players: " + body.toString());
        if (body.success()) {
            taskManager.clear(body.id);
        }
        printer.printPlayers();
    }
    public record TaskRecieved(String type, UUID player_id, Map<String,String> arguments) {
    }
    public record TasksResponse(Map<UUID, Task> tasks) {}
    public record TaskStatus(UUID id, boolean success) {}
}

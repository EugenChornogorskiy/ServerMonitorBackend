package server.monitor.backend.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.monitor.backend.Printer;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;
import server.monitor.backend.task.Task;
import server.monitor.backend.task.TaskManager;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskClient {
    public Printer printer;
    public TaskManager taskManager;
    public TaskClient(Printer printer, TaskManager taskManager) {
        this.taskManager = taskManager;
        this.printer = printer;
    }
    @PostMapping("/set")
    public void taskRecieve(@RequestBody TaskRecieved body) {
        System.out.println("TASKS RECEIVED, players: " + body.toString());
        taskManager.addTask(body);
        printer.printPlayers();
    }

    @PostMapping("/status")
    public void taskStatus(@RequestBody TaskStatus body) {
        System.out.println("TASKSTATUS RECEIVED, players: " + body.toString());
        if (body.success()) {
            taskManager.clear(body.id);
        }
        printer.printPlayers();
    }

    public record TaskRecieved(String type, UUID player_id, Map<String,String> arguments) {
    }
    public record TaskStatus(UUID id, boolean success) {}
}

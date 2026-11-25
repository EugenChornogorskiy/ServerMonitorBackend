package server.monitor.backend.client;

import org.springframework.web.bind.annotation.*;
import server.monitor.backend.*;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;
import server.monitor.backend.services.PlayerService;
import server.monitor.backend.task.Task;
import server.monitor.backend.task.TaskManager;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private final Printer printer;
    private final TaskManager taskManager;
    private final PlayerService playerService;
    private final Players players;
    public PlayerController(Printer printer, TaskManager taskManager,  PlayerService playerService, Players players) {
        this.taskManager = taskManager;
        this.printer = printer;
        this.playerService = playerService;
        this.players = players;

    }

    @PostMapping("/set")
    public TasksResponse players(@RequestBody Map<UUID, Player> body) {
        System.out.println("REQUEST RECEIVED, players: " + body.size());
        players.convert(body);
        printer.printPlayers();
        return new TasksResponse(taskManager.getTasks());
    }
    @PostMapping("/authorize")
    public void authorizePlayer(@RequestBody AuthorizePlayer body) {
        System.out.println("AUTHORIZE PLAYER RECEIVED, players: " + body.toString());
        playerService.authorizePlayer(body);
    }
    public record AuthorizePlayer(UUID id, String code) {}
    public record TasksResponse(Map<UUID, Task> tasks) {}
}

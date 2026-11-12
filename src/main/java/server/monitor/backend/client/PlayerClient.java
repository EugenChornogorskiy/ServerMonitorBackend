package server.monitor.backend.client;

import org.springframework.web.bind.annotation.*;
import server.monitor.backend.*;
import server.monitor.backend.db.claims.ClaimRequest;
import server.monitor.backend.db.claims.ClaimResponse;
import server.monitor.backend.db.items.Item;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;
import server.monitor.backend.services.ClaimService;
import server.monitor.backend.task.Task;
import server.monitor.backend.task.TaskManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/player")
public class PlayerClient {
    public Printer printer;
    public TaskManager taskManager;
    private final UserRepository userRepository;
    public PlayerClient(Printer printer, TaskManager taskManager,UserRepository userRepository) {
        this.taskManager = taskManager;
        this.printer = printer;
        this.userRepository = userRepository;
    }

    @PostMapping("/set")
    public TasksResponse players(@RequestBody Map<UUID, Player> body) {
        System.out.println("REQUEST RECEIVED, players: " + body.size());
        Players.convert(body);
        printer.printPlayers();
        return new TasksResponse(taskManager.getTasks());
    }
    @PostMapping("/authorize")
    public Boolean authorizePlayer(@RequestBody AuthorizePlayer body) {
        System.out.println("AUTHORIZE PLAYER RECEIVED, players: " + body.toString());
        Optional<User> user = userRepository.findByCode(body.code());
        if (user.isPresent()) {
            user.get().setPlayer_id(body.id());
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
    public record AuthorizePlayer(UUID id, String code) {}
    public record TasksResponse(Map<UUID, Task> tasks) {}
}

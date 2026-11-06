package server.monitor.backend;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestClient {
    public Printer printer;
    public RestClient(Printer printer) {
        this.printer = printer;
    }

    @PostMapping("/set/players")
    public void players(@RequestBody Map<UUID, Player> body) {
        System.out.println("REQUEST RECEIVED, players: " + body.size());
        Players.convert(body);
        printer.printPlayers();
    }
}

package server.monitor.backend;

import org.springframework.stereotype.Component;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;

@Component
public class Printer {
    public void printPlayers() {
        for (Player player : Players.getPlayers()) {
            System.out.println(player.toString());
        }
    }
}

package server.monitor.backend;

import org.springframework.stereotype.Component;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;

@Component
public class Printer {
    private final Players players;
    public Printer(Players players){
        this.players = players;
    }
    public void printPlayers() {
        for (Player player : players.getPlayers()) {
            System.out.println(player.toString());
        }
    }
}

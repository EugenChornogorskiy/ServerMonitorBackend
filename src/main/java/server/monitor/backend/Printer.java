package server.monitor.backend;

import org.springframework.stereotype.Component;

@Component
public class Printer {
    public void printPlayers() {
        for (Player player : Players.getPlayers()) {
            System.out.println(player.toString());
        }
    }
}

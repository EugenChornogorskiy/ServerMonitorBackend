package server.monitor.backend;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class Players {
    private static List<Player> players;

    public static List<Player> getPlayers() {
        return List.copyOf(players);
    }

    public static void convert(Map<UUID, Player> body) {
        List<Player> converted = new ArrayList<>();
        for(Map.Entry<UUID,Player> mapa : body.entrySet()) {
            converted.add(mapa.getValue());
        }
        Players.players = converted;
    }
}

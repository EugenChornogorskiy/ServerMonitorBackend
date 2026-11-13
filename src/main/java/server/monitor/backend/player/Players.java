package server.monitor.backend.player;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Players {
    private static List<Player> players = Collections.synchronizedList(new ArrayList<>());

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

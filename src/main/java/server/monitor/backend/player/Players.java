package server.monitor.backend.player;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class Players {
    private final Duration TTL;
    private List<Player> players =new ArrayList<>();
    private volatile Instant lastUpdate = Instant.EPOCH;
    public Players(@Value("server-monitor.ttl") int ttl){
        this.TTL = Duration.ofSeconds(ttl);
    }
    public List<Player> getPlayers() {
        return isAlive() ?  List.copyOf(players) : List.of();
    }

    public void convert(Map<UUID, Player> body) {
        players = List.copyOf(body.values());
        lastUpdate = Instant.now();
    }
    private boolean isAlive() {
        return Duration.between(lastUpdate, Instant.now()).compareTo(TTL) < 0;
    }
}

package server.monitor.backend.player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Player(UUID id, String nickname, LocalDateTime joinTime, Position position, List<PlayerItem> inventory, int kills, int deaths) {

    public record Position(double x, double y, double z) {
    }
    public record PlayerItem(int position, String material, int amount) {}
}
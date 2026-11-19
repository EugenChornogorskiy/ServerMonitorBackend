package server.monitor.backend.db.users;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "player_id", unique = true)
    private UUID playerId;
    private Long money_spend;
    private String nickname;
    private String email;
    private String code;

    public User(Long id, UUID playerId, Long money_spend, String nickname, String email, String code) {
        this.id = id;
        this.playerId = playerId;
        this.money_spend = money_spend;
        this.nickname = nickname;
        this.email = email;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID player_id) {
        this.playerId = player_id;
    }

}

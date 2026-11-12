package server.monitor.backend.db.users;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID player_id;
    private Long money_spend;
    private String nickname;
    private String email;
    private String code;

    public Long getMoney_spend() {
        return money_spend;
    }

    public void setMoney_spend(Long money_spend) {
        this.money_spend = money_spend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(UUID player_id) {
        this.player_id = player_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

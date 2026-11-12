package server.monitor.backend.db.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCode(String code);
    Optional<User> findByPlayer_id(UUID id);
}
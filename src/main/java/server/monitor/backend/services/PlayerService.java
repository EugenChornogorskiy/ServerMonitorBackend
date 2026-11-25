package server.monitor.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import server.monitor.backend.client.PlayerController;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class PlayerService {
    private final UserRepository userRepository;
    public PlayerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void authorizePlayer(PlayerController.AuthorizePlayer authorizePlayer) {
        Optional<User> user = userRepository.findByCode(authorizePlayer.code());
        if (user.isPresent()) {
            user.get().setPlayerId(authorizePlayer.id());
            userRepository.save(user.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }
    }
}

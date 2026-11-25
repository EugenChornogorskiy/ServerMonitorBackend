package server.monitor.backend;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import server.monitor.backend.client.PlayerController;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.services.PlayerService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class PlayerServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PlayerService playerService = new PlayerService(userRepository);
    private final User user = new User(1L,null,0L,"Riven","player@email.com","1111");


    @Test
    void authorizeUnknownPlayer() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByCode("1111")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                playerService.authorizePlayer(new PlayerController.AuthorizePlayer(id, "1111"))
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
    @Test
    void authorizePlayer() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByCode("1111")).thenReturn(Optional.of(user));
        playerService.authorizePlayer(new PlayerController.AuthorizePlayer(id,user.getCode()));
        assertEquals(id,user.getPlayerId());
    }
}
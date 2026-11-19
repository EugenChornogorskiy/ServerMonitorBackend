package server.monitor.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import server.monitor.backend.client.PlayerController;
import server.monitor.backend.db.purchases.PurchaseRepository;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.player.Player;
import server.monitor.backend.player.Players;
import server.monitor.backend.task.TaskManager;

import java.util.Optional;
import java.util.UUID;


@WebMvcTest(PlayerController.class)
@Import({Printer.class, TaskManager.class})
class PlayerControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskManager taskManager;

    @BeforeEach
    void resetTasks() {
        taskManager.getTasks().keySet().forEach(taskManager::clear);
    }

    @Test
    void acceptsPlayers() throws Exception {
        String json = """
                {
                  "ebeb55c1-bc68-4a92-8eda-3bedeaa6e676": {
                    "id": "ebeb55c1-bc68-4a92-8eda-3bedeaa6e676",
                    "nickname": "Riven1777",
                    "joinTime": "2026-11-06T19:35:19",
                    "position": {"x": 1.38, "y": 76.0, "z": 11.47},
                    "inventory": [{"position": 0, "material": "OAK_LOG", "amount": 1}],
                    "kills": 0,
                    "deaths": 1
                  }
                }
                """;

        mvc.perform(post("/api/player/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        Player p = Players.getPlayers().getFirst();
        assertAll(
                () -> assertEquals(1, Players.getPlayers().size()),
                () -> assertEquals("Riven1777", p.nickname()),
                () -> assertEquals(1, p.deaths()),
                () -> assertEquals(0, p.kills())
        );
    }
    @Test
    void authorizeUnknownPlayer() throws Exception {
        String json = """
                {

                    "id": "ebeb55c1-bc68-4a92-8eda-3bedeaa6e676",
                    "code": "1111"

                }
                """;

        mvc.perform(post("/api/player/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
    @Test
    void authorizePlayerSuccess() throws Exception {

        String json = """
                {
                    "id": "ebeb55c1-bc68-4a92-8eda-3bedeaa6e676",
                    "code": "1111"
                }
                """;
        when(userRepository.findByCode("1111")).thenReturn(Optional.of(new User(1L, UUID.fromString("ebeb55c1-bc68-4a92-8eda-3bedeaa6e676"),0L,"Riven","email","1111")));
        mvc.perform(post("/api/player/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
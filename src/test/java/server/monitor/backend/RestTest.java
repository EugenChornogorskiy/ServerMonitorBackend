package server.monitor.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

@WebMvcTest(RestClient.class)
@Import({Printer.class,TaskManager.class})
class RestTest {

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
                    "inventory": [{"position": 0, "name": "OAK_LOG", "count": 1}],
                    "kills": 0,
                    "deaths": 1
                  }
                }
                """;

        mvc.perform(post("/api/set/players")
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
    void acceptsTasks() throws Exception {
        String json = """
            {
              "type": "KICK",
              "player_id": "ebeb55c1-bc68-4a92-8eda-3bedeaa6e672",
              "arguments": {}
            }
            """;

        mvc.perform(post("/api/set/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(1, taskManager.getTasks().size());
        Task saved = taskManager.getTasks().values().iterator().next();
        assertAll(
                () -> assertEquals(Task.Type.KICK, saved.type()),
                () -> assertEquals(UUID.fromString("ebeb55c1-bc68-4a92-8eda-3bedeaa6e672"), saved.player_id()),
                () -> assertTrue(saved.arguments().isEmpty())
        );
    }
    @Test
    void clearRemovesTask() throws Exception {
        String json = """
            {
              "type": "KICK",
              "player_id": "ebeb55c1-bc68-4a92-8eda-3bedeaa6e672",
              "arguments": {}
            }
            """;

        mvc.perform(post("/api/set/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(1, taskManager.getTasks().size());
        Task saved = taskManager.getTasks().values().iterator().next();
        taskManager.clear(saved.id());
        assertEquals(0, taskManager.getTasks().size());
    }
    @Test
    void acceptsTaskStatus() throws Exception {
        taskManager.addTask(new RestClient.TaskRecieved("KICK",UUID.fromString("ebeb55c1-bc68-4a92-8eda-3bedeaa6e672"),Map.of()));
        Task saved = taskManager.getTasks().values().iterator().next();

        String json = """
        {
          "id": "%s",
          "success": true
        }
        """.formatted(saved.id());

        mvc.perform(post("/api/command/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(0, taskManager.getTasks().size());
    }
}
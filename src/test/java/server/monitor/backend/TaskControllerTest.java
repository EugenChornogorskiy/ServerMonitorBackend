package server.monitor.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import server.monitor.backend.client.TaskController;
import server.monitor.backend.db.purchases.PurchaseRepository;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.player.Players;
import server.monitor.backend.task.Task;
import server.monitor.backend.task.TaskManager;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({Printer.class, TaskManager.class})
class TaskControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PurchaseRepository purchaseRepository;

    @MockitoBean
    private Players players;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskManager taskManager;

    @BeforeEach
    void resetTasks() {
        taskManager.getTasks().keySet().forEach(taskManager::clear);
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

        mvc.perform(post("/api/task/set")
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

        mvc.perform(post("/api/task/set")
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
        taskManager.addTask(new TaskController.TaskRecieved("KICK",UUID.fromString("ebeb55c1-bc68-4a92-8eda-3bedeaa6e672"),Map.of()));
        Task saved = taskManager.getTasks().values().iterator().next();

        String json = """
        {
          "id": "%s",
          "success": true
        }
        """.formatted(saved.id());

        mvc.perform(post("/api/task/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(0, taskManager.getTasks().size());
    }
}
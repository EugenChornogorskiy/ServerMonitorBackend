package server.monitor.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import server.monitor.backend.client.PurchaseController;
import server.monitor.backend.db.purchases.PurchaseRepository;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.player.Players;
import server.monitor.backend.services.ClaimService;
import server.monitor.backend.task.TaskManager;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
@Import({Printer.class, TaskManager.class})
class PurchaseControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PurchaseRepository purchaseRepository;

    @MockitoBean
    private Players players;

    @MockitoBean
    private ClaimService claimService;

    @Autowired
    private MockMvc mvc;


    @Test
    void getPurchase() throws Exception {
        UUID id = UUID.fromString("ebeb55c1-bc68-4a92-8eda-3bedeaa6e672");


        mvc.perform(get("/api/purchase/get")
                        .param("id", id.toString())
                        .param("code", "1111"))
                .andExpect(status().isOk());
    }
    @Test
    void closePurchase() throws Exception {
        String json = """
                {
                  "ebeb55c1-bc68-4a92-8eda-3bedeaa6e676": "SUCCESS",
                  "id": 1
                }
                """;

        mvc.perform(post("/api/purchase/close")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
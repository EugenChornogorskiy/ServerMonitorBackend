package server.monitor.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RestClient.class)
@Import(Printer.class)
class RestTest {

    @Autowired
    private MockMvc mvc;

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
}
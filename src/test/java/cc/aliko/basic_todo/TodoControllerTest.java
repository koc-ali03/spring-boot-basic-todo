package cc.aliko.basic_todo;

import cc.aliko.basic_todo.dto.TodoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TodoRequest todoRequest;
    private String uniqueTitle;

    @BeforeEach
    void setup() {
        uniqueTitle = "Learn Spring Boot " + System.currentTimeMillis();
        todoRequest = new TodoRequest(uniqueTitle, false);
    }

    @Test
    void testCrudAndExceptions() throws Exception {
        // 1. CREATE Todo
        String todoJson = objectMapper.writeValueAsString(todoRequest);

        String response = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(uniqueTitle))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Cevaptan ID elde et
        long id = objectMapper.readTree(response).get("id").asLong();

        // 2. GET (full)
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", hasItem(uniqueTitle)));

        // 3. GET (ID üzerinden)
        mockMvc.perform(get("/api/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(uniqueTitle));

        // 4. UPDATE Todo
        TodoRequest updateRequest = new TodoRequest("Spring Boot Rocks " + System.currentTimeMillis(), true);
        String updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/todos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", containsString("Spring Boot Rocks")))
                .andExpect(jsonPath("$.completed").value(true));

        // 5. DUPLICATE CREATE (hata vermeli)
        String duplicateJson = objectMapper.writeValueAsString(updateRequest);
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("zaten var")));

        // 6. DELETE Todo
        mockMvc.perform(delete("/api/todos/" + id))
                .andExpect(status().isNoContent());

        // 7. GET Todo -> NotFoundException
        mockMvc.perform(get("/api/todos/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("bulunamadı")));
    }
}

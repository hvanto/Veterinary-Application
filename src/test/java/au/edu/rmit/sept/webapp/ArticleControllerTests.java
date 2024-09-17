package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addAndRemoveArticleTest() throws Exception {
        // Add the article
        mockMvc.perform(post("/article/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Sample Title")
                .param("link", "https://www.google.com")
                .param("description", "Sample Description")
                .param("author", "John Doe")
                .param("published_date", "2024-09-12")
                .param("image_url", "https://example.com/image.jpg"))
                .andExpect(status().isOk());
    
        // Now remove the article by id
        mockMvc.perform(delete("/article/remove")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk());
    }
}

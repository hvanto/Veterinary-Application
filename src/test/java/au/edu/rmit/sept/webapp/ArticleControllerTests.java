package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

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

    @Test
    public void addAndRemoveBookmarkTest() throws Exception {
        // Add user if db is empty
        if (!userService.findFirst().isPresent()) {
            User user = new User();
            user.setFirstName("Firstname");
            user.setLastName("Lastname");
            user.setEmail("email@gmail.com");
            user.setPassword("password");
            userService.saveUser(new User());
        }

        // Create the article JSON payload
        String articleJson = "{"
                + "\"title\":\"Sample Title\","
                + "\"link\":\"https://www.google.com\","
                + "\"author\":\"John Doe\","
                + "\"publishedDate\":\"2024-09-12\","
                + "\"description\":\"Sample Description\","
                + "\"imageUrl\":\"https://example.com/image.jpg\""
                + "}";

        // Add the bookmark
        mockMvc.perform(post("/addBookmark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(articleJson))
                .andExpect(status().isOk());

        // Remove the bookmark
        mockMvc.perform(post("/removeBookmark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(articleJson))
                .andExpect(status().isOk());
    }
}

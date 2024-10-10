package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.controller.FAQController;
import au.edu.rmit.sept.webapp.model.FAQ;
import au.edu.rmit.sept.webapp.repository.FAQRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FAQController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FAQControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FAQRepository faqRepository;


    @Test
    public void testGetFaqByIdSuccess() throws Exception {
        // Set up testing data
        List<FAQ> faqs = Arrays.asList(
                new FAQ("Question 1", "Answer 1", "general"),
                new FAQ("Question 2", "Answer 2", "general")
        );

        String category = "general";

        // Mock faq repository behavior
        given(faqRepository.findByCategory(category)).willReturn(faqs);

        mockMvc.perform(get("/faq/{category}", category))
                .andExpect(status().isOk())
                // Check if faqs list returned has the correct size
                .andExpect(model().attribute("faqs", hasSize(2)))
                // Check if faqs list returned has the correct category
                .andExpect(model().attribute("category", equalTo(category)))
                .andExpect(model().attribute("isEmpty", false))
                .andExpect(view().name("index"));
    }


    @Test
    public void testGetFaqByIdEmpty() throws Exception {
        // Set up testing data
        List<FAQ> faqs = Collections.emptyList();

        String category = "general";

        // Mock faq repository behavior
        given(faqRepository.findByCategory(category)).willReturn(faqs);

        mockMvc.perform(get("/faq/{category}", category))
                .andExpect(status().isOk())
                // Check if faqs list returned is empty
                .andExpect(model().attribute("faqs", hasSize(0)))
                // Check if isEmpty attribute is true
                .andExpect(model().attribute("isEmpty", true))
                .andExpect(view().name("index"));
    }


    @Test
    public void testGetFaqByCategoryFail() throws Exception {
        String category = "general";

        // Throw exception when repository findByCategory is called
        Mockito.doThrow(new RuntimeException("Database error"))
                .when(faqRepository).findByCategory(category);

        mockMvc.perform(get("/faq/{category}", category))
                .andExpect(status().isOk())
                // Check if there is an attribute named error
                .andExpect(model().attributeExists("error"))
                //.andExpect(model().attribute("error", "An error occurred while retrieving FAQs."))
                .andExpect(view().name("index"));
    }

}

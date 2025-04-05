package scitech.newsservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scitech.newsservice.controllers.NewsObjectController;
import scitech.newsservice.models.dto.NewsDto;
import scitech.newsservice.services.NewsObjectService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsObjectController.class)
class NewsObjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsObjectService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final NewsDto dummyNews = new NewsDto(
            1L,
            100L,
            "Science",
            "PUBLISHED",
            "Test Title",
            "Short description",
            "Test content",
            "http://example.com",
            42,
            128,
            List.of("science", "space"),
            LocalDate.of(2024, 4, 5)
    );

    @Test
    void getByTitle_shouldReturnNewsDto() throws Exception {
        Mockito.when(newsService.findByTitle("Test Title"))
                .thenReturn(dummyNews);

        mockMvc.perform(get("/api/news/by-title")
                        .param("title", "Test Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void getByDateRange_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findByDateRange(any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/by-date-range")
                        .param("start", "2024-01-01")
                        .param("end", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));
    }

    @Test
    void getByTags_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findByTags(anyList(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/by-tags")
                        .param("tags", "science", "space"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));
    }

    @Test
    void getByTheme_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findByTheme(eq("Science"), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/by-theme")
                        .param("theme", "Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));
    }

    @Test
    void getByAuthor_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findByOwnerId(eq(100L), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/by-author")
                        .param("ownerId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ownerId").value(100));
    }

    @Test
    void searchNews_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findAllWithFilters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/search")
                        .param("title", "Test Title")
                        .param("sort", "dateOfCreation,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));
    }

    @Test
    void createNews_shouldReturnCreatedNewsDto() throws Exception {
        Mockito.when(newsService.createNews(any()))
                .thenReturn(dummyNews);

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyNews)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void getPopularNews_shouldReturnPagedNews() throws Exception {
        Page<NewsDto> page = new PageImpl<>(List.of(dummyNews));
        Mockito.when(newsService.findPopularNews(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/news/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));
    }
}


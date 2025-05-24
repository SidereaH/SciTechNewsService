package scitech.newsservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import scitech.newsservice.controllers.StatsController;
import scitech.newsservice.services.NewsObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NewsObjectService newsObjectService;

//    @Test
//    @DisplayName("PATCH /api/news/stats/add-show - should increment shows and return value")
//    void testAddShow() throws Exception {
//        Mockito.when(newsObjectService.addShows(anyLong())).thenReturn(11);
//
//        mockMvc.perform(patch("/api/news/stats/add-show")
//                        .param("newsId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("11"));
//    }
//
//    @Test
//    @DisplayName("PATCH /api/news/stats/add-like - should increment likes and return value")
//    void testAddLike() throws Exception {
//        Mockito.when(newsObjectService.addLikes(anyLong())).thenReturn(5);
//
//        mockMvc.perform(patch("/api/news/stats/add-like")
//                        .param("newsId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("5"));
//    }
}

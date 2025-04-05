package scitech.newsservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scitech.newsservice.models.dto.StatsResponseDto;
import scitech.newsservice.repositories.NewsObjectRepo;
import scitech.newsservice.services.NewsObjectService;

@RestController
@RequestMapping("/api/news/stats")
@Tag(name = "News Stats API", description = "Ведение статистики по новостям")
public class StatsController {
    private final NewsObjectService newsObjectService;

    @Autowired
    public StatsController(NewsObjectService newsObjectService) {
        this.newsObjectService = newsObjectService;
    }

    @Operation(
            summary = "Увеличить счетчик просмотров новости",
            description = "Увеличивает на 1 количество показов указанной новости",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Новое значение счетчика просмотров"),
                    @ApiResponse(responseCode = "404", description = "Новость с указанным ID не найдена")
            }
    )
    @PatchMapping("/add-show")
    public ResponseEntity<Integer> addShow(
            @Parameter(description = "ID новости", required = true, example = "1")
            @RequestParam Long newsId) {
        return ResponseEntity.ok(newsObjectService.addShows(newsId));
    }

    @Operation(
            summary = "Увеличить счетчик лайков новости",
            description = "Увеличивает на 1 количество лайков указанной новости",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Новое значение счетчика лайков"),
                    @ApiResponse(responseCode = "404", description = "Новость с указанным ID не найдена")
            }
    )
    @PatchMapping("/add-like")
    public ResponseEntity<StatsResponseDto> addLike(
            @Parameter(description = "ID новости", required = true, example = "1")
            @RequestParam Long newsId) {
        Integer newsLikes;
        try{
            newsLikes = newsObjectService.addLikes(newsId);
        }
            catch(EntityNotFoundException e){
                return ResponseEntity.badRequest().body(
                        new StatsResponseDto(
                                e.getMessage(), false
                        )
                );
        }
        return ResponseEntity.ok(new StatsResponseDto(newsLikes.toString(),true));
    }
}
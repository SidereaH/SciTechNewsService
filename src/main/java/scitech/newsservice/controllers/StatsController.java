package scitech.newsservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scitech.newsservice.models.dto.StatsResponseDto;
import scitech.newsservice.services.NewsObjectService;

@RestController
@RequestMapping("/api/news/stats")
@Tag(
        name = "News Stats API",
        description = "API для управления статистикой новостей. Позволяет увеличивать счетчики просмотров и лайков новостей."
)
public class StatsController {
    private final NewsObjectService newsObjectService;


    @Autowired
    public StatsController(NewsObjectService newsObjectService) {
        this.newsObjectService = newsObjectService;
    }

    @Operation(
            summary = "Увеличить счетчик просмотров новости",
            description = "Увеличивает счетчик просмотров указанной новости на 1. Возвращает обновленное количество просмотров.",
            parameters = {
                    @Parameter(
                            name = "newsId",
                            description = "Уникальный идентификатор новости",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "long")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Счетчик просмотров успешно увеличен",
                            content = @Content(
                                    schema = @Schema(type = "integer"),
                                    examples = @ExampleObject(value = "42")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Новость не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "\"Новость с ID 999 не найдена\"")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный запрос",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "\"Неверный формат ID новости\"")
                            )
                    )
            }
    )
    @PatchMapping("/add-show/{newsId}")
    public ResponseEntity<Integer> addShow(@PathVariable Long newsId) {
        return ResponseEntity.ok(newsObjectService.addShows(newsId));
    }

    @Operation(
            summary = "Увеличить счетчик лайков новости",
            description = "Увеличивает счетчик лайков указанной новости на 1. Возвращает объект с результатом операции.",
            parameters = {
                    @Parameter(
                            name = "newsId",
                            description = "Уникальный идентификатор новости",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "long")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Счетчик лайков успешно увеличен",
                            content = @Content(
                                    schema = @Schema(implementation = StatsResponseDto.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"5\",\"success\":true}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Новость не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = StatsResponseDto.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"Новость с ID 999 не найдена\",\"success\":false}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный запрос",
                            content = @Content(
                                    schema = @Schema(implementation = StatsResponseDto.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"Неверный формат ID новости\",\"success\":false}"
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/add-like/{newsId}")
    public ResponseEntity<StatsResponseDto> addLike(@PathVariable Long newsId) {
        try {
            Integer newsLikes = newsObjectService.addLikes(newsId);
            return ResponseEntity.ok(new StatsResponseDto(newsLikes.toString(), true));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    new StatsResponseDto(e.getMessage(), false)
            );
        }
    }
    @GetMapping("/get-likes/{newsId}")
    public ResponseEntity<Integer> getLikes(@PathVariable("newsId") Long newsId) {
        return ResponseEntity.ok(newsObjectService.getLikesCount(newsId));
    }

    @GetMapping("/get-shows/{newsId}")
    public ResponseEntity<Integer> getShows(@PathVariable("newsId") Long newsId) {
        return ResponseEntity.ok(newsObjectService.getShowsCount(newsId));
    }
}
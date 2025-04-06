package scitech.newsservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scitech.newsservice.models.dto.NewsDto;
import scitech.newsservice.services.NewsObjectService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News API", description = "Операции с новостями")
public class NewsObjectController {

    private final NewsObjectService newsService;

    @Operation(summary = "Получить новость по заголовку")
    @GetMapping("/by-title")
    public ResponseEntity<Page<NewsDto>> getByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
        ) {
        return ResponseEntity.ok(newsService.findByTitle(title, PageRequest.of(page, size)));
    }

    @Operation(summary = "Получить новости за указанный период")
    @GetMapping("/by-date-range")
    public ResponseEntity<Page<NewsDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                newsService.findByDateRange(start, end, PageRequest.of(page, size))
        );
    }

    @Operation(summary = "Получить новости по тегам")
    @GetMapping("/by-tags")
    public ResponseEntity<Page<NewsDto>> getByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                newsService.findByTags(tags, PageRequest.of(page, size))
        );
    }

    @Operation(summary = "Получить новости по теме (тематике)")
    @GetMapping("/by-theme")
    public ResponseEntity<Page<NewsDto>> getByTheme(
            @RequestParam String theme,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                newsService.findByTheme(theme, PageRequest.of(page, size))
        );
    }

    @Operation(summary = "Получить новости по автору (владельцу)")
    @GetMapping("/by-author")
    public ResponseEntity<Page<NewsDto>> getByAuthor(
            @RequestParam Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                newsService.findByOwnerId(ownerId, PageRequest.of(page, size))
        );
    }

    @Operation(summary = "Поиск новостей по фильтрам")
    @GetMapping("/search")
    public ResponseEntity<Page<NewsDto>> searchNews(
            @Parameter(description = "Заголовок") @RequestParam(required = false) String title,
            @Parameter(description = "ID владельца") @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Дата начала") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Дата окончания") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Список тегов") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "Тема") @RequestParam(required = false) String theme,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Сортировка (поле, направление)") @RequestParam(defaultValue = "dateOfCreation,desc") String[] sort) {

        Sort sorting = Sort.by(
                sort[0].equals("dateOfCreation") ?
                        Sort.Order.desc("dateOfCreation") :
                        Sort.Order.asc(sort[0])
        );

        PageRequest pageable = PageRequest.of(page, size, sorting);

        return ResponseEntity.ok(
                newsService.findAllWithFilters(title, ownerId, startDate, endDate, tags, theme, pageable)
        );
    }

    @Operation(summary = "Создать новость")
    @PostMapping
    public ResponseEntity<NewsDto> createNews(@RequestBody NewsDto newsDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newsService.createNews(newsDto));
    }

    @Operation(summary = "Получить популярные новости")
    @GetMapping("/popular")
    public ResponseEntity<Page<NewsDto>> getPopularNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                newsService.findPopularNews(PageRequest.of(
                        page,
                        size,
                        Sort.by("likes").descending()
                ))
        );
    }
}

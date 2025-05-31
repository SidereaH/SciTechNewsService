package scitech.newsservice.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scitech.newsservice.models.*;
import scitech.newsservice.models.dto.NewsDto;
import scitech.newsservice.repositories.NewsObjectRepo;
import scitech.newsservice.repositories.StatusRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsObjectService {
    private final StatusRepo statusRepo;
    private final NewsObjectRepo newsObjectRepo;
    private final NewsMapper newsMapper;

    @Transactional(readOnly = true)
    public Integer getShowsCount(Long id) {
        NewsObject obj = newsObjectRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        return obj.getShows();
    }
    @Transactional(readOnly = true)
    public Integer getLikesCount(Long id) {
        NewsObject obj = newsObjectRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        return obj.getLikes();
    }


    @Transactional(readOnly = true)
    public NewsDto findByTitle(String title) {
        NewsObject newsObject = newsObjectRepo.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("News not found with title: " + title));
        return newsMapper.toDto(newsObject);
    }
    public Page<NewsDto> findByTitle(String theme, Pageable pageable) {
        Page<NewsObject> newsPage = newsObjectRepo.findByThemeContainingIgnoreCase(theme, pageable);
        return newsPage.map(newsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NewsDto> findByDateRange(LocalDate start, LocalDate end, Pageable pageable) {
        Page<NewsObject> newsPage = newsObjectRepo.findByDateOfCreationBetween(start, end, pageable);
        return newsPage.map(newsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NewsDto> findByTags(List<String> tags, Pageable pageable) {
        return findAllWithFilters(null, null, null, null, tags, null, pageable);
    }

    @Transactional(readOnly = true)
    public Page<NewsDto> findByTheme(String theme, Pageable pageable) {
        Page<NewsObject> newsPage = newsObjectRepo.findByThemeContainingIgnoreCase(theme, pageable);
        return newsPage.map(newsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NewsDto> findByOwnerId(Long ownerId, Pageable pageable) {
        Page<NewsObject> newsPage = newsObjectRepo.findByOwnerId(ownerId, pageable);
        return newsPage.map(newsMapper::toDto);
    }


    public NewsDto createNews(NewsDto newsDto) {

        NewsObject news = newsMapper.toEntity(newsDto);
        if(newsObjectRepo.existsByTitle(news.getTitle())) {
            return null;
        }


        Status status = statusRepo.findByName(newsDto.getStatus().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + newsDto.getStatus()));
        news.setStatus(status);

        if (news.getDateOfCreation() == null) {
            news.setDateOfCreation(LocalDate.now());
        }

        NewsObject saved;
        try {

            saved = newsObjectRepo.save(news);
        } catch (Exception e) {
            log.error("Error saving news: {}", e.getMessage());
            throw new RuntimeException("Failed to save news: " + newsDto.getTitle(), e);
        }

        return newsMapper.toDto(saved);
    }


    @Transactional(readOnly = true)
    public Page<NewsDto> findAllWithFilters(
            String title,
            Long ownerId,
            LocalDate startDate,
            LocalDate endDate,
            List<String> tags,
            String theme,
            Pageable pageable) {

        Specification<NewsObject> spec = Specification.where(null);

        if (title != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("title"), title));
        }

        if (ownerId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("ownerId"), ownerId));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("dateOfCreation"), startDate, endDate));
        }

        if (tags != null && !tags.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(tags.stream()
                            .map(tag -> cb.like(cb.lower(root.get("tags")), "%" + tag.toLowerCase() + "%"))
                            .toArray(Predicate[]::new))
            );
        }


        if (theme != null) {

            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("theme")), "%" + theme.toLowerCase() + "%"));
        }

        return newsObjectRepo.findAll(spec, pageable)
                .map(newsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NewsDto> findPopularNews(Pageable pageable) {
        return newsObjectRepo.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by("likes").descending()
                                        .and(Sort.by("shows").descending())
                        ))
                .map(newsMapper::toDto);
    }

    //stats

    @Transactional
    public Integer addLikes(Long newsId) {
        NewsObject newsObj = newsObjectRepo.findById(newsId).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));

        newsObj.setLikes(newsObj.getLikes() + 1);
        newsObjectRepo.save(newsObj);
        return newsObj.getLikes();
    }
    @Transactional
    public Integer addShows(Long newsId) {
        NewsObject newsObj = newsObjectRepo.findById(newsId).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));

        newsObj.setShows(newsObj.getShows() + 1);
        newsObjectRepo.save(newsObj);
        return newsObj.getShows();
    }

    @Transactional
    public Integer delLikes(Long newsId) {
        NewsObject newsObj = newsObjectRepo.findById(newsId).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));

        newsObj.setLikes(newsObj.getLikes() - 1);
        newsObjectRepo.save(newsObj);
        return newsObj.getLikes();
    }
    @Transactional
    public Integer delShows(Long newsId) {
        NewsObject newsObj = newsObjectRepo.findById(newsId).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));
        newsObj.setShows(newsObj.getShows() - 1);
        newsObjectRepo.save(newsObj);
        return newsObj.getShows();
    }
    @Transactional
    public List<NewsDto> createNewsFromList(List<NewsDto> newsDtos) {
        List<NewsDto> newsObjects = new ArrayList<>();
        for (NewsDto newsDto : newsDtos) {

            NewsDto dto = createNews(newsDto);
            newsObjects.add(dto);
            log.info("News created: {}", dto);
        }
        return newsObjects;
    }
    @Transactional(readOnly = true)
    public NewsDto findById(Long id) {
        NewsObject newsObject = newsObjectRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
        return newsMapper.toDto(newsObject);
    }
}
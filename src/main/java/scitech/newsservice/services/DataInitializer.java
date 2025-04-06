package scitech.newsservice.services;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import scitech.newsservice.models.NewsObject;
import scitech.newsservice.models.Status;
import scitech.newsservice.repositories.NewsObjectRepo;
import scitech.newsservice.repositories.StatusRepo;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StatusRepo statusRepository;
    private final NewsObjectRepo newsObjectRepository;

    public DataInitializer(StatusRepo statusRepository, NewsObjectRepo newsObjectRepository) {
        this.statusRepository = statusRepository;
        this.newsObjectRepository = newsObjectRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Создаем статусы
        Status draft = new Status();
        draft.setName("Черновик");
        statusRepository.save(draft);

        Status published = new Status();
        published.setName("Опубликовано");
        statusRepository.save(published);

        Status archived = new Status();
        archived.setName("В архиве");
        statusRepository.save(archived);

        // Создаем тестовые новости
        NewsObject news1 = new NewsObject();
        news1.setOwnerId(1L);
        news1.setTheme("Наука");
        news1.setStatus(published);
        news1.setTitle("Новое открытие в квантовой физике");
        news1.setDescription("Ученые сделали прорыв в области квантовых вычислений");
        news1.setContent("<p>Исследователи из MIT объявили о создании нового квантового процессора...</p>");
        news1.setUrl("quantum-breakthrough");
        news1.setLikes(42);
        news1.setShows(150);
        news1.setTags("наука,физика,технологии");
        news1.setDateOfCreation(LocalDate.now().minusDays(3));
        newsObjectRepository.save(news1);

        NewsObject news2 = new NewsObject();
        news2.setOwnerId(2L);
        news2.setTheme("Технологии");
        news2.setStatus(published);
        news2.setTitle("ИИ научился предсказывать погоду с точностью 95%");
        news2.setDescription("Новая модель искусственного интеллекта превзошла традиционные методы");
        news2.setContent("<p>Компания DeepWeather представила свою новую систему прогнозирования...</p>");
        news2.setUrl("ai-weather-prediction");
        news2.setLikes(87);
        news2.setShows(320);
        news2.setTags("технологии,искусственный интеллект,погода");
        news2.setDateOfCreation(LocalDate.now().minusDays(1));
        newsObjectRepository.save(news2);

        NewsObject news3 = new NewsObject();
        news3.setOwnerId(1L);
        news3.setTheme("Политика");
        news3.setStatus(draft);
        news3.setTitle("Новые санкции вступают в силу");
        news3.setDescription("Страны ЕС приняли решение о введении дополнительных ограничений");
        news3.setContent("<p>Совет ЕС единогласно проголосовал за...</p>");
        news3.setUrl("new-sanctions");
        news3.setLikes(0);
        news3.setShows(5);
        news3.setTags("политика,экономика,санкции");
        news3.setDateOfCreation(LocalDate.now());
        newsObjectRepository.save(news3);

        NewsObject news4 = new NewsObject();
        news4.setOwnerId(3L);
        news4.setTheme("Медицина");
        news4.setStatus(archived);
        news4.setTitle("Прорыв в лечении болезни Альцгеймера");
        news4.setDescription("Ученые разработали новый препарат, замедляющий прогрессирование заболевания");
        news4.setContent("<p>Клинические испытания показали...</p>");
        news4.setUrl("alzheimer-treatment");
        news4.setLikes(124);
        news4.setShows(450);
        news4.setTags("медицина,здоровье,наука");
        news4.setDateOfCreation(LocalDate.now().minusMonths(2));
        newsObjectRepository.save(news4);
    }
}
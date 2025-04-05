package scitech.newsservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import scitech.newsservice.models.NewsObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsObjectRepo extends JpaRepository<NewsObject, Long> , JpaSpecificationExecutor<NewsObject> {
        Optional<NewsObject> findByTitle(String title);

        Page<NewsObject> findByDateOfCreationBetween(LocalDate start, LocalDate end, Pageable pageable);



        Page<NewsObject> findByThemeContainingIgnoreCase(String theme, Pageable pageable);

        Page<NewsObject> findByOwnerId(Long ownerId, Pageable pageable);

}

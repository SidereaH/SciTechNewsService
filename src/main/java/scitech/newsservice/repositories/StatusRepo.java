package scitech.newsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scitech.newsservice.models.Status;

import java.util.Optional;

@Repository
public interface StatusRepo extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String statusName);
    boolean existsByName(String statusName);
}

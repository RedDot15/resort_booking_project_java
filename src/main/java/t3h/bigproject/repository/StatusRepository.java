package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.StatusEntity;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
}

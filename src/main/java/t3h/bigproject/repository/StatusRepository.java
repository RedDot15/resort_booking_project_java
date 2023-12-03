package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import t3h.bigproject.entities.StatusEntity;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    @Modifying
    void deleteStatusEntityById(Long id);
}

package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import t3h.bigproject.entities.ResortimageEntity;

public interface ResortImageRepository extends JpaRepository<ResortimageEntity, Long> {
    @Modifying
    void deleteAllByResortId(Long resortId);

    ResortimageEntity findFirstByResortId(Long id);
}

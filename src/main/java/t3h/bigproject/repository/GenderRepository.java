package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import t3h.bigproject.entities.GenderEntity;

public interface GenderRepository extends JpaRepository<GenderEntity, Long> {
    @Modifying
    void deleteGenderEntityById(Long id);
}

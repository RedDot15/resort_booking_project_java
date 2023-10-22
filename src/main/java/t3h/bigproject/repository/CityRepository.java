package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.CityEntity;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    List<CityEntity> findAllByName(String name);

    CityEntity findFirstById(Long id);
}

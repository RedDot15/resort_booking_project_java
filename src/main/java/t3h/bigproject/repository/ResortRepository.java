package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortEntity;

import java.util.List;

public interface ResortRepository extends JpaRepository<ResortEntity, Long> {
    List<ResortEntity> findAllByName(String name);

    List<ResortEntity> findAllByCityId(Long id);

    ResortEntity findFirstById(Long id);

}

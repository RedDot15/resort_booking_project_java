package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortEntity;

import java.util.List;

public interface ResortRepository extends JpaRepository<ResortEntity, Long> {
    List<ResortEntity> findAllByName(String name);

    List<ResortEntity> findAllByCityIdAndNameContaining(Long id, String keyword);

    List<ResortEntity> findAllByCityId(Long id);

    ResortEntity findFirstById(Long id);

    List<ResortEntity> findAllByIdIsNot(Long id);

    @Modifying
    void deleteResortEntityById(Long id);

}

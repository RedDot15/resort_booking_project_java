package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.ExtensionEntity;
import t3h.bigproject.entities.ExtensionEntity;

import java.util.List;

public interface ExtensionRepository extends JpaRepository<ExtensionEntity, Long> {
    List<ExtensionEntity> findAllByName(String name);

    ExtensionEntity findFirstById(Long id);
}

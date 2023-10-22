package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.RoleEntity;
import t3h.bigproject.entities.RoleEntity;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findAllByName(String name);

    RoleEntity findFirstById(Long id);
}

package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.entities.RoomEntity;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findAllByName(String name);

    List<RoomEntity> findAllByResortId(Long id);

    RoomEntity findFirstById(Long id);


}

package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.bigproject.entities.BillEntity;

public interface BillRepository extends JpaRepository<BillEntity, Long> {
    BillEntity getBillEntityById(Long id);
}

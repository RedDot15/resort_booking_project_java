package t3h.bigproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import t3h.bigproject.dto.CountryDto;
import t3h.bigproject.entities.CityEntity;
import t3h.bigproject.entities.CountryEntity;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    List<CountryEntity> findAllByName(String name);

    CountryEntity findFirstById(Long id);

    @Modifying
    void deleteCountryEntityById(Long id);
}

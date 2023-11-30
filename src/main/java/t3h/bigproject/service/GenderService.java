package t3h.bigproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t3h.bigproject.repository.CityRepository;
import t3h.bigproject.repository.GenderRepository;

@Service
public class GenderService {

    @Autowired
    GenderRepository genderRepository;

    public void delete(Long id) {
        genderRepository.deleteGenderEntityById(id);
    }
}

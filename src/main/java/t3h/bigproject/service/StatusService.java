package t3h.bigproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t3h.bigproject.repository.StatusRepository;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public void delete(Long id) {
        statusRepository.deleteStatusEntityById(id);
    }
}

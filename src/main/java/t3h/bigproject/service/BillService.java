package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.VerificationTokenEntity;
import t3h.bigproject.repository.BillRepository;
import t3h.bigproject.repository.VerificationTokenRepository;

@Service
@Transactional
public class BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    public BillEntity add(BillDto billDto){
        BillEntity billEntity = new BillEntity();
        BeanUtils.copyProperties(billDto,billEntity);
        billRepository.save(billEntity);
        billDto.setId(billEntity.getId());
        return billEntity;
    }

    public void createVerificationToken(BillEntity billEntity, String token) {
        VerificationTokenEntity newToken = new VerificationTokenEntity(token, billEntity);
        verificationTokenRepository.save(newToken);
    }

    public VerificationTokenEntity getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findVerificationTokenEntityByToken(verificationToken);
    }
}

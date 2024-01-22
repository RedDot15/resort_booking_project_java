package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.entities.VerificationTokenEntity;
import t3h.bigproject.repository.BillRepository;
import t3h.bigproject.repository.RoomRepository;
import t3h.bigproject.repository.VerificationTokenRepository;

import java.util.List;

@Service
@Transactional
public class BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    public List<BillEntity> getAll(){
        return billRepository.findAll();
    }

    public void updateBill(Long id){
        BillEntity billEntity = billRepository.getBillEntityById(id);
        RoomEntity roomEntity = roomRepository.findFirstById(billEntity.getRoomEntity().getId());
        roomEntity.setStatus("free");
        roomRepository.save(roomEntity);
        billEntity.setStatusId((long) 3);
        billRepository.save(billEntity);
    }

    public BillEntity add(BillDto billDto){
        BillEntity billEntity = new BillEntity();
        BeanUtils.copyProperties(billDto,billEntity);
        billRepository.save(billEntity);
        billDto.setId(billEntity.getId());
        return billEntity;
    }

}

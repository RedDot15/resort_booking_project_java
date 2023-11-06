package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.ResortExtensionDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.entities.ResortExtensionEntity;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.repository.ResortExtensionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResortExtensionService {

    @Autowired
    ResortExtensionRepository resortExtensionRepository;
    public List<ResortExtensionEntity> getAllByResortId(Long id){
        List<ResortExtensionDto> resortExtensionDtoList = new ArrayList<>();
        List<ResortExtensionEntity> resortExtensionEntityList;
        if(StringUtils.isEmpty(id)){
            resortExtensionEntityList = resortExtensionRepository.findAllByResortId(null);
        }
        else{
            resortExtensionEntityList = resortExtensionRepository.findAllByResortId(id);
        }
        for (ResortExtensionEntity resortExtensionEntity : resortExtensionEntityList){
            ResortExtensionDto resortExtensionDto = new ResortExtensionDto();
            BeanUtils.copyProperties(resortExtensionEntity,resortExtensionDto);
            resortExtensionDtoList.add(resortExtensionDto);
        }
        return resortExtensionEntityList;
    }

}

package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortImageDto;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortimageEntity;
import t3h.bigproject.repository.ResortImageRepository;

@Service
@Transactional
public class ResortImageService {
    @Autowired
    ResortImageRepository resortImageRepository;

    public ResortImageDto getFirstByResortId(Long id){
        ResortImageDto resortImageDto =  new ResortImageDto();
        ResortimageEntity resortimageEntity = resortImageRepository.findFirstByResortId(id);
        if(resortimageEntity != null)
            BeanUtils.copyProperties(resortimageEntity,resortImageDto);
        else
            return null;
        return resortImageDto;
    }
}

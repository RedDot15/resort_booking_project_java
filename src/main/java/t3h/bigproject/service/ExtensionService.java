package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.ExtensionDto;
import t3h.bigproject.dto.ExtensionDto;
import t3h.bigproject.entities.ExtensionEntity;
import t3h.bigproject.entities.ExtensionEntity;
import t3h.bigproject.repository.ExtensionRepository;
import t3h.bigproject.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExtensionService {
    @Autowired
    ExtensionRepository extensionRepository;

    @Autowired
    FileUtils fileUtils;

    public List<ExtensionDto> getAll(String name){
        List<ExtensionDto> extensionDtoList = new ArrayList<>();
        List<ExtensionEntity> extensionEntityList;
        if(StringUtils.isEmpty(name)){
            extensionEntityList = extensionRepository.findAll();
        }
        else{
            extensionEntityList = extensionRepository.findAllByName(name);
        }
        for (ExtensionEntity extensionEntity : extensionEntityList){
            ExtensionDto extensionDto = new ExtensionDto();
            BeanUtils.copyProperties(extensionEntity,extensionDto);
            extensionDtoList.add(extensionDto);
        }
        return extensionDtoList;
    }

    public ExtensionDto getDetailById(Long id){
        ExtensionDto extensionDto =  new ExtensionDto();
        ExtensionEntity extensionEntity = extensionRepository.findFirstById(id);
        if(extensionEntity != null)
            BeanUtils.copyProperties(extensionEntity,extensionDto);
        else
            return null;
        return extensionDto;
    }

    public ExtensionDto add(ExtensionDto extensionDto){
        ExtensionEntity extensionEntity = new ExtensionEntity();
        BeanUtils.copyProperties(extensionDto, extensionEntity);
        // Lưu vào bảng product để Lấy thông tin primarykey (ID)
        extensionRepository.save(extensionEntity);
        extensionDto.setId(extensionEntity.getId());

        return extensionDto;
    }
    public ExtensionDto update(ExtensionDto extensionDto){
        ExtensionEntity extensionEntity = extensionRepository.findById(extensionDto.getId()).get();
        BeanUtils.copyProperties(extensionDto, extensionEntity);
        extensionRepository.save(extensionEntity);

        return extensionDto;
    }

    public void delete(Long id) {
        extensionRepository.deleteExtensionEntityById(id);
    }
}

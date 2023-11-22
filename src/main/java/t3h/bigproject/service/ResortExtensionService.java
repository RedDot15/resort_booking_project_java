package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortExtensionDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.entities.CityEntity;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortExtensionEntity;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.params.ExtensionCityParam;
import t3h.bigproject.repository.CityRepository;
import t3h.bigproject.repository.ResortExtensionRepository;
import t3h.bigproject.repository.ResortRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ResortExtensionService {

    @Autowired
    ResortExtensionRepository resortExtensionRepository;

    @Autowired
    ResortRepository resortRepository;

    @Autowired
    CityRepository cityRepository;

    public List<ResortExtensionEntity> getAllByResortId(Long id) {
        List<ResortExtensionDto> resortExtensionDtoList = new ArrayList<>();
        List<ResortExtensionEntity> resortExtensionEntityList;
        if (StringUtils.isEmpty(id)) {
            resortExtensionEntityList = resortExtensionRepository.findAllByResortId(null);
        } else {
            resortExtensionEntityList = resortExtensionRepository.findAllByResortId(id);
        }
        for (ResortExtensionEntity resortExtensionEntity : resortExtensionEntityList) {
            ResortExtensionDto resortExtensionDto = new ResortExtensionDto();
            BeanUtils.copyProperties(resortExtensionEntity, resortExtensionDto);
            resortExtensionDtoList.add(resortExtensionDto);
        }
        return resortExtensionEntityList;
    }

    public List<ResortDto> getAllResortByEntensionIdAndCityId(ExtensionCityParam extensionCity) {
        List<ResortDto> resortDtoList = new ArrayList<>();
        List<ResortEntity> resortEntityList = new ArrayList<>();
        List<ResortEntity> allResortEntityRepository = resortRepository.findAll();
        List<ResortExtensionEntity> allResortExtensionRepository = resortExtensionRepository.findAll();

        for (ResortEntity resortEntity : allResortEntityRepository) {
            for (ResortExtensionEntity resortExtensionEntity : allResortExtensionRepository) {
                if (resortEntity.getId().equals(resortExtensionEntity.getResortId())
                        && extensionCity.cityId.equals(resortEntity.getCityId())
                        && resortEntity.getCityId().equals(extensionCity.cityId)) {
                    resortEntityList.add(resortEntity);
                    break;
                }
            }
        }

        if (extensionCity.starArrId.size() > 0) {
            CollectionUtils.filter(resortEntityList,
                    o -> extensionCity.starArrId.contains(new Long(((ResortEntity) o).getPoint())));
        }

        if (extensionCity.extensionArrId.size() > 0) {
            CollectionUtils.filter(resortEntityList,
                    o -> {
                        for (ResortExtensionEntity rstExt : ((ResortEntity) o).getResortExtensionEntityList()) {
                            if ((extensionCity.extensionArrId).contains(rstExt.getExtensionEntity().getId()))
                                return true;
                        }
                        return false;
                    });
        }

        if (!extensionCity.keyword.equals("")) {
            CollectionUtils.filter(resortEntityList,
                    o -> ((ResortEntity) o).getName().toUpperCase().trim()
                            .contains(extensionCity.keyword.toUpperCase().trim()));
        }

        Collections.sort(resortEntityList, new Comparator<ResortEntity>() {
            @Override
            public int compare(ResortEntity resort1, ResortEntity resort2) {
                return resort2.getPoint() - resort1.getPoint();
            }
        });

        for (ResortEntity resortEntity : resortEntityList) {
            ResortDto resortDto = new ResortDto();
            BeanUtils.copyProperties(resortEntity, resortDto);
            resortDtoList.add(resortDto);
        }

        return resortDtoList;
    }

}

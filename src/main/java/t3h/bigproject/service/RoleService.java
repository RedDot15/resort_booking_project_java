package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.RoleDto;
import t3h.bigproject.dto.RoleDto;
import t3h.bigproject.entities.RoleEntity;
import t3h.bigproject.entities.RoleEntity;
import t3h.bigproject.repository.RoleRepository;
import t3h.bigproject.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FileUtils fileUtils;

    public List<RoleDto> getAll(String name){
        List<RoleDto> roleDtoList = new ArrayList<>();
        List<RoleEntity> roleEntityList;
        if(StringUtils.isEmpty(name)){
            roleEntityList = roleRepository.findAll();
        }
        else{
            roleEntityList = roleRepository.findAllByName(name);
        }
        for (RoleEntity roleEntity : roleEntityList){
            RoleDto roleDto = new RoleDto();
            BeanUtils.copyProperties(roleEntity,roleDto);
            roleDtoList.add(roleDto);
        }
        return roleDtoList;
    }

    public RoleDto getDetailById(Long id){
        RoleDto roleDto =  new RoleDto();
        RoleEntity roleEntity = roleRepository.findFirstById(id);
        if(roleEntity != null)
            BeanUtils.copyProperties(roleEntity,roleDto);
        else
            return null;
        return roleDto;
    }

    public RoleDto add(RoleDto roleDto){
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(roleDto, roleEntity);
        // Lưu vào bảng product để Lấy thông tin primarykey (ID)
        roleRepository.save(roleEntity);
        roleDto.setId(roleEntity.getId());
        return roleDto;
    }
    public RoleDto update(RoleDto roleDto){
        RoleEntity roleEntity = roleRepository.findById(roleDto.getId()).get();
        BeanUtils.copyProperties(roleDto, roleEntity);
        roleRepository.save(roleEntity);
        return roleDto;
    }

    public void delete(Long id) {
        roleRepository.deleteRoleEntityById(id);
    }

}

package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<UserDto> getAll(String email){
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList;
        if(StringUtils.isEmpty(email)){
            userEntityList = userRepository.findAll();
        }
        else{
            userEntityList = userRepository.findAllByEmail(email);
        }
        for (UserEntity roomEntity : userEntityList){
            UserDto roomDto = new UserDto();
            BeanUtils.copyProperties(roomEntity,roomDto);
            userDtoList.add(roomDto);
        }
        return userDtoList;
    }

    public UserDto getDetailByPhone(String phone) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findFirstByPhone(phone);
        if (userEntity != null)
            BeanUtils.copyProperties(userEntity, userDto);
        else
            return null;
        return userDto;
    }

    public UserDto getDetail(Long id) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findById(id).get();
        if (userEntity != null)
            BeanUtils.copyProperties(userEntity, userDto);
        else
            return null;
        return userDto;
    }

    public UserDto addUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if (userEntity.getRoleId() == null || userEntity.getRoleId().equals("")){
            userEntity.setRoleId((long) 2);
        }
        userRepository.save(userEntity);
        userDto.setId(userEntity.getId());
        return userDto;
    }

    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.getId()).get();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return userDto;
    }

    public UserDto updateUserWithoutEncode(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.getId()).get();
        BeanUtils.copyProperties(userDto, userEntity);
        userRepository.save(userEntity);
        return userDto;
    }

    public void delete(Long id) {
        userRepository.deleteUserEntityById(id);
    }
}

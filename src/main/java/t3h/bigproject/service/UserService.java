package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.entities.VerificationTokenEntity;
import t3h.bigproject.repository.UserRepository;
import t3h.bigproject.repository.VerificationTokenRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

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

    public UserDto getDetailByEmail(String email) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findFirstByEmail(email);
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

    public UserEntity addUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if (userEntity.getRoleId() == null || userEntity.getRoleId().equals("")){
            userEntity.setRoleId((long) 2);
        }
        if (userEntity.getStatus() == null){
            userEntity.setStatus((long) 0);
        }
        userRepository.save(userEntity);
        userDto.setId(userEntity.getId());
        return userEntity;
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

    public void createVerificationToken(UserEntity userEntity, String token) {
        VerificationTokenEntity newToken = new VerificationTokenEntity(token, userEntity);
        verificationTokenRepository.save(newToken);
    }

    public VerificationTokenEntity getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findVerificationTokenEntityByToken(verificationToken);
    }

    public List<VerificationTokenEntity> getAllVerificationToken(){
        return verificationTokenRepository.findAll();
    }
}

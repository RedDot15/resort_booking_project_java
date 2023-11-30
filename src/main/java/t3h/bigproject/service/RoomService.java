package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.repository.RoomRepository;
import t3h.bigproject.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    FileUtils fileUtils;

//    @Autowired
//    ProductImagesRepository productImagesRepository;

    public List<RoomDto> getAll(String name){
        List<RoomDto> roomDtoList = new ArrayList<>();
        List<RoomEntity> roomEntityList;
        if(StringUtils.isEmpty(name)){
            roomEntityList = roomRepository.findAll();
        }
        else{
            roomEntityList = roomRepository.findAllByName(name);
        }
        for (RoomEntity roomEntity : roomEntityList){
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(roomEntity,roomDto);
            roomDtoList.add(roomDto);
        }
        return roomDtoList;
    }

    public List<RoomDto> getAllByResortId(Long id){
        List<RoomDto> roomDtoList = new ArrayList<>();
        List<RoomEntity> roomEntityList;
        if(StringUtils.isEmpty(id)){
            roomEntityList = roomRepository.findAllByResortId(null);
        }
        else{
            roomEntityList = roomRepository.findAllByResortId(id);
        }
        for (RoomEntity roomEntity : roomEntityList){
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(roomEntity,roomDto);
            roomDtoList.add(roomDto);
        }
        return roomDtoList;
    }

    public RoomDto getDetailById(Long id){
        RoomDto roomDto =  new RoomDto();
        RoomEntity roomEntity = roomRepository.findFirstById(id);
        if(roomEntity != null)
            BeanUtils.copyProperties(roomEntity,roomDto);
        else
            return null;
        return roomDto;
    }

    public RoomDto add(RoomDto roomDto){
        RoomEntity roomEntity = new RoomEntity();
        BeanUtils.copyProperties(roomDto, roomEntity);
        // Lưu vào bảng product để Lấy thông tin primarykey (ID)
        roomRepository.save(roomEntity);
        roomDto.setId(roomEntity.getId());

        //LƯU ẢNH
        try {
            saveFile(roomDto);
        } catch (IOException e) {
            throw new RuntimeException("Tạo mới lỗi");
        }

        //LƯU TÊN ẢNH
        // lưu ảnh main
        roomEntity.setImageName(roomDto.getImageName());
        return roomDto;
    }
    public RoomDto update(RoomDto roomDto){
        RoomEntity roomEntity = roomRepository.findById(roomDto.getId()).get();
        BeanUtils.copyProperties(roomDto, roomEntity);
        roomRepository.save(roomEntity);

        fileUtils.cleanDir("room\\" + roomDto.getId());// xóa ảnh trong thư mục
        try {
            saveFile(roomDto);
        } catch (IOException e) {
            throw new RuntimeException("Tạo mới lỗi");
        }
        // lưu ảnh main
        roomEntity.setImageName(roomDto.getImageName());
        return roomDto;
    }

    void saveFile(RoomDto roomDto) throws IOException {
        if (roomDto.getFileImage() != null && !roomDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
            roomDto.setImageName(
                    fileUtils.saveFile(roomDto.getFileImage(), "room\\" + roomDto.getId() + "\\"));
        }
    }

    public void delete(Long id) {
        roomRepository.deleteRoomEntityById(id);
    }
}

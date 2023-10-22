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
        // Lưu mhiều ảnh
//        if (!CollectionUtils.isEmpty(roomDto.getProductImagesDtos())) {
////            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: roomDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
////                productImagesEntities.add(productImagesEntity);
//                productImagesRepository.save(productImagesEntity);
//            }
////            productImagesRepository.saveAll(productImagesEntities);
//        }
        return roomDto;
    }
    public RoomDto update(RoomDto roomDto){
        RoomEntity roomEntity = roomRepository.findById(roomDto.getId()).get();
        BeanUtils.copyProperties(roomDto, roomEntity);
        roomRepository.save(roomEntity);

        fileUtils.cleanDir("room\\" + roomDto.getId());// xóa ảnh trong thư mục
//        productImagesRepository.deleteAllByProductId(roomDto.getId());// xóa nhiều ảnh trong database
        try {
            saveFile(roomDto);
        } catch (IOException e) {
            throw new RuntimeException("Tạo mới lỗi");
        }
        // lưu ảnh main
        roomEntity.setImageName(roomDto.getImageName());
        // Lưu nhiều ảnh
//        if (!CollectionUtils.isEmpty(roomDto.getProductImagesDtos())) {
//            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: roomDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
//                productImagesEntities.add(productImagesEntity);
//            }
//            productImagesRepository.saveAll(productImagesEntities);
//        }
        return roomDto;
    }

    void saveFile(RoomDto roomDto) throws IOException {
        if (roomDto.getFileImage() != null && !roomDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
            roomDto.setImageName(
                    fileUtils.saveFile(roomDto.getFileImage(), "room\\" + roomDto.getId() + "\\"));
        }

//        if (!CollectionUtils.isEmpty(roomDto.getMultipartFileList())) {//Lưu các file ảnh phụ
//            List<ProductImagesDto> productImagesDtos = new ArrayList<>();
//            for (MultipartFile multipartFile: roomDto.getMultipartFileList()
//            ) {
//                if (roomDto.getFileImage() != null && !roomDto.getFileImage().isEmpty()) {
//                    ProductImagesDto productImagesDto = new ProductImagesDto();
//                    productImagesDto.setName(
//                            fileUtils.saveFile(roomDto.getFileImage(), "products\\" + roomDto.getId() + "\\detail\\"));
//                    productImagesDto.setProductId(roomDto.getId());
//                    productImagesDtos.add(productImagesDto);
//                }
//            }
//            roomDto.setProductImagesDtos(productImagesDtos);
//        }
    }
}

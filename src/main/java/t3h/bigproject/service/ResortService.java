package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortImageDto;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortExtensionEntity;
import t3h.bigproject.entities.ResortimageEntity;
import t3h.bigproject.repository.ResortExtensionRepository;
import t3h.bigproject.repository.ResortImageRepository;
import t3h.bigproject.repository.ResortRepository;
import t3h.bigproject.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ResortService {
    @Autowired
    ResortRepository resortRepository;

    @Autowired
    ResortExtensionRepository resortExtensionRepository;

    @Autowired
    ResortImageRepository resortImageRepository;

    @Autowired
    FileUtils fileUtils;

//    @Autowired
//    ProductImagesRepository productImagesRepository;

    public List<ResortDto> getAll(String name){
        List<ResortDto> resortDtoList = new ArrayList<>();
        List<ResortEntity> resortEntityList;
        if(StringUtils.isEmpty(name)){
            resortEntityList = resortRepository.findAll();
        }
        else{
            resortEntityList = resortRepository.findAllByName(name);
        }
        for (ResortEntity resortEntity : resortEntityList){
            ResortDto resortDto = new ResortDto();
            BeanUtils.copyProperties(resortEntity,resortDto);
            resortDtoList.add(resortDto);
        }
        return resortDtoList;
    }

    public List<ResortDto> getAllByCityId(Long id){
        List<ResortDto> resortDtoList = new ArrayList<>();
        List<ResortEntity> resortEntityList;
//        if(StringUtils.isEmpty(id)){
//            resortEntityList = resortRepository.findAllByCityId();
//        }
//        else{
        resortEntityList = resortRepository.findAllByCityId(id);
        System.out.println();
//        }
        for (ResortEntity resortEntity : resortEntityList){
            ResortDto resortDto = new ResortDto();
            BeanUtils.copyProperties(resortEntity,resortDto);
            resortDtoList.add(resortDto);
        }
        return resortDtoList;
    }

    public ResortDto getDetailById(Long id){
        ResortDto resortDto =  new ResortDto();
        ResortEntity resortEntity = resortRepository.findFirstById(id);
        if(resortEntity != null)
            BeanUtils.copyProperties(resortEntity,resortDto);
        else
            return null;
        return resortDto;
    }

    public ResortDto add(ResortDto resortDto) throws IOException {
        ResortEntity resortEntity = new ResortEntity();
        BeanUtils.copyProperties(resortDto, resortEntity);
        // Lưu vào bảng product để Lấy thông tin primarykey (ID)
        resortRepository.save(resortEntity);
        resortDto.setId(resortEntity.getId());

        //LƯU EXTENSION
        for (Long x : resortDto.getExtensionListId()){
            ResortExtensionEntity resortExtensionEntity = new ResortExtensionEntity();
            resortExtensionEntity.setResortId(resortDto.getId());
            resortExtensionEntity.setExtensionEntityId(x);
            resortExtensionRepository.save(resortExtensionEntity);
        }


        //LƯU ẢNH
//        try {
//            saveFile(resortDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }

        //LƯU TÊN ẢNH
//         Lưu mhiều ảnh
        if (!CollectionUtils.isEmpty(resortDto.getMultipartFileList())) {
            for (MultipartFile multipartFile: resortDto.getMultipartFileList()
            ) {
                ResortimageEntity resortimageEntity = new ResortimageEntity();
                resortimageEntity.setName(
                        fileUtils.saveFile(multipartFile, "resort\\" + resortDto.getId() + "\\detail\\"));
                resortimageEntity.setResortId(resortDto.getId());
                resortImageRepository.save(resortimageEntity);
            }
        }
        return resortDto;
    }

    public ResortDto update(ResortDto resortDto) throws IOException {
        ResortEntity resortEntity = resortRepository.findById(resortDto.getId()).get();
        resortDto.setResortimageEntityList(resortEntity.getResortimageEntityList());
        BeanUtils.copyProperties(resortDto, resortEntity);
        resortRepository.save(resortEntity);

        resortExtensionRepository.deleteAllByResortId(resortDto.getId());
        //LƯU EXTENSION
        for (Long x : resortDto.getExtensionListId()){
            ResortExtensionEntity resortExtensionEntity = new ResortExtensionEntity();
            resortExtensionEntity.setResortId(resortDto.getId());
            resortExtensionEntity.setExtensionEntityId(x);
            resortExtensionRepository.save(resortExtensionEntity);
        }

//        try {
//            saveFile(resortDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }

        fileUtils.cleanDir("resort\\" + resortDto.getId() + "\\detail\\");// xóa ảnh trong thư mục
        resortImageRepository.deleteAllByResortId(resortDto.getId());// xóa nhiều ảnh trong database

//         Lưu nhiều ảnh
        if (!CollectionUtils.isEmpty(resortDto.getMultipartFileList())) {
            for (MultipartFile multipartFile: resortDto.getMultipartFileList()
            ) {
                ResortimageEntity resortimageEntity = new ResortimageEntity();
                resortimageEntity.setName(
                        fileUtils.saveFile(multipartFile, "resort\\" + resortDto.getId() + "\\detail\\"));
                resortimageEntity.setResortId(resortDto.getId());
                resortImageRepository.save(resortimageEntity);
            }
        }
        return resortDto;
    }

//    void saveFile(ResortDto resortDto) throws IOException {
////        if (resortDto.getFileImage() != null && !resortDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
////            resortDto.setImageName(
////                    fileUtils.saveFile(resortDto.getFileImage(), "city\\" + resortDto.getId() + "\\"));
////        }
//
//        if (!CollectionUtils.isEmpty(resortDto.getMultipartFileList())) {//Lưu các file ảnh phụ
//            List<ResortImageDto> resortImageDtos = new ArrayList<>();
//            for (MultipartFile multipartFile: resortDto.getMultipartFileList()
//            ) {
//                if (multipartFile != null && !multipartFile.isEmpty()) {
//                    ResortImageDto resortImageDto = new ResortImageDto();
//                    resortImageDto.setName(
//                            fileUtils.saveFile(multipartFile, "products\\" + resortDto.getId() + "\\detail\\"));
//                    resortImageDto.setResortId(resortDto.getId());
//                    resortImageDtos.add(resortImageDto);
//                }
//            }
//            resortDto.setResortImageDtos(resortImageDtos);
//        }
//    }
}

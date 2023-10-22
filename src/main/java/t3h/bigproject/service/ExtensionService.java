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

//    @Autowired
//    ProductImagesRepository productImagesRepository;

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

        //LƯU ẢNH
//        try {
//            saveFile(extensionDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }

        //LƯU TÊN ẢNH
        // lưu ảnh main
//        extensionEntity.setImageName(extensionDto.getImageName());
        // Lưu mhiều ảnh
//        if (!CollectionUtils.isEmpty(extensionDto.getProductImagesDtos())) {
////            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: extensionDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
////                productImagesEntities.add(productImagesEntity);
//                productImagesRepository.save(productImagesEntity);
//            }
////            productImagesRepository.saveAll(productImagesEntities);
//        }
        return extensionDto;
    }
    public ExtensionDto update(ExtensionDto extensionDto){
        ExtensionEntity extensionEntity = extensionRepository.findById(extensionDto.getId()).get();
        BeanUtils.copyProperties(extensionDto, extensionEntity);
        extensionRepository.save(extensionEntity);

//        fileUtils.cleanDir("products\\" + extensionDto.getId());// xóa ảnh trong thư mục
////        productImagesRepository.deleteAllByProductId(extensionDto.getId());// xóa nhiều ảnh trong database
//        try {
//            saveFile(extensionDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }
        // lưu ảnh main
//        extensionEntity.setImageName(extensionDto.getImageName());
        // Lưu nhiều ảnh
//        if (!CollectionUtils.isEmpty(extensionDto.getProductImagesDtos())) {
//            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: extensionDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
//                productImagesEntities.add(productImagesEntity);
//            }
//            productImagesRepository.saveAll(productImagesEntities);
//        }
        return extensionDto;
    }

//    void saveFile(ExtensionDto extensionDto) throws IOException {
//        if (extensionDto.getFileImage() != null && !extensionDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
//            extensionDto.setImageName(
//                    fileUtils.saveFile(extensionDto.getFileImage(), "city\\" + extensionDto.getId() + "\\"));
//        }
//
////        if (!CollectionUtils.isEmpty(extensionDto.getMultipartFileList())) {//Lưu các file ảnh phụ
////            List<ProductImagesDto> productImagesDtos = new ArrayList<>();
////            for (MultipartFile multipartFile: extensionDto.getMultipartFileList()
////            ) {
////                if (extensionDto.getFileImage() != null && !extensionDto.getFileImage().isEmpty()) {
////                    ProductImagesDto productImagesDto = new ProductImagesDto();
////                    productImagesDto.setName(
////                            fileUtils.saveFile(extensionDto.getFileImage(), "products\\" + extensionDto.getId() + "\\detail\\"));
////                    productImagesDto.setProductId(extensionDto.getId());
////                    productImagesDtos.add(productImagesDto);
////                }
////            }
////            extensionDto.setProductImagesDtos(productImagesDtos);
////        }
//    }
}

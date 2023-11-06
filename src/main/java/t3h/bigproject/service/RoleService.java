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
//    @Autowired
//    ProductImagesRepository productImagesRepository;

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

        //LƯU ẢNH
//        try {
//            saveFile(roleDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }

        //LƯU TÊN ẢNH
        // lưu ảnh main
//        roleEntity.setImageName(roleDto.getImageName());
        // Lưu mhiều ảnh
//        if (!CollectionUtils.isEmpty(roleDto.getProductImagesDtos())) {
////            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: roleDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
////                productImagesEntities.add(productImagesEntity);
//                productImagesRepository.save(productImagesEntity);
//            }
////            productImagesRepository.saveAll(productImagesEntities);
//        }
        return roleDto;
    }
    public RoleDto update(RoleDto roleDto){
        RoleEntity roleEntity = roleRepository.findById(roleDto.getId()).get();
        BeanUtils.copyProperties(roleDto, roleEntity);
        roleRepository.save(roleEntity);

//        fileUtils.cleanDir("products\\" + roleDto.getId());// xóa ảnh trong thư mục
////        productImagesRepository.deleteAllByProductId(roleDto.getId());// xóa nhiều ảnh trong database
//        try {
//            saveFile(roleDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }
        // lưu ảnh main
//        roleEntity.setImageName(roleDto.getImageName());
        // Lưu nhiều ảnh
//        if (!CollectionUtils.isEmpty(roleDto.getProductImagesDtos())) {
//            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: roleDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
//                productImagesEntities.add(productImagesEntity);
//            }
//            productImagesRepository.saveAll(productImagesEntities);
//        }
        return roleDto;
    }

//    void saveFile(RoleDto roleDto) throws IOException {
//        if (roleDto.getFileImage() != null && !roleDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
//            roleDto.setImageName(
//                    fileUtils.saveFile(roleDto.getFileImage(), "city\\" + roleDto.getId() + "\\"));
//        }
//
////        if (!CollectionUtils.isEmpty(roleDto.getMultipartFileList())) {//Lưu các file ảnh phụ
////            List<ProductImagesDto> productImagesDtos = new ArrayList<>();
////            for (MultipartFile multipartFile: roleDto.getMultipartFileList()
////            ) {
////                if (roleDto.getFileImage() != null && !roleDto.getFileImage().isEmpty()) {
////                    ProductImagesDto productImagesDto = new ProductImagesDto();
////                    productImagesDto.setName(
////                            fileUtils.saveFile(roleDto.getFileImage(), "products\\" + roleDto.getId() + "\\detail\\"));
////                    productImagesDto.setProductId(roleDto.getId());
////                    productImagesDtos.add(productImagesDto);
////                }
////            }
////            roleDto.setProductImagesDtos(productImagesDtos);
////        }
//    }
}

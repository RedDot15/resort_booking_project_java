package t3h.bigproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import t3h.bigproject.dto.CountryDto;
import t3h.bigproject.entities.CountryEntity;
import t3h.bigproject.repository.CountryRepository;
import t3h.bigproject.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CountryService {
    @Autowired
    CountryRepository countryRepository;

    @Autowired
    FileUtils fileUtils;

//    @Autowired
//    ProductImagesRepository productImagesRepository;

    public List<CountryDto> getAll(String name){
        List<CountryDto> countryDtoList = new ArrayList<>();
        List<CountryEntity> countryEntityList;
        if(StringUtils.isEmpty(name)){
            countryEntityList = countryRepository.findAll();
        }
        else{
            countryEntityList = countryRepository.findAllByName(name);
        }
        for (CountryEntity countryEntity : countryEntityList){
            CountryDto countryDto = new CountryDto();
            BeanUtils.copyProperties(countryEntity,countryDto);
            countryDtoList.add(countryDto);
        }
        return countryDtoList;
    }

    public CountryDto getDetailById(Long id){
        CountryDto countryDto =  new CountryDto();
        CountryEntity countryEntity = countryRepository.findFirstById(id);
        if(countryEntity != null)
            BeanUtils.copyProperties(countryEntity,countryDto);
        else
            return null;
        return countryDto;
    }

    public CountryDto add(CountryDto countryDto){
        CountryEntity countryEntity = new CountryEntity();
        BeanUtils.copyProperties(countryDto, countryEntity);
        // Lưu vào bảng product để Lấy thông tin primarykey (ID)
        countryRepository.save(countryEntity);
        countryDto.setId(countryEntity.getId());

        //LƯU ẢNH
//        try {
//            saveFile(countryDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }

        //LƯU TÊN ẢNH
        // lưu ảnh main
//        countryEntity.setImageName(countryDto.getImageName());
        // Lưu mhiều ảnh
//        if (!CollectionUtils.isEmpty(countryDto.getProductImagesDtos())) {
////            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: countryDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
////                productImagesEntities.add(productImagesEntity);
//                productImagesRepository.save(productImagesEntity);
//            }
////            productImagesRepository.saveAll(productImagesEntities);
//        }
        return countryDto;
    }
    public CountryDto update(CountryDto countryDto){
        CountryEntity countryEntity = countryRepository.findById(countryDto.getId()).get();
        BeanUtils.copyProperties(countryDto, countryEntity);
        countryRepository.save(countryEntity);

//        fileUtils.cleanDir("products\\" + countryDto.getId());// xóa ảnh trong thư mục
////        productImagesRepository.deleteAllByProductId(countryDto.getId());// xóa nhiều ảnh trong database
//        try {
//            saveFile(countryDto);
//        } catch (IOException e) {
//            throw new RuntimeException("Tạo mới lỗi");
//        }
        // lưu ảnh main
//        countryEntity.setImageName(countryDto.getImageName());
        // Lưu nhiều ảnh
//        if (!CollectionUtils.isEmpty(countryDto.getProductImagesDtos())) {
//            List<ProductImagesEntity> productImagesEntities = new ArrayList<>();
//            for (ProductImagesDto productImagesDto: countryDto.getProductImagesDtos()
//            ) {
//                ProductImagesEntity productImagesEntity = new ProductImagesEntity();
//                BeanUtils.copyProperties(productImagesDto, productImagesEntity);
//                productImagesEntities.add(productImagesEntity);
//            }
//            productImagesRepository.saveAll(productImagesEntities);
//        }
        return countryDto;
    }

//    void saveFile(CountryDto countryDto) throws IOException {
//        if (countryDto.getFileImage() != null && !countryDto.getFileImage().isEmpty()) {//Lưu 1 file ảnh chính
//            countryDto.setImageName(
//                    fileUtils.saveFile(countryDto.getFileImage(), "city\\" + countryDto.getId() + "\\"));
//        }
//
////        if (!CollectionUtils.isEmpty(countryDto.getMultipartFileList())) {//Lưu các file ảnh phụ
////            List<ProductImagesDto> productImagesDtos = new ArrayList<>();
////            for (MultipartFile multipartFile: countryDto.getMultipartFileList()
////            ) {
////                if (countryDto.getFileImage() != null && !countryDto.getFileImage().isEmpty()) {
////                    ProductImagesDto productImagesDto = new ProductImagesDto();
////                    productImagesDto.setName(
////                            fileUtils.saveFile(countryDto.getFileImage(), "products\\" + countryDto.getId() + "\\detail\\"));
////                    productImagesDto.setProductId(countryDto.getId());
////                    productImagesDtos.add(productImagesDto);
////                }
////            }
////            countryDto.setProductImagesDtos(productImagesDtos);
////        }
//    }
}

package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import t3h.bigproject.entities.ResortEntity;
import t3h.bigproject.entities.ResortimageEntity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Long id;
    private Long countryId;
    private String name;
    private String imageName;

    MultipartFile fileImage;

    List<ResortEntity> resortEntityList;
}



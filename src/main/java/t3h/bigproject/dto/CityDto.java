package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

}

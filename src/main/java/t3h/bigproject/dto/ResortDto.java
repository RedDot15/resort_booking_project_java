package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import t3h.bigproject.entities.ResortExtensionEntity;
import t3h.bigproject.entities.ResortimageEntity;
import t3h.bigproject.entities.RoomEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResortDto {
    private Long id;
    private Long cityId;
    private String name;
    private String infoHTML;
    private String infoMarkDown;
    private Integer point;
    private Float lat;
    private Float lng;


    private ArrayList<Long> extensionListId;

    private List<MultipartFile> multipartFileList;

    List<ResortimageEntity> resortimageEntityList;

    List<RoomEntity> roomEntityList;

    List<ResortExtensionEntity> resortExtensionEntityList;
}

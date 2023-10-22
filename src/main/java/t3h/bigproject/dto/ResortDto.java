package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private Long point;

    private ArrayList<Long> extensionListId;

    private List<MultipartFile> multipartFileList;
}

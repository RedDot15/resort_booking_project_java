package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import t3h.bigproject.entities.ExtensionEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResortExtensionDto {
    private Long id;
    private Long resortId;
//    private Long extensionId;

    ExtensionEntity extensionEntity;
}

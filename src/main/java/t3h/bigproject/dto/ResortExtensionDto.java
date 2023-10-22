package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResortExtensionDto {
    private Long id;
    private Long resortId;
    private Long extensionId;
}

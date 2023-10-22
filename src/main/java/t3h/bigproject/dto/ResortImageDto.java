package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResortImageDto {
    private Long id;
    private String name;
    private Long resortId;
}

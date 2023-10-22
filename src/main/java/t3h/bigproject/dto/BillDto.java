package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long id;
    private Long genderId;
    private String name;
    private String phone;
    private String email;
    private Long statusId;
}

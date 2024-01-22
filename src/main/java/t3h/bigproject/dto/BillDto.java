package t3h.bigproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import t3h.bigproject.entities.RoomEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long id;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String gender;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String name;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String phone;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String email;
    private String info;
    private Long statusId = (long)1;

    private RoomEntity roomEntity;
}

package t3h.bigproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "bill", schema = "bigproject")
public class BillEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "info")
    private String info;
    @Basic
    @Column(name = "status_id")
    private Long statusId;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity roomEntity;
}

package t3h.bigproject.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "city", schema = "bigproject")
public class CityEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "country_id")
    private Long countryId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "image_name")
    private String imageName;
}

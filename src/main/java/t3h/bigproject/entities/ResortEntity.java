package t3h.bigproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "resort", schema = "bigproject")
public class ResortEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "city_id")
    private Long cityId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "info_html")
    private String infoHtml;
    @Basic
    @Column(name = "info_markdown")
    private String infoMarkdown;
    @Basic
    @Column(name = "point")
    private Integer point;


}

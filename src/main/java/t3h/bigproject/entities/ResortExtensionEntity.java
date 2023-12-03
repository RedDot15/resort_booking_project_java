package t3h.bigproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "resort_extension", schema = "bigproject" )
public class ResortExtensionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "resort_id")
    private Long resortId;
//    @Basic
//    @Column(name = "extension_id")
//    private Long extensionId;

    @ManyToOne
    @JoinColumn(name = "extension_id")
    ExtensionEntity extensionEntity;

    public void setExtensionEntityId(Long id){
        extensionEntity = new ExtensionEntity();
        extensionEntity.setId(id);
    }
}

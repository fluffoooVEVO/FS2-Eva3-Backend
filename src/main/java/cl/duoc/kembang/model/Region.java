package cl.duoc.kembang.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la region es obligatorio")
    @Size(min = 3, max = 50)
    @Column(name = "region", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "region")
    private List<Comuna> comuna;
}

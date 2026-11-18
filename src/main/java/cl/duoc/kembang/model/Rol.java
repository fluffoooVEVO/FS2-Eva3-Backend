package cl.duoc.kembang.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name="Rol")

public class Rol {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_rol;

    @NotBlank(message = "El nombre del rol no puede estar vacio")
    @Column(nullable = false,length = 50)
    private String nombre_rol;
}

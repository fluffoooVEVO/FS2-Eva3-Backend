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
@Entity
@Table(name="Usuario")
@NoArgsConstructor
@AllArgsConstructor


public class Usuario {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id_usuario;

    @NotBlank(message = "Ingrese nombre usuario")
    @Column(nullable = false,length = 70)
    private String nombre_usuario;

    @NotBlank(message = "Porfavor ingresar correo")
    @Column(nullable =false,length = 150)
    private String correo;

    
}

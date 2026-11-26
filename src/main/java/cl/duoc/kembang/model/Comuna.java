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
@Table(name = "comunas")
public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la comuna es obligatorio")
    @Size(min = 4, max = 50, message = "El nombre de la comuna debe tener al menos diez caracteres y como maximo 50")
    @Column(name = "comuna", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "comuna")
    private List<Direccion> direccion;
}

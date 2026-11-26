package cl.duoc.kembang.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Porfavor ingresar correo")
    @Email(message = "El correo no tiene un formato valido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
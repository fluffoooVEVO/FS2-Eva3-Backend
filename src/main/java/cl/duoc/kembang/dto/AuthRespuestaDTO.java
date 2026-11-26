package cl.duoc.kembang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRespuestaDTO {

    private String token;
    private String tipo;
    private Long expiraEn;
    private String nombre;
    private String email;
    private String rol;
}
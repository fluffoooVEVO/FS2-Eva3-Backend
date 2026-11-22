package cl.duoc.kembang.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoDTO {

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    private List<@Valid ItemPedidoDTO> items;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 30, message = "El metodo de pago no puede superar 30 caracteres")
    private String metodoPago;
}
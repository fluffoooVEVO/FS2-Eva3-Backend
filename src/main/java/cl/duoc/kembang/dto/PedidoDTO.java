package cl.duoc.kembang.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private Integer total;
    private String metodoPago;
    private List<DetallePedidoDTO> detalles;
}
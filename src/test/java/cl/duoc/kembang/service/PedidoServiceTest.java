package cl.duoc.kembang.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.kembang.dto.CrearPedidoDTO;
import cl.duoc.kembang.dto.ItemPedidoDTO;
import cl.duoc.kembang.exception.StockInsuficienteException;
import cl.duoc.kembang.model.Estado;
import cl.duoc.kembang.model.Producto;
import cl.duoc.kembang.model.Usuario;
import cl.duoc.kembang.repository.ComunaRepository;
import cl.duoc.kembang.repository.DetallePedidoRepository;
import cl.duoc.kembang.repository.DireccionRepository;
import cl.duoc.kembang.repository.EstadoRepository;
import cl.duoc.kembang.repository.PagoRepository;
import cl.duoc.kembang.repository.PedidoRepository;
import cl.duoc.kembang.repository.ProductoRepository;
import cl.duoc.kembang.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private DireccionRepository direccionRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("No crea el pedido si no hay stock suficiente")
    void crearSinStock() {
        Usuario usuario = new Usuario();
        usuario.setEmail("cliente@kembang.cl");
        Estado pendiente = new Estado();
        pendiente.setNombre("PENDIENTE");
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Ficus Benjamina");
        producto.setStock(2);

        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProductoId(1L);
        item.setCantidad(5);
        CrearPedidoDTO dto = new CrearPedidoDTO();
        dto.setItems(List.of(item));
        dto.setMetodoPago("TARJETA");

        when(usuarioRepository.findByEmail("cliente@kembang.cl")).thenReturn(Optional.of(usuario));
        when(estadoRepository.findByNombre("PENDIENTE")).thenReturn(Optional.of(pendiente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class, () -> pedidoService.crear(dto, "cliente@kembang.cl"));
        verify(pedidoRepository, never()).save(any());
        verify(pagoRepository, never()).save(any());
    }
}
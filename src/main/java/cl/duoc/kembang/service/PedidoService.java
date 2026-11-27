package cl.duoc.kembang.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.kembang.dto.CrearPedidoDTO;
import cl.duoc.kembang.dto.DetallePedidoDTO;
import cl.duoc.kembang.dto.ItemPedidoDTO;
import cl.duoc.kembang.dto.PedidoDTO;
import cl.duoc.kembang.exception.ResourceNotFoundException;
import cl.duoc.kembang.exception.StockInsuficienteException;
import cl.duoc.kembang.model.Comuna;
import cl.duoc.kembang.model.DetallePedido;
import cl.duoc.kembang.model.Direccion;
import cl.duoc.kembang.model.Estado;
import cl.duoc.kembang.model.Pago;
import cl.duoc.kembang.model.Pedido;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComunaRepository comunaRepository;
    private final DireccionRepository direccionRepository;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository,
        PagoRepository pagoRepository, ProductoRepository productoRepository,
        EstadoRepository estadoRepository, UsuarioRepository usuarioRepository,
        ComunaRepository comunaRepository, DireccionRepository direccionRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
        this.estadoRepository = estadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.comunaRepository = comunaRepository;
        this.direccionRepository = direccionRepository;
    }

    @Transactional
    public PedidoDTO crear(CrearPedidoDTO dto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario " + email));

        Estado pendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estado PENDIENTE"));

        List<DetallePedido> detalles = new ArrayList<>();
        int total = 0;

        for (ItemPedidoDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el producto con id " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre()
                        + ": disponible " + producto.getStock() + ", solicitado " + item.getCantidad());
            }
            producto.setStock(producto.getStock() - item.getCantidad());

            int precio = precioConDescuento(producto);
            total += precio * item.getCantidad();
            detalles.add(new DetallePedido(null, null, producto, item.getCantidad(), precio));
        }

        Comuna comuna = comunaRepository.findById(dto.getDireccion().getComunaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la comuna con id " + dto.getDireccion().getComunaId()));
        Direccion direccion = direccionRepository.save(
                new Direccion(null, dto.getDireccion().getCalle(), dto.getDireccion().getNumero(), comuna));

        Pedido pedido = pedidoRepository.save(
                new Pedido(null, LocalDateTime.now(), total, pendiente, usuario, direccion));

        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedido);
        }
        detallePedidoRepository.saveAll(detalles);

        pagoRepository.save(new Pago(null, pedido, dto.getMetodoPago(), total, LocalDateTime.now()));

        log.info("Pedido {} creado por {} con total {}", pedido.getId(), email, total);
        return convertirADTO(pedido, detalles, dto.getMetodoPago());
    }

    public List<PedidoDTO> listar() {
        return pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha")).stream()
                .map(this::armarDTO)
                .toList();
    }

    public PedidoDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el pedido con id " + id));
        return armarDTO(pedido);
    }

    private PedidoDTO armarDTO(Pedido pedido) {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
        String metodoPago = pagoRepository.findByPedidoId(pedido.getId())
                .map(Pago::getMetodo)
                .orElse(null);
        return convertirADTO(pedido, detalles, metodoPago);
    }

    private int precioConDescuento(Producto producto) {
        return (int) Math.round(producto.getPrecio() * (1 - producto.getDescuento() / 100.0));
    }

    private PedidoDTO convertirADTO(Pedido pedido, List<DetallePedido> detalles, String metodoPago) {
        List<DetallePedidoDTO> detallesDTO = detalles.stream()
                .map(d -> new DetallePedidoDTO(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getPrecioUnitario() * d.getCantidad()))
                .toList();
        String cliente = pedido.getUsuario() != null ? pedido.getUsuario().getEmail() : null;
        String direccion = pedido.getDireccion() != null
                ? pedido.getDireccion().getCalle() + " " + pedido.getDireccion().getNumero()
                        + ", " + pedido.getDireccion().getComuna().getNombre()
                : null;
        return new PedidoDTO(pedido.getId(), pedido.getFecha(), cliente, direccion, pedido.getEstado().getNombre(),
                pedido.getTotal(), metodoPago, detallesDTO);
    }
}
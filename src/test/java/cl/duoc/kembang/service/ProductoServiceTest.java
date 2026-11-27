package cl.duoc.kembang.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cl.duoc.kembang.exception.ResourceNotFoundException;
import cl.duoc.kembang.model.Producto;
import cl.duoc.kembang.repository.CategoriaRepository;
import cl.duoc.kembang.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("Devuelve el producto cuando el id existe")
    void obtenerPorIdExistente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Ficus Benjamina");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerPorId(1L);

        assertEquals("Ficus Benjamina", resultado.getNombre());
    }

    @Test
    @DisplayName("Lanza ResourceNotFoundException cuando el id no existe")
    void obtenerPorIdInexistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerPorId(99L));
    }
}
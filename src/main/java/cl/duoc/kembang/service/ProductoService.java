package cl.duoc.kembang.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.kembang.exception.ResourceNotFoundException;
import cl.duoc.kembang.model.Categoria;
import cl.duoc.kembang.model.Producto;
import cl.duoc.kembang.repository.CategoriaRepository;
import cl.duoc.kembang.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // Inyeccion por constructor: Spring entrega los repositorios al crear el service
    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // GET /api/productos
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // GET /api/productos/{id}
    public Producto obtenerPorId(Long id) {
        log.info("Buscando producto con id {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el producto con id " + id));
    }

    // POST /api/productos
    public Producto crear(Producto producto) {
        producto.setId(null); // el id siempre lo genera la base de datos
        producto.setCategoria(buscarCategoria(producto.getCategoria()));
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado con id {}", guardado.getId());
        return guardado;
    }

    // PUT /api/productos/{id}: reemplaza el producto COMPLETO
    public Producto actualizar(Long id, Producto datos) {
        Producto existente = obtenerPorId(id); // si no existe, lanza 404
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setDescuento(datos.getDescuento());
        existente.setStock(datos.getStock());
        existente.setImagenUrl(datos.getImagenUrl());
        existente.setCategoria(buscarCategoria(datos.getCategoria()));
        log.info("Producto con id {} actualizado", id);
        return productoRepository.save(existente);
    }

    // DELETE /api/productos/{id}
    public void eliminar(Long id) {
        Producto existente = obtenerPorId(id); // si no existe, lanza 404
        productoRepository.delete(existente);
        log.info("Producto con id {} eliminado", id);
    }

    // Comprueba que la categoria enviada exista en la base de datos
    private Categoria buscarCategoria(Categoria categoria) {
        if (categoria == null || categoria.getId() == null) {
            throw new ResourceNotFoundException("Debe indicar el id de la categoria");
        }
        return categoriaRepository.findById(categoria.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la categoria con id " + categoria.getId()));
    }
} 
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

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        log.info("Buscando producto con id {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el producto con id " + id));
    }

    public Producto crear(Producto producto) {
        producto.setId(null);
        producto.setCategoria(buscarCategoria(producto.getCategoria()));
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado con id {}", guardado.getId());
        return guardado;
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto existente = obtenerPorId(id);
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

    public void eliminar(Long id) {
        Producto existente = obtenerPorId(id);
        productoRepository.delete(existente);
        log.info("Producto con id {} eliminado", id);
    }

    private Categoria buscarCategoria(Categoria categoria) {
        if (categoria == null || categoria.getId() == null) {
            throw new ResourceNotFoundException("Debe indicar el id de la categoria");
        }
        return categoriaRepository.findById(categoria.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la categoria con id " + categoria.getId()));
    }
}
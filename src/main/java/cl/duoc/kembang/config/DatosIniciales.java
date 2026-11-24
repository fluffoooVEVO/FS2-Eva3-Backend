package cl.duoc.kembang.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.duoc.kembang.model.Categoria;
import cl.duoc.kembang.model.Producto;
import cl.duoc.kembang.repository.CategoriaRepository;
import cl.duoc.kembang.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatosIniciales implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DatosIniciales(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("Ya existen productos: no se cargan datos iniciales");
            return;
        }

        Categoria plantas = obtenerOCrear("Plantas");
        Categoria maceteros = obtenerOCrear("Maceteros");
        Categoria cuidado = obtenerOCrear("Cuidado vegetal");

        productoRepository.saveAll(List.of(
            new Producto(null, "Cactus Euphorbia",
                "Escultura viva de cero complicaciones: casi no necesita agua y aguanta el sol directo sin problema",
                199990, 25, 10, null, plantas),
            new Producto(null, "Ficus Benjamina",
                "Ficus de follaje fino y elegante que da un aire de árbol señorial. Resistente, dentro o fuera. El clásico atemporal que llena un espacio con presencia natural.",
                9990, 25, 10, null, plantas),
            new Producto(null, "Oxalis Triangularis Trébol Africano",
                "Ficus de follaje fino y elegante que da un aire de árbol señorial. Resistente, dentro o fuera. El clásico atemporal que llena un espacio con presencia natural.",
                9990, 25, 10, null, plantas),
            new Producto(null, "Macetero Ranurado XL 35x37cm",
                "Macetero Ranurado XL: textura acanalada y buen tamaño que suma carácter arquitectónico. Moderno y con estilo. Presencia y diseño para tus plantas grandes.",
                19990, 10, 100, null, maceteros),
            new Producto(null, "Colgante Macramé Planta Eucalipto",
                "Colgante de macramé tejido a mano para dar altura y estilo a tus plantas. Fibra natural, aire bohemio. El detalle artesanal que transforma cualquier rincón.",
                19990, 10, 100, null, maceteros),
            new Producto(null, "Mini Pedestales",
                "Un detalle pequeño que cambia por completo la composición. Los Mini Pedestales elevan macetas compactas y ayudan a crear distintos niveles en repisas, muebles o rincones verdes con un look limpio y minimalista.",
                19990, 10, 100, null, maceteros),
            new Producto(null, "Jabón Potásico Whenua",
                "Control natural de plagas sin químicos agresivos. Limpia pulgones y cochinillas mientras cuida tu planta y el ambiente. La solución orgánica que tus plantas agradecen.",
                7990, 0, 50, null, cuidado),
            new Producto(null, "Bioprotec",
                "Bioprotec: escudo biológico que refuerza las defensas naturales de tus plantas contra plagas y estrés. Prevención que funciona. Plantas protegidas, plantas felices",
                7990, 0, 50, null, cuidado),
            new Producto(null, "Insecticida Chetak Whenua",
                "El insecticida que elimina las plagas que atacan tus plantas, protegiéndolas para que sigan creciendo sanas. Actúa rápido, cuida lo que tanto te costó cultivar.",
                7990, 0, 30, null, cuidado)
        ));
        log.info("Datos iniciales cargados: 3 categorias y 9 productos");
    }

    private Categoria obtenerOCrear(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombre)));
    }
}
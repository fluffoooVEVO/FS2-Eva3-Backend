package cl.duoc.kembang.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.duoc.kembang.model.Comuna;
import cl.duoc.kembang.model.Region;
import cl.duoc.kembang.repository.ComunaRepository;
import cl.duoc.kembang.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatosUbicacion implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    public DatosUbicacion(RegionRepository regionRepository, ComunaRepository comunaRepository) {
        this.regionRepository = regionRepository;
        this.comunaRepository = comunaRepository;
    }

    @Override
    public void run(String... args) {
        if (comunaRepository.count() > 0) {
            log.info("Ya existen comunas: no se cargan ubicaciones");
            return;
        }

        Region metropolitana = obtenerOCrear("Metropolitana");
        Region valparaiso = obtenerOCrear("Valparaíso");

        comunaRepository.saveAll(List.of(
                new Comuna(null, "Santiago", metropolitana),
                new Comuna(null, "Providencia", metropolitana),
                new Comuna(null, "Las Condes", metropolitana),
                new Comuna(null, "Maipú", metropolitana),
                new Comuna(null, "Puente Alto", metropolitana),
                new Comuna(null, "La Florida", metropolitana),
                new Comuna(null, "Valparaíso", valparaiso),
                new Comuna(null, "Viña del Mar", valparaiso)));

        log.info("Regiones y comunas cargadas");
    }

    private Region obtenerOCrear(String nombre) {
        return regionRepository.findByNombre(nombre)
                .orElseGet(() -> regionRepository.save(new Region(null, nombre)));
    }
}
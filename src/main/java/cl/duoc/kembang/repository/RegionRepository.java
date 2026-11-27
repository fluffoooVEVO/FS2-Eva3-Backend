package cl.duoc.kembang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.kembang.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByNombre(String nombre);
}
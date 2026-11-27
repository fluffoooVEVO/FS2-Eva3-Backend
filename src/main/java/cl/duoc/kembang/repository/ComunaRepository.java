package cl.duoc.kembang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.kembang.model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {
}
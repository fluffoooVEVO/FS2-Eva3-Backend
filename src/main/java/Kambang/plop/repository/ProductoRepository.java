package Kambang.plop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Kambang.plop.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
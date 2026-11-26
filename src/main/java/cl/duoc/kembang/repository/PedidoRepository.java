package cl.duoc.kembang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.kembang.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
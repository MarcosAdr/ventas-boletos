package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long> {
}

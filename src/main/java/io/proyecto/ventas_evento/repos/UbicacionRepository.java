package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}

package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolRepository extends JpaRepository<Rol, Long> {
}

package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Plan;
import io.proyecto.ventas_evento.domain.Rol;
import io.proyecto.ventas_evento.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreoIgnoreCase(String correo);

    Usuario findFirstByIdPlan(Plan plan);

    Usuario findFirstByIdUsuario(Rol rol);

}

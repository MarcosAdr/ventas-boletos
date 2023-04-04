package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.domain.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Valoracion findFirstByIdUsuario(Usuario usuario);

    Valoracion findFirstByIdEvento(Evento evento);

}

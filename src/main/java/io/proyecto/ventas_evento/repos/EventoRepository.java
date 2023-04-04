package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Categoria;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.TipoEvento;
import io.proyecto.ventas_evento.domain.Ubicacion;
import io.proyecto.ventas_evento.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepository extends JpaRepository<Evento, Long> {

    Evento findFirstByIdCategoria(Categoria categoria);

    Evento findFirstByIdTipoEvento(TipoEvento tipoEvento);

    Evento findFirstByIdUbicacion(Ubicacion ubicacion);

    Evento findFirstByIdUsuario(Usuario usuario);

}

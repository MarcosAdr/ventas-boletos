package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompraRepository extends JpaRepository<Compra, Long> {

    Compra findFirstByIdUsuario(Usuario usuario);

    Compra findFirstByIdEvento(Evento evento);

}

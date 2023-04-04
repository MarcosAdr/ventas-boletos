package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findFirstByIdCompra(Compra compra);

    Ticket findFirstByEventoId(Evento evento);

}

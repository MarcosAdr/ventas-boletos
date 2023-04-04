package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Ticket;
import io.proyecto.ventas_evento.model.TicketDTO;
import io.proyecto.ventas_evento.repos.CompraRepository;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.TicketRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;

    public TicketService(final TicketRepository ticketRepository,
            final CompraRepository compraRepository, final EventoRepository eventoRepository) {
        this.ticketRepository = ticketRepository;
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<TicketDTO> findAll() {
        final List<Ticket> tickets = ticketRepository.findAll(Sort.by("id"));
        return tickets.stream()
                .map((ticket) -> mapToDTO(ticket, new TicketDTO()))
                .toList();
    }

    public TicketDTO get(final Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);
        return ticketRepository.save(ticket).getId();
    }

    public void update(final Long id, final TicketDTO ticketDTO) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ticketDTO, ticket);
        ticketRepository.save(ticket);
    }

    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket, final TicketDTO ticketDTO) {
        ticketDTO.setId(ticket.getId());
        ticketDTO.setCodigoqr(ticket.getCodigoqr());
        ticketDTO.setFechaTicket(ticket.getFechaTicket());
        ticketDTO.setPrecioTotal(ticket.getPrecioTotal());
        ticketDTO.setIdCompra(ticket.getIdCompra() == null ? null : ticket.getIdCompra().getId());
        ticketDTO.setEventoId(ticket.getEventoId() == null ? null : ticket.getEventoId().getId());
        return ticketDTO;
    }

    private Ticket mapToEntity(final TicketDTO ticketDTO, final Ticket ticket) {
        ticket.setCodigoqr(ticketDTO.getCodigoqr());
        ticket.setFechaTicket(ticketDTO.getFechaTicket());
        ticket.setPrecioTotal(ticketDTO.getPrecioTotal());
        final Compra idCompra = ticketDTO.getIdCompra() == null ? null : compraRepository.findById(ticketDTO.getIdCompra())
                .orElseThrow(() -> new NotFoundException("idCompra not found"));
        ticket.setIdCompra(idCompra);
        final Evento eventoId = ticketDTO.getEventoId() == null ? null : eventoRepository.findById(ticketDTO.getEventoId())
                .orElseThrow(() -> new NotFoundException("eventoId not found"));
        ticket.setEventoId(eventoId);
        return ticket;
    }

}

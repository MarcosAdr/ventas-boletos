package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Ticket;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.CompraDTO;
import io.proyecto.ventas_evento.repos.CompraRepository;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.TicketRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;

    public CompraService(final CompraRepository compraRepository,
            final EventoRepository eventoRepository, final UsuarioRepository usuarioRepository,
            final TicketRepository ticketRepository) {
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<CompraDTO> findAll() {
        final List<Compra> compras = compraRepository.findAll(Sort.by("id"));
        return compras.stream()
                .map((compra) -> mapToDTO(compra, new CompraDTO()))
                .toList();
    }

    public CompraDTO get(final Long id) {
        return compraRepository.findById(id)
                .map(compra -> mapToDTO(compra, new CompraDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CompraDTO compraDTO) {
        final Compra compra = new Compra();
        mapToEntity(compraDTO, compra);
        return compraRepository.save(compra).getId();
    }

    public void update(final Long id, final CompraDTO compraDTO) {
        final Compra compra = compraRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(compraDTO, compra);
        compraRepository.save(compra);
    }

    public void delete(final Long id) {
        compraRepository.deleteById(id);
    }

    private CompraDTO mapToDTO(final Compra compra, final CompraDTO compraDTO) {
        compraDTO.setId(compra.getId());
        compraDTO.setCantidadEntradas(compra.getCantidadEntradas());
        compraDTO.setFechaCompra(compra.getFechaCompra());
        compraDTO.setIdEvento(compra.getIdEvento() == null ? null : compra.getIdEvento().getId());
        compraDTO.setIdUsuario(compra.getIdUsuario() == null ? null : compra.getIdUsuario().getId());
        return compraDTO;
    }

    private Compra mapToEntity(final CompraDTO compraDTO, final Compra compra) {
        compra.setCantidadEntradas(compraDTO.getCantidadEntradas());
        compra.setFechaCompra(compraDTO.getFechaCompra());
        final Evento idEvento = compraDTO.getIdEvento() == null ? null : eventoRepository.findById(compraDTO.getIdEvento())
                .orElseThrow(() -> new NotFoundException("idEvento not found"));
        compra.setIdEvento(idEvento);
        final Usuario idUsuario = compraDTO.getIdUsuario() == null ? null : usuarioRepository.findById(compraDTO.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("idUsuario not found"));
        compra.setIdUsuario(idUsuario);
        return compra;
    }

    public String getReferencedWarning(final Long id) {
        final Compra compra = compraRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket idCompraTicket = ticketRepository.findFirstByIdCompra(compra);
        if (idCompraTicket != null) {
            return WebUtils.getMessage("compra.ticket.idCompra.referenced", idCompraTicket.getId());
        }
        return null;
    }

}

package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Categoria;
import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Ticket;
import io.proyecto.ventas_evento.domain.TipoEvento;
import io.proyecto.ventas_evento.domain.Ubicacion;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.domain.Valoracion;
import io.proyecto.ventas_evento.model.EventoDTO;
import io.proyecto.ventas_evento.repos.CategoriaRepository;
import io.proyecto.ventas_evento.repos.CompraRepository;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.TicketRepository;
import io.proyecto.ventas_evento.repos.TipoEventoRepository;
import io.proyecto.ventas_evento.repos.UbicacionRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.repos.ValoracionRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final TipoEventoRepository tipoEventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ValoracionRepository valoracionRepository;
    private final CompraRepository compraRepository;
    private final TicketRepository ticketRepository;

    public EventoService(final EventoRepository eventoRepository,
            final TipoEventoRepository tipoEventoRepository,
            final CategoriaRepository categoriaRepository,
            final UbicacionRepository ubicacionRepository,
            final UsuarioRepository usuarioRepository,
            final ValoracionRepository valoracionRepository,
            final CompraRepository compraRepository, final TicketRepository ticketRepository) {
        this.eventoRepository = eventoRepository;
        this.tipoEventoRepository = tipoEventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.valoracionRepository = valoracionRepository;
        this.compraRepository = compraRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<EventoDTO> findAll() {
        final List<Evento> eventos = eventoRepository.findAll(Sort.by("id"));
        return eventos.stream()
                .map((evento) -> mapToDTO(evento, new EventoDTO()))
                .toList();
    }

    public EventoDTO get(final Long id) {
        return eventoRepository.findById(id)
                .map(evento -> mapToDTO(evento, new EventoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventoDTO eventoDTO) {
        final Evento evento = new Evento();
        mapToEntity(eventoDTO, evento);
        return eventoRepository.save(evento).getId();
    }

    public void update(final Long id, final EventoDTO eventoDTO) {
        final Evento evento = eventoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventoDTO, evento);
        eventoRepository.save(evento);
    }

    public void delete(final Long id) {
        eventoRepository.deleteById(id);
    }

    private EventoDTO mapToDTO(final Evento evento, final EventoDTO eventoDTO) {
        eventoDTO.setId(evento.getId());
        eventoDTO.setNombre(evento.getNombre());
        eventoDTO.setDescripcion(evento.getDescripcion());
        eventoDTO.setFechaInicio(evento.getFechaInicio());
        eventoDTO.setFechaFin(evento.getFechaFin());
        eventoDTO.setMediaUrl(evento.getMediaUrl());
        eventoDTO.setIdTipoEvento(evento.getIdTipoEvento() == null ? null : evento.getIdTipoEvento().getId());
        eventoDTO.setIdCategoria(evento.getIdCategoria() == null ? null : evento.getIdCategoria().getId());
        eventoDTO.setIdUbicacion(evento.getIdUbicacion() == null ? null : evento.getIdUbicacion().getId());
        eventoDTO.setIdUsuario(evento.getIdUsuario() == null ? null : evento.getIdUsuario().getId());
        return eventoDTO;
    }

    private Evento mapToEntity(final EventoDTO eventoDTO, final Evento evento) {
        evento.setNombre(eventoDTO.getNombre());
        evento.setDescripcion(eventoDTO.getDescripcion());
        evento.setFechaInicio(eventoDTO.getFechaInicio());
        evento.setFechaFin(eventoDTO.getFechaFin());
        evento.setMediaUrl(eventoDTO.getMediaUrl());
        final TipoEvento idTipoEvento = eventoDTO.getIdTipoEvento() == null ? null : tipoEventoRepository.findById(eventoDTO.getIdTipoEvento())
                .orElseThrow(() -> new NotFoundException("idTipoEvento not found"));
        evento.setIdTipoEvento(idTipoEvento);
        final Categoria idCategoria = eventoDTO.getIdCategoria() == null ? null : categoriaRepository.findById(eventoDTO.getIdCategoria())
                .orElseThrow(() -> new NotFoundException("idCategoria not found"));
        evento.setIdCategoria(idCategoria);
        final Ubicacion idUbicacion = eventoDTO.getIdUbicacion() == null ? null : ubicacionRepository.findById(eventoDTO.getIdUbicacion())
                .orElseThrow(() -> new NotFoundException("idUbicacion not found"));
        evento.setIdUbicacion(idUbicacion);
        final Usuario idUsuario = eventoDTO.getIdUsuario() == null ? null : usuarioRepository.findById(eventoDTO.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("idUsuario not found"));
        evento.setIdUsuario(idUsuario);
        return evento;
    }

    public String getReferencedWarning(final Long id) {
        final Evento evento = eventoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Valoracion idEventoValoracion = valoracionRepository.findFirstByIdEvento(evento);
        if (idEventoValoracion != null) {
            return WebUtils.getMessage("evento.valoracion.idEvento.referenced", idEventoValoracion.getId());
        }
        final Compra idEventoCompra = compraRepository.findFirstByIdEvento(evento);
        if (idEventoCompra != null) {
            return WebUtils.getMessage("evento.compra.idEvento.referenced", idEventoCompra.getId());
        }
        final Ticket eventoIdTicket = ticketRepository.findFirstByEventoId(evento);
        if (eventoIdTicket != null) {
            return WebUtils.getMessage("evento.ticket.eventoId.referenced", eventoIdTicket.getId());
        }
        return null;
    }

}

package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.TipoEvento;
import io.proyecto.ventas_evento.model.TipoEventoDTO;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.TipoEventoRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TipoEventoService {

    private final TipoEventoRepository tipoEventoRepository;
    private final EventoRepository eventoRepository;

    public TipoEventoService(final TipoEventoRepository tipoEventoRepository,
            final EventoRepository eventoRepository) {
        this.tipoEventoRepository = tipoEventoRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<TipoEventoDTO> findAll() {
        final List<TipoEvento> tipoEventos = tipoEventoRepository.findAll(Sort.by("id"));
        return tipoEventos.stream()
                .map((tipoEvento) -> mapToDTO(tipoEvento, new TipoEventoDTO()))
                .toList();
    }

    public TipoEventoDTO get(final Long id) {
        return tipoEventoRepository.findById(id)
                .map(tipoEvento -> mapToDTO(tipoEvento, new TipoEventoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TipoEventoDTO tipoEventoDTO) {
        final TipoEvento tipoEvento = new TipoEvento();
        mapToEntity(tipoEventoDTO, tipoEvento);
        return tipoEventoRepository.save(tipoEvento).getId();
    }

    public void update(final Long id, final TipoEventoDTO tipoEventoDTO) {
        final TipoEvento tipoEvento = tipoEventoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tipoEventoDTO, tipoEvento);
        tipoEventoRepository.save(tipoEvento);
    }

    public void delete(final Long id) {
        tipoEventoRepository.deleteById(id);
    }

    private TipoEventoDTO mapToDTO(final TipoEvento tipoEvento, final TipoEventoDTO tipoEventoDTO) {
        tipoEventoDTO.setId(tipoEvento.getId());
        tipoEventoDTO.setNombre(tipoEvento.getNombre());
        tipoEventoDTO.setDescripcion(tipoEvento.getDescripcion());
        return tipoEventoDTO;
    }

    private TipoEvento mapToEntity(final TipoEventoDTO tipoEventoDTO, final TipoEvento tipoEvento) {
        tipoEvento.setNombre(tipoEventoDTO.getNombre());
        tipoEvento.setDescripcion(tipoEventoDTO.getDescripcion());
        return tipoEvento;
    }

    public String getReferencedWarning(final Long id) {
        final TipoEvento tipoEvento = tipoEventoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Evento idTipoEventoEvento = eventoRepository.findFirstByIdTipoEvento(tipoEvento);
        if (idTipoEventoEvento != null) {
            return WebUtils.getMessage("tipoEvento.evento.idTipoEvento.referenced", idTipoEventoEvento.getId());
        }
        return null;
    }

}

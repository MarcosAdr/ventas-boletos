package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.domain.Valoracion;
import io.proyecto.ventas_evento.model.ValoracionDTO;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.repos.ValoracionRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ValoracionService {

    private final ValoracionRepository valoracionRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public ValoracionService(final ValoracionRepository valoracionRepository,
            final EventoRepository eventoRepository, final UsuarioRepository usuarioRepository) {
        this.valoracionRepository = valoracionRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ValoracionDTO> findAll() {
        final List<Valoracion> valoracions = valoracionRepository.findAll(Sort.by("id"));
        return valoracions.stream()
                .map((valoracion) -> mapToDTO(valoracion, new ValoracionDTO()))
                .toList();
    }

    public ValoracionDTO get(final Long id) {
        return valoracionRepository.findById(id)
                .map(valoracion -> mapToDTO(valoracion, new ValoracionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ValoracionDTO valoracionDTO) {
        final Valoracion valoracion = new Valoracion();
        mapToEntity(valoracionDTO, valoracion);
        return valoracionRepository.save(valoracion).getId();
    }

    public void update(final Long id, final ValoracionDTO valoracionDTO) {
        final Valoracion valoracion = valoracionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(valoracionDTO, valoracion);
        valoracionRepository.save(valoracion);
    }

    public void delete(final Long id) {
        valoracionRepository.deleteById(id);
    }

    private ValoracionDTO mapToDTO(final Valoracion valoracion, final ValoracionDTO valoracionDTO) {
        valoracionDTO.setId(valoracion.getId());
        valoracionDTO.setComentario(valoracion.getComentario());
        valoracionDTO.setCalificacion(valoracion.getCalificacion());
        valoracionDTO.setIdEvento(valoracion.getIdEvento() == null ? null : valoracion.getIdEvento().getId());
        valoracionDTO.setIdUsuario(valoracion.getIdUsuario() == null ? null : valoracion.getIdUsuario().getId());
        return valoracionDTO;
    }

    private Valoracion mapToEntity(final ValoracionDTO valoracionDTO, final Valoracion valoracion) {
        valoracion.setComentario(valoracionDTO.getComentario());
        valoracion.setCalificacion(valoracionDTO.getCalificacion());
        final Evento idEvento = valoracionDTO.getIdEvento() == null ? null : eventoRepository.findById(valoracionDTO.getIdEvento())
                .orElseThrow(() -> new NotFoundException("idEvento not found"));
        valoracion.setIdEvento(idEvento);
        final Usuario idUsuario = valoracionDTO.getIdUsuario() == null ? null : usuarioRepository.findById(valoracionDTO.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("idUsuario not found"));
        valoracion.setIdUsuario(idUsuario);
        return valoracion;
    }

}

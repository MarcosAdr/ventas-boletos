package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Ubicacion;
import io.proyecto.ventas_evento.model.UbicacionDTO;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.UbicacionRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final EventoRepository eventoRepository;

    public UbicacionService(final UbicacionRepository ubicacionRepository,
            final EventoRepository eventoRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<UbicacionDTO> findAll() {
        final List<Ubicacion> ubicacions = ubicacionRepository.findAll(Sort.by("id"));
        return ubicacions.stream()
                .map((ubicacion) -> mapToDTO(ubicacion, new UbicacionDTO()))
                .toList();
    }

    public UbicacionDTO get(final Long id) {
        return ubicacionRepository.findById(id)
                .map(ubicacion -> mapToDTO(ubicacion, new UbicacionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UbicacionDTO ubicacionDTO) {
        final Ubicacion ubicacion = new Ubicacion();
        mapToEntity(ubicacionDTO, ubicacion);
        return ubicacionRepository.save(ubicacion).getId();
    }

    public void update(final Long id, final UbicacionDTO ubicacionDTO) {
        final Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ubicacionDTO, ubicacion);
        ubicacionRepository.save(ubicacion);
    }

    public void delete(final Long id) {
        ubicacionRepository.deleteById(id);
    }

    private UbicacionDTO mapToDTO(final Ubicacion ubicacion, final UbicacionDTO ubicacionDTO) {
        ubicacionDTO.setId(ubicacion.getId());
        ubicacionDTO.setDireccion(ubicacion.getDireccion());
        ubicacionDTO.setProvincia(ubicacion.getProvincia());
        ubicacionDTO.setCiudad(ubicacion.getCiudad());
        ubicacionDTO.setLatitud(ubicacion.getLatitud());
        ubicacionDTO.setLongitud(ubicacion.getLongitud());
        return ubicacionDTO;
    }

    private Ubicacion mapToEntity(final UbicacionDTO ubicacionDTO, final Ubicacion ubicacion) {
        ubicacion.setDireccion(ubicacionDTO.getDireccion());
        ubicacion.setProvincia(ubicacionDTO.getProvincia());
        ubicacion.setCiudad(ubicacionDTO.getCiudad());
        ubicacion.setLatitud(ubicacionDTO.getLatitud());
        ubicacion.setLongitud(ubicacionDTO.getLongitud());
        return ubicacion;
    }

    public String getReferencedWarning(final Long id) {
        final Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Evento idUbicacionEvento = eventoRepository.findFirstByIdUbicacion(ubicacion);
        if (idUbicacionEvento != null) {
            return WebUtils.getMessage("ubicacion.evento.idUbicacion.referenced", idUbicacionEvento.getId());
        }
        return null;
    }

}

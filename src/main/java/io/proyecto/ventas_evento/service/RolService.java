package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Rol;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.RolDTO;
import io.proyecto.ventas_evento.repos.RolRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(final RolRepository rolRepository,
            final UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RolDTO> findAll() {
        final List<Rol> rols = rolRepository.findAll(Sort.by("id"));
        return rols.stream()
                .map((rol) -> mapToDTO(rol, new RolDTO()))
                .toList();
    }

    public RolDTO get(final Long id) {
        return rolRepository.findById(id)
                .map(rol -> mapToDTO(rol, new RolDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RolDTO rolDTO) {
        final Rol rol = new Rol();
        mapToEntity(rolDTO, rol);
        return rolRepository.save(rol).getId();
    }

    public void update(final Long id, final RolDTO rolDTO) {
        final Rol rol = rolRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rolDTO, rol);
        rolRepository.save(rol);
    }

    public void delete(final Long id) {
        rolRepository.deleteById(id);
    }

    private RolDTO mapToDTO(final Rol rol, final RolDTO rolDTO) {
        rolDTO.setId(rol.getId());
        rolDTO.setNombre(rol.getNombre());
        rolDTO.setDescripcion(rol.getDescripcion());
        return rolDTO;
    }

    private Rol mapToEntity(final RolDTO rolDTO, final Rol rol) {
        rol.setNombre(rolDTO.getNombre());
        rol.setDescripcion(rolDTO.getDescripcion());
        return rol;
    }

    public String getReferencedWarning(final Long id) {
        final Rol rol = rolRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Usuario idUsuarioUsuario = usuarioRepository.findFirstByIdUsuario(rol);
        if (idUsuarioUsuario != null) {
            return WebUtils.getMessage("rol.usuario.idUsuario.referenced", idUsuarioUsuario.getId());
        }
        return null;
    }

}

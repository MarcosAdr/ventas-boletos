package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Plan;
import io.proyecto.ventas_evento.domain.Rol;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.domain.Valoracion;
import io.proyecto.ventas_evento.model.UsuarioDTO;
import io.proyecto.ventas_evento.repos.CompraRepository;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.PlanRepository;
import io.proyecto.ventas_evento.repos.RolRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.repos.ValoracionRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;
    private final RolRepository rolRepository;
    private final ValoracionRepository valoracionRepository;
    private final EventoRepository eventoRepository;
    private final CompraRepository compraRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final PlanRepository planRepository, final RolRepository rolRepository,
            final ValoracionRepository valoracionRepository,
            final EventoRepository eventoRepository, final CompraRepository compraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
        this.rolRepository = rolRepository;
        this.valoracionRepository = valoracionRepository;
        this.eventoRepository = eventoRepository;
        this.compraRepository = compraRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map((usuario) -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setIdPlan(usuario.getIdPlan() == null ? null : usuario.getIdPlan().getId());
        usuarioDTO.setIdUsuario(usuario.getIdUsuario() == null ? null : usuario.getIdUsuario().stream()
                .map(rol -> rol.getId())
                .toList());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(usuarioDTO.getContrasena());
        final Plan idPlan = usuarioDTO.getIdPlan() == null ? null : planRepository.findById(usuarioDTO.getIdPlan())
                .orElseThrow(() -> new NotFoundException("idPlan not found"));
        usuario.setIdPlan(idPlan);
        final List<Rol> idUsuario = rolRepository.findAllById(
                usuarioDTO.getIdUsuario() == null ? Collections.emptyList() : usuarioDTO.getIdUsuario());
        if (idUsuario.size() != (usuarioDTO.getIdUsuario() == null ? 0 : usuarioDTO.getIdUsuario().size())) {
            throw new NotFoundException("one of idUsuario not found");
        }
        usuario.setIdUsuario(idUsuario.stream().collect(Collectors.toSet()));
        return usuario;
    }

    public boolean correoExists(final String correo) {
        return usuarioRepository.existsByCorreoIgnoreCase(correo);
    }

    public String getReferencedWarning(final Long id) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Valoracion idUsuarioValoracion = valoracionRepository.findFirstByIdUsuario(usuario);
        if (idUsuarioValoracion != null) {
            return WebUtils.getMessage("usuario.valoracion.idUsuario.referenced", idUsuarioValoracion.getId());
        }
        final Evento idUsuarioEvento = eventoRepository.findFirstByIdUsuario(usuario);
        if (idUsuarioEvento != null) {
            return WebUtils.getMessage("usuario.evento.idUsuario.referenced", idUsuarioEvento.getId());
        }
        final Compra idUsuarioCompra = compraRepository.findFirstByIdUsuario(usuario);
        if (idUsuarioCompra != null) {
            return WebUtils.getMessage("usuario.compra.idUsuario.referenced", idUsuarioCompra.getId());
        }
        return null;
    }

}

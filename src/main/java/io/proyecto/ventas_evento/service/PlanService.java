package io.proyecto.ventas_evento.service;

import io.proyecto.ventas_evento.domain.Plan;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.PlanDTO;
import io.proyecto.ventas_evento.repos.PlanRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.util.NotFoundException;
import io.proyecto.ventas_evento.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;

    public PlanService(final PlanRepository planRepository,
            final UsuarioRepository usuarioRepository) {
        this.planRepository = planRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PlanDTO> findAll() {
        final List<Plan> plans = planRepository.findAll(Sort.by("id"));
        return plans.stream()
                .map((plan) -> mapToDTO(plan, new PlanDTO()))
                .toList();
    }

    public PlanDTO get(final Long id) {
        return planRepository.findById(id)
                .map(plan -> mapToDTO(plan, new PlanDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PlanDTO planDTO) {
        final Plan plan = new Plan();
        mapToEntity(planDTO, plan);
        return planRepository.save(plan).getId();
    }

    public void update(final Long id, final PlanDTO planDTO) {
        final Plan plan = planRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(planDTO, plan);
        planRepository.save(plan);
    }

    public void delete(final Long id) {
        planRepository.deleteById(id);
    }

    private PlanDTO mapToDTO(final Plan plan, final PlanDTO planDTO) {
        planDTO.setId(plan.getId());
        planDTO.setNombre(plan.getNombre());
        planDTO.setPrecio(plan.getPrecio());
        planDTO.setDescripcion(plan.getDescripcion());
        return planDTO;
    }

    private Plan mapToEntity(final PlanDTO planDTO, final Plan plan) {
        plan.setNombre(planDTO.getNombre());
        plan.setPrecio(planDTO.getPrecio());
        plan.setDescripcion(planDTO.getDescripcion());
        return plan;
    }

    public String getReferencedWarning(final Long id) {
        final Plan plan = planRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Usuario idPlanUsuario = usuarioRepository.findFirstByIdPlan(plan);
        if (idPlanUsuario != null) {
            return WebUtils.getMessage("plan.usuario.idPlan.referenced", idPlanUsuario.getId());
        }
        return null;
    }

}

package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanRepository extends JpaRepository<Plan, Long> {
}
